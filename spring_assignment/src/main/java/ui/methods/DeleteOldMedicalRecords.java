package ui.methods;

import jakarta.inject.Inject;
import services.MedicalRecordService;
import services.PatientService;

import java.util.Scanner;

public class DeleteOldMedicalRecords {

    private final MedicalRecordService service;

    @Inject
    public DeleteOldMedicalRecords(MedicalRecordService service) {
        this.service = service;
    }

    public void deleteOldMedicalRecords() {

        service.deleteOldAndSaveXML().peek(e -> System.out.println("Medical records older than 1 year from now were successfully deleted from the database and saved into an XML file.")).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
