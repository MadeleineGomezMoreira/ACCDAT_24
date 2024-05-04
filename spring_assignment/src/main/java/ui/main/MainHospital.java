package ui.main;

import common.Constants;
import io.vavr.control.Either;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import model.Patient;
import model.error.AppError;
import services.LoginService;
import services.PatientService;
import ui.methods.*;

import java.util.Scanner;

public class MainHospital {
    public static void main(String[] args) {

        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        Scanner sc = new Scanner(System.in);

        LoginService service = container.select(LoginService.class).get();

        System.out.println("WELCOME TO THE HOSPITAL SYSTEM");
        System.out.println("Please enter your username: (admin - root | patient - user1)");
        String username = sc.nextLine();
        System.out.println("Please enter your password: (admin - 2dam | patient - pass1)");
        String password = sc.nextLine();

        Either<AppError, Boolean> loginResult = service.login(username, password);

        if (loginResult.isRight()) {

            Boolean loggedIn = loginResult.get();

            if (loggedIn) {
                System.out.println("You are logged in!");

                boolean exit = false;

                while (!exit) {

                    if (service.isAdmin(username, password)) { //ADMIN FUNCTIONALITY
                        //MENU

                        System.out.println("""
                                MENU:
                                1. Show the medications of each medical record
                                2. Append a new medication to the newest medical record of patient
                                3. Modify the dosage of a prescribed medication
                                4. Delete old medical records
                                5. Show the name of the patients which were prescribed 400mg of Ibuprofen
                                6. Show the name and the number of medications of each patient
                                7. Exit
                                """);

                        int choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.println("Loading...");
                                ShowMedicationsOfEachMedicalRecord showMedicationsOfEachMedicalRecord = container.select(ShowMedicationsOfEachMedicalRecord.class).get();
                                showMedicationsOfEachMedicalRecord.showMedicationsOfEachMedicalRecord();
                                break;
                            case 2:
                                System.out.println("Loading...");
                                SaveMedicationToNewestRecord saveMedicationToNewestRecord = container.select(SaveMedicationToNewestRecord.class).get();
                                saveMedicationToNewestRecord.saveMedicationToNewestRecord();
                                break;
                            case 3:
                                UpdateMedicationDosage updateMedicationDosage = container.select(UpdateMedicationDosage.class).get();
                                updateMedicationDosage.updateMedicationDosage();
                                break;
                            case 4:
                                DeleteOldMedicalRecords deleteOldMedicalRecords = container.select(DeleteOldMedicalRecords.class).get();
                                deleteOldMedicalRecords.deleteOldMedicalRecords();
                                break;
                            case 5:
                                ShowPatientsWithSpecificPrescription showPatientsWithSpecificPrescription = container.select(ShowPatientsWithSpecificPrescription.class).get();
                                showPatientsWithSpecificPrescription.showPatientMedication();
                                break;
                            case 6:
                                ShowPatientPrescriptionAmount showPatientPrescriptionAmount = container.select(ShowPatientPrescriptionAmount.class).get();
                                showPatientPrescriptionAmount.showMostSharedAppointmentDate();
                                break;
                            case 7:
                                System.out.println("Exiting...");
                                exit = true;
                                break;
                            default:
                                System.out.println("Invalid choice!");
                        }
                    } else { //PATIENT FUNCTIONALITY

                        //retrieve patient id
                        PatientService patientService = container.select(PatientService.class).get();
                        Either<AppError, Patient> patient = patientService.getPatientByUsername(username);
                        String patientName = patient.get().getName();

                        //MENU
                        System.out.println("""
                                MENU:
                                1. Go take a walk,""" + Constants.BLANK_SPACE + patientName + "\n" + """
                                2. Exit
                                """);

                        int choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.println("Go touch some grass already...");
                                break;
                            case 2:
                                System.out.println("Exiting...");
                                exit = true;
                                break;
                            default:
                                System.out.println("Invalid choice!");
                        }
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
