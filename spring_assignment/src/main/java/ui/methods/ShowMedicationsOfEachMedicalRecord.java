package ui.methods;

import jakarta.inject.Inject;
import services.MedicalRecordService;

public class ShowMedicationsOfEachMedicalRecord {

    private final MedicalRecordService service;

    @Inject
    public ShowMedicationsOfEachMedicalRecord(MedicalRecordService service) {
        this.service = service;
    }

    public void showMedicationsOfEachMedicalRecord() {
        service.showRecordsWithMedications().peek(System.out::println).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }

}
