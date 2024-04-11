package ui.main;

import io.vavr.control.Either;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import model.Patient;
import model.error.AppError;
import services.LoginService;
import services.PatientService;
import ui.methods.*;

import java.util.Scanner;

//show medical r by patient (show all patients (each has a mr))

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

                    if(service.isAdmin(username,password)) { //ADMIN FUNCTIONALITY
                        //MENU

                        System.out.println("""
                            MENU:
                            1. Show all patients
                            2. Show medical records by patient
                            3. Append a new medical record with medication
                            4. Delete a patient
                            5. Show patients medicated with a specific medicine
                            6. Show the medication prescribed to a specific patient
                            7. Show the patient with the most medical records
                            8. Show the date when more patients have appointments
                            9. Exit
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
                                showMrByPatient.showMrByPatient(0);
                                break;
                            case 3:
                                SaveRecordWithMedication saveRecordWithMedication = container.select(SaveRecordWithMedication.class).get();
                                saveRecordWithMedication.saveRecordWithMedication();
                                break;
                            case 4:
                                DeletePatient deletePatient = container.select(DeletePatient.class).get();
                                deletePatient.deletePatient();
                                break;
                            case 5:
                                ShowPatientsByMedication showPatientsByMedication = container.select(ShowPatientsByMedication.class).get();
                                showPatientsByMedication.showPatientByMedication();
                                break;
                            case 6:
                                ShowPatientMedication showPatientMedication = container.select(ShowPatientMedication.class).get();
                                showPatientMedication.showPatientMedication(0);
                                break;
                            case 7:
                                ShowPatientWithMostMedicalRecords showPatientWithMostMedicalRecords = container.select(ShowPatientWithMostMedicalRecords.class).get();
                                showPatientWithMostMedicalRecords.showPatientWithMostMedicalRecords();
                                break;
                            case 8:
                                ShowMostSharedAppointmentDate showMostSharedAppointmentDate = container.select(ShowMostSharedAppointmentDate.class).get();
                                showMostSharedAppointmentDate.showMostSharedAppointmentDate();
                                break;
                            case 9:
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
                        int patientId = patient.get().getId();

                        //MENU
                        System.out.println("""
                            MENU:
                            1. See your medical records
                            2. See your medication
                            3. Exit
                            """);

                        int choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.println("Showing medical records...");
                                ShowMrByPatient showMrByPatient = container.select(ShowMrByPatient.class).get();
                                showMrByPatient.showMrByPatient(patientId);
                                break;
                            case 2:
                                System.out.println("Showing medication...");
                                ShowPatientMedication showPatientMedication = container.select(ShowPatientMedication.class).get();
                                showPatientMedication.showPatientMedication(patientId);
                                break;
                            case 3:
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
