package ui;

import io.vavr.control.Either;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import model.error.AppError;
import services.LoginService;
import ui.methods.*;
import ui.methods.patientcrud.*;

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
                            6. Show medical records by patient
                            7. Append a new medical record with two medications
                            8. Delete the last medication of the patient's last record
                            9. Show the doctors that treated a specific patient
                            10. Append a new prescription to a patient's last record
                            11. Get all prescribed medication (name + dosage) from a specific patient
                            12. Import data to MongoDB
                            13. Exit
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
                            ShowRecordsByPatient showRecordsByPatient = container.select(ShowRecordsByPatient.class).get();
                            showRecordsByPatient.showRecordsWithMedication();
                            break;
                        case 7:
                            SaveMedicalRecordWithPrescription saveMedicalRecordWithPrescription = container.select(SaveMedicalRecordWithPrescription.class).get();
                            saveMedicalRecordWithPrescription.saveMedicalRecordWithPrescription();
                            break;
                        case 8:
                            DeleteLastMedicationFromLastRecord deleteLastMedicationFromLastRecord = container.select(DeleteLastMedicationFromLastRecord.class).get();
                            deleteLastMedicationFromLastRecord.deleteMedication();
                            break;
                        case 9:
                            System.out.println("Loading...");
                            GetDoctorsWhoTreatedOnePatient getDoctorsWhoTreatedOnePatient = container.select(GetDoctorsWhoTreatedOnePatient.class).get();
                            getDoctorsWhoTreatedOnePatient.getAllRecordsByPatient();
                            break;
                        case 10:
                            SaveNewPrescriptionToLastMedicalRecord saveNewPrescriptionToLastMedicalRecord = container.select(SaveNewPrescriptionToLastMedicalRecord.class).get();
                            saveNewPrescriptionToLastMedicalRecord.saveNewPrescription();
                            break;
                        case 11:
                            System.out.println("Loading...");
                            GetMedicationFromSpecificPatient getMedicationFromSpecificPatient = container.select(GetMedicationFromSpecificPatient.class).get();
                            getMedicationFromSpecificPatient.getMedicationFromSpecificPatient();
                            break;
                        case 12:
                            System.out.println("Loading...");
                            ImportDataToMongo importDataToMongo = container.select(ImportDataToMongo.class).get();
                            importDataToMongo.importDataToMongo();
                            break;
                        case 13:
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
