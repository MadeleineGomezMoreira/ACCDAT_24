package ui.methods.patientcrud;

import jakarta.inject.Inject;
import model.Patient;
import services.PatientService;

import java.time.LocalDate;
import java.util.Scanner;

public class UpdatePatient {

    private final PatientService patientService;

    @Inject
    public UpdatePatient(PatientService patientService) {
        this.patientService = patientService;
    }

    public void updatePatient() {
        Scanner sc = new Scanner(System.in);

        int id;
        String name;
        String birthDate;
        String phone;
        LocalDate birthDateLocal;

        System.out.println("Please enter the ID of the patient you want to update:");
        id = sc.nextInt();
        sc.nextLine();

        System.out.println("Please enter the new name:");
        name = sc.nextLine();

        System.out.println("Please enter the new birth date: (YYYY-MM-DD)");
        birthDate = sc.nextLine();
        birthDateLocal = LocalDate.parse(birthDate);

        System.out.println("Please enter the new phone number:");
        phone = sc.nextLine();

        patientService.updatePatient(new Patient(id, name, birthDateLocal, phone))
                .peek(i -> System.out.println("Patient updated successfully!"))
                .peekLeft(appError -> System.out.println("ERROR: " + appError.getMessage()));


    }
}
