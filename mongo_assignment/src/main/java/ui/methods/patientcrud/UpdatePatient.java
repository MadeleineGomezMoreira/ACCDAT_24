package ui.methods.patientcrud;

import common.Constants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.error.AppError;
import model.mongo.Patient;
import org.bson.types.ObjectId;
import services.PatientService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class UpdatePatient {

    private final PatientService patientService;

    @Inject
    public UpdatePatient(PatientService patientService) {
        this.patientService = patientService;
    }

    public void updatePatient() {
        Scanner sc = new Scanner(System.in);

        String name;
        String birthDate;
        String phone;
        LocalDate birthDateLocal;

        Either<AppError, HashMap<Integer, ObjectId>> allPatientsIDs = patientService.getAllPatientsIDs();

        if (allPatientsIDs.isRight()) {
            allPatientsIDs.get().forEach((key, value) -> System.out.println(key + " - " + value));

            System.out.println("Enter the patient's id: (select the number associated to its object Id)");
            int id = sc.nextInt();
            sc.nextLine();

            ObjectId objectId = allPatientsIDs.get().get(id);

            System.out.println("Please enter the new name:");
            name = sc.nextLine();

            System.out.println("Please enter the new birth date: (YYYY-MM-DD)");
            birthDate = sc.nextLine();
            birthDateLocal = LocalDate.parse(birthDate);

            System.out.println("Please enter the new phone number:");
            phone = sc.nextLine();

            //new Patient(id, name, birthDateLocal, phone)

            patientService.updatePatient(Patient.builder()
                            .patientId(objectId)
                            .name(name)
                            .birthDate(birthDateLocal)
                            .phone(phone)
                            .build())
                    .peek(i -> System.out.println("Patient updated successfully!"))
                    .peekLeft(appError -> System.out.println("ERROR: " + appError.getMessage()));


        } else {
            System.out.println(Constants.ERROR + allPatientsIDs.getLeft().getMessage());
        }
    }
}
