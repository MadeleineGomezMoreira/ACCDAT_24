package ui.methods;

import jakarta.inject.Inject;
import services.PrescribedMedicationService;

public class ShowRecordsWithMedication {

    private final PrescribedMedicationService prescribedMedicationService;

    @Inject
    public ShowRecordsWithMedication(PrescribedMedicationService prescribedMedicationService) {
        this.prescribedMedicationService = prescribedMedicationService;
    }

    public void showRecordsWithMedication() {
        prescribedMedicationService.getRecordsWithPrescription()
                .peek(records -> records.forEach(System.out::println))
                .peekLeft(error -> System.out.println("ERROR: " + error.getMessage()));
    }
}
