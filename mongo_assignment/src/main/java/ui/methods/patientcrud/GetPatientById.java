package ui.methods.patientcrud;

import common.Constants;
import jakarta.inject.Inject;
import services.PatientService;

import java.util.Scanner;

public class GetPatientById {

    private final PatientService patientService;

    @Inject
    public GetPatientById(PatientService patientService) {
        this.patientService = patientService;
    }

    public void getPatientById() {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the patient's id: ");
        int id = sc.nextInt();
        sc.nextLine();

        patientService.getPatientById(id)
                .peek(System.out::println)
                .peekLeft(appError -> System.out.println(Constants.ERROR + appError.getMessage()));
    }
}
