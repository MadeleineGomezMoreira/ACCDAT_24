package ui;

import io.vavr.control.Either;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import model.error.AppError;
import services.LoginService;
import ui.methods.*;
import ui.methods.patientcrud.*;
import ui.methods.queries.GetDateWithMostAdmissions;
import ui.methods.queries.GetNameAndMedicationNumOfEachPatient;
import ui.methods.queries.GetPatientWithMostMedicalRecords;

import java.util.Scanner;

public class MainHospital {

    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        Scanner sc = new Scanner(System.in);

        LoginService service = container.select(LoginService.class).get();

        System.out.println("WELCOME TO THE HOSPITAL SYSTEM");
        System.out.println("Please enter your username: (user1)");
        String username = sc.nextLine();
        System.out.println("Please enter your password: (pass1)");
        String password = sc.nextLine();

        Either<AppError, Boolean> loginResult = service.login(username, password);

        if (loginResult.isRight()) {
            Boolean loggedIn = loginResult.get();

            if (loggedIn) {
                System.out.println("You are logged in!");

                boolean exit = false;

                while (!exit) {
                    System.out.println("""
                            MENU:
                            1. Show all patients
                            2. Show a patient by ID
                            3. Update a patient's information
                            4. Delete a patient's account
                            5. Save a new patient
                            6. Show the medication prescribed in every medical record
                            7. Append a new medical record with two medications
                            8. Get the total amount paid by each patient, ordered by amount paid
                            9. Show all the patients with their medicalRecords
                            10. Append a new prescription to a medical record
                            11. Delete all medical records older than year 2024
                            12. Show the patient with most medical records
                            13. Show the date when most patients were admitted
                            14. Show the name and the number of medications of each patient
                            15. Exit
                            """);

                    int choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.println("Loading...");
                            GetAllPatients showMedicationsOfEachMedicalRecord = container.select(GetAllPatients.class).get();
                            showMedicationsOfEachMedicalRecord.getAllPatients();
                            break;
                        case 2:
                            GetPatientById getPatientById = container.select(GetPatientById.class).get();
                            getPatientById.getPatientById();
                            break;
                        case 3:
                            UpdatePatient updatePatient = container.select(UpdatePatient.class).get();
                            updatePatient.updatePatient();
                            break;
                        case 4:
                            DeletePatient deletePatient = container.select(DeletePatient.class).get();
                            deletePatient.deletePatient();
                            break;
                        case 5:
                            SavePatient savePatient = container.select(SavePatient.class).get();
                            savePatient.savePatient();
                            break;
                        case 6:
                            System.out.println("Loading...");
                            ShowRecordsWithMedication showRecordsWithMedication = container.select(ShowRecordsWithMedication.class).get();
                            showRecordsWithMedication.showRecordsWithMedication();
                            break;
                        case 7:
                            SaveMedicalRecordWithPrescription saveMedicalRecordWithPrescription = container.select(SaveMedicalRecordWithPrescription.class).get();
                            saveMedicalRecordWithPrescription.saveMedicalRecordWithPrescription();
                            break;
                        case 8:
                            GetTotalAmountPaidByPatient getTotalAmountPaidByPatient = container.select(GetTotalAmountPaidByPatient.class).get();
                            getTotalAmountPaidByPatient.getTotalAmountPaidByPatient();
                            break;
                        case 9:
                            System.out.println("Loading...");
                            GetAllMedicalRecordsByPatient getAllMedicalRecordsByPatient = container.select(GetAllMedicalRecordsByPatient.class).get();
                            getAllMedicalRecordsByPatient.getAllRecordsByPatient();
                            break;
                        case 10:
                            SaveNewPrescription saveNewPrescription = container.select(SaveNewPrescription.class).get();
                            saveNewPrescription.saveNewPrescription();
                            break;
                        case 11:
                            System.out.println("Loading...");
                            DeleteRecordsOlderThan2024 deleteRecordsOlderThan2024 = container.select(DeleteRecordsOlderThan2024.class).get();
                            deleteRecordsOlderThan2024.deleteRecordsOlderThan2024();
                            break;
                        case 12:
                            System.out.println("Loading...");
                            GetPatientWithMostMedicalRecords getPatientWithMostMedicalRecords = container.select(GetPatientWithMostMedicalRecords.class).get();
                            getPatientWithMostMedicalRecords.getPatientWithMostMedicalRecords();
                            break;
                        case 13:
                            System.out.println("Loading...");
                            GetDateWithMostAdmissions getDateWithMostAdmissions = container.select(GetDateWithMostAdmissions.class).get();
                            getDateWithMostAdmissions.getDateWithMostAdmissions();
                            break;
                        case 14:
                            System.out.println("Loading...");
                            GetNameAndMedicationNumOfEachPatient getNameAndMedicationNumOfEachPatient = container.select(GetNameAndMedicationNumOfEachPatient.class).get();
                            getNameAndMedicationNumOfEachPatient.getNameAndMedicationNumOfEachPatient();
                            break;
                        case 15:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                }

            }
        } else {
            System.out.println("Invalid credentials: + " + loginResult.getLeft().getMessage());
        }

    }
}
