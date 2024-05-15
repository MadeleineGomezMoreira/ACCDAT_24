package ui.methods.queries;

import jakarta.inject.Inject;
import services.QueryService;

public class GetPatientWithMostMedicalRecords {

    private final QueryService queryService;

    @Inject
    public GetPatientWithMostMedicalRecords(QueryService queryService) {
        this.queryService = queryService;
    }

    public void getPatientWithMostMedicalRecords() {
        queryService.getPatientIdWithMostMedicalRecords()
                .peek(result -> System.out.println("The patient with most medical records has the following ID: " + result))
                .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
