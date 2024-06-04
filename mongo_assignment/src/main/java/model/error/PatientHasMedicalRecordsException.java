package model.error;

public class PatientHasMedicalRecordsException extends RuntimeException {
    public PatientHasMedicalRecordsException(String message) {
        super(message);
    }
}
