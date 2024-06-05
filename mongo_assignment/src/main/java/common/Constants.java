package common;

import java.time.LocalDate;

public class Constants {

    //FILE PATHS
    public static final String SETTINGS_FILE_PATH = "src/main/resources/config/settings.xml";

    //PERSISTENCE UNIT
    public static final String PERSISTENCE_UNIT_NAME = "hospitalPU";

    //DAO STRINGS
    public static final String OBJECT_ID = "_id";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String MEDICAL_RECORDS = "medical_records";

    //ERROR MESSAGES
    public static final String NO_CREDENTIAL_ASSOCIATED_TO_USERNAME_ERROR = "There is no credential associated to the provided username.";
    public static final String NO_DOCTORS_TREATED_THIS_PATIENT_ERROR = "No doctors treated this patient, they have no medical records.";
    public static final String PATIENT_HAS_NO_MEDICAL_RECORDS_ERROR = "The patient has no medical records";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND = "The data was not found";
    public static final String DATA_MIGRATION_ERROR = "There was an error while migrating the data. The data was not found in the MySQL database.";
    public static final String CANNOT_DELETE_MEDICATION_EMPTY_MEDICAL_RECORD = "The prescribed medication cannot be deleted from the medical record. There must be at least one prescribed medication in the medical record";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID = "The data was not found. The provided ID is incorrect";
    public static final String PATIENT_STILL_HAS_MEDICAL_RECORDS_ERROR = "The patient still has medical records and thus cannot be deleted";
    public static final String MEDICAL_RECORD_DELETION_QUESTION_ERROR = "The patient still has medical records, would you like to delete them? (Y/N)";
    public static final String PATIENT_INSERTION_ERROR = "There was an error while trying to insert the patient: ";
    public static final String DOCTOR_INSERTION_ERROR = "There was an error while trying to insert the doctor: ";
    public static final String PATIENT_UPDATE_ERROR = "There was an error while trying to update the patient: ";
    public static final String MEDICAL_RECORD_INSERTION_ERROR = "There was an error while trying to insert the medical record";
    public static final String PATIENT_DELETION_ERROR = "There was an error while trying to delete the patient: ";
    public static final String INVALID_NAME_INPUT_ERROR = "Please input a valid name for the patient.";
    public static final String INVALID_DATE_INPUT_ERROR = "Birth date is not valid. Please input a date before today.";
    public static final String INVALID_PHONE_INPUT_ERROR = "Phone number is not valid. Please include 9 digits only.";

    //UI STRINGS
    public static final String ERROR = "ERROR";

    //CONFIG STRINGS

    public static final String MONGO_CLIENT = "mongoClient";
    public static final String MONGO_DB = "mongoDB";
    public static final String DOCTORS_COLLECTION = "doctorsCol";
    public static final String PATIENTS_COLLECTION = "patientsCol";


    private Constants() {
    }
}
