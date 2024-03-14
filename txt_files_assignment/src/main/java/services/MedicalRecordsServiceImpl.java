package services;

import dao.DaoMedicalRecords;
import dao.DaoPrescribedMedication;
import jakarta.inject.Inject;

public class MedicalRecordsServiceImpl {

    private final DaoMedicalRecords daoMedicalRecords;
    private final DaoPrescribedMedication daoPrescribedMedication;

    @Inject
    public MedicalRecordsServiceImpl(DaoMedicalRecords daoMedicalRecords, DaoPrescribedMedication daoPrescribedMedication) {
        this.daoMedicalRecords = daoMedicalRecords;
        this.daoPrescribedMedication = daoPrescribedMedication;
    }


}
