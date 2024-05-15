package ui.methods;

import jakarta.inject.Inject;
import services.MedicalRecordService;

public class DeleteRecordsOlderThan2024 {

    private final MedicalRecordService medicalRecordService;

    @Inject
    public DeleteRecordsOlderThan2024(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void deleteRecordsOlderThan2024() {
        medicalRecordService.deleteOlderThan2024()
                .peek(i -> System.out.println("Older medical records deleted successfully!"))
                .peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }

}
