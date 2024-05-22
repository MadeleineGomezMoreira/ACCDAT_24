package ui.methods.queries;

import jakarta.inject.Inject;
import services.QueryService;

public class GetNameAndMedicationNumOfEachPatient {

    private final QueryService queryService;

    @Inject
    public GetNameAndMedicationNumOfEachPatient(QueryService queryService) {
        this.queryService = queryService;
    }

    public void getNameAndMedicationNumOfEachPatient() {
        queryService.getNameAndNumberOfMedicationsOfEachPatient()
                .peek(patient -> patient.forEach(System.out::println))
                .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
