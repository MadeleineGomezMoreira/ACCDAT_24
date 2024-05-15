package ui.methods.patientcrud;

import common.Constants;
import jakarta.inject.Inject;
import services.PatientService;

public class GetAllPatients {

    private final PatientService patientService;

    @Inject
    public GetAllPatients(PatientService patientService) {
        this.patientService = patientService;
    }

    public void getAllPatients() {
        patientService.getAllPatients()
                .peek(System.out::println)
                .peekLeft(appError -> System.out.println(Constants.ERROR + appError.getMessage()));
    }
}
