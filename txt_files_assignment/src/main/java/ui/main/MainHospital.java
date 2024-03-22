package ui.main;

import io.vavr.control.Either;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import model.error.AppError;
import services.LoginService;
import ui.methods.DeletePatient;
import ui.methods.SaveRecordWithMedication;
import ui.methods.ShowAllPatients;
import ui.methods.ShowMrByPatient;

import java.util.Scanner;

public class MainHospital {
    public static void main(String[] args) {

        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        Scanner sc = new Scanner(System.in);

        LoginService service = container.select(LoginService.class).get();

        System.out.println("WELCOME TO THE HOSPITAL SYSTEM");
        System.out.println("Please enter your username: (root)");
        String username = sc.nextLine();
        System.out.println("Please enter your password: (2dam)");
        String password = sc.nextLine();

        Either<AppError, Boolean> loginResult = service.login(username, password);

        if (loginResult.isRight()) {

            Boolean loggedIn = loginResult.get();

            if (loggedIn) {
                System.out.println("You are logged in!");

                boolean exit = false;

                while (!exit) {

                    //MENU
                    System.out.println("""
                        MENU:\s
                        1. Show all patients
                        2. Show medical records by patient
                        3. Append a new medical record with medication
                        4. Delete a patient
                        5. Exit
                        """);

                    int choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.println("Showing all patients...");
                            ShowAllPatients showAllPatients = container.select(ShowAllPatients.class).get();
                            showAllPatients.showAllPatients();
                            break;
                        case 2:
                            System.out.println("Showing medical records by patient...");
                            ShowMrByPatient showMrByPatient = container.select(ShowMrByPatient.class).get();
                            showMrByPatient.showMrByPatient();
                            break;
                        case 3:
                            System.out.println("Appending a new medical record with medication...");
                            SaveRecordWithMedication saveRecordWithMedication = container.select(SaveRecordWithMedication.class).get();
                            saveRecordWithMedication.saveRecordWithMedication();
                            break;
                        case 4:
                            System.out.println("Deleting a patient...");
                            DeletePatient deletePatient = container.select(DeletePatient.class).get();
                            deletePatient.deletePatient();
                            break;
                        case 5:
                            System.out.println("Exiting...");
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                }

            } else {
                System.out.println("Invalid credentials!");
            }
        } else if (loginResult.isLeft()) {
            System.out.println(loginResult.getLeft().getMessage());
        }

    }
}
