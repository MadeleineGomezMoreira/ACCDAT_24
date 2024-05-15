package ui.methods.patientcrud;

import jakarta.inject.Inject;
import model.Credential;
import model.Patient;
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

        System.out.println("Enter the name of the patient: ");
        name = sc.nextLine();

        System.out.println("Enter the birth date of the patient (yyyy-mm-dd): ");
        birthDate = sc.nextLine();
        birthDateLocal = LocalDate.parse(birthDate);

        System.out.println("Enter the phone number of the patient: ");
        phone = sc.nextLine();

        System.out.println("Enter the username of the patient's account: ");
        username = sc.nextLine();

        System.out.println("Enter the password of the patient's account: ");
        password = sc.nextLine();

        Credential credential = new Credential(0, username, password, 0);
        Patient patient = new Patient(0, name, birthDateLocal, phone, credential);

        patientService.savePatient(patient)
                .peek(i -> System.out.println("Patient saved successfully!"))
                .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));

    }
}
