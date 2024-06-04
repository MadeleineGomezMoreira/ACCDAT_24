package ui.methods.patientcrud;

import jakarta.inject.Inject;
import model.hibernate.CredentialEntity;
import model.hibernate.PatientEntity;
import model.mongo.Patient;
import services.PatientService;

import java.time.LocalDate;
import java.util.Scanner;

public class SavePatient {

    private final PatientService patientService;

    @Inject
    public SavePatient(PatientService patientService) {
        this.patientService = patientService;
    }

    public void savePatient() {
        Scanner sc = new Scanner(System.in);

        String name;
        String birthDate;
        LocalDate birthDateLocal;
        String phone;
        String username;
        String password;

        System.out.println("Enter the name of the patientEntity: ");
        name = sc.nextLine();

        System.out.println("Enter the birth date of the patientEntity (yyyy-mm-dd): ");
        birthDate = sc.nextLine();
        birthDateLocal = LocalDate.parse(birthDate);

        System.out.println("Enter the phone number of the patientEntity: ");
        phone = sc.nextLine();

        System.out.println("Enter the username of the patientEntity's account: ");
        username = sc.nextLine();

        System.out.println("Enter the password of the patientEntity's account: ");
        password = sc.nextLine();

        CredentialEntity credentialEntity = new CredentialEntity(0, username, password);
        Patient patient = Patient.builder()
                .name(name)
                .birthDate(birthDateLocal)
                .phone(phone)
                .build();

        patientService.savePatient(patient, credentialEntity)
                .peek(i -> System.out.println("PatientEntity saved successfully!"))
                .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));

    }
}
