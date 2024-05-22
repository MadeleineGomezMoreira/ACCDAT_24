package common;

import java.time.LocalDate;

public class Constants {

    //FILE PATHS
    public static final String SETTINGS_FILE_PATH = "src/main/resources/config/settings.xml";

    //PERSISTENCE UNIT
    public static final String PERSISTENCE_UNIT_NAME = "hospitalPU";

    //DAO STRINGS
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String USERNAME = "username";

    //DATE 2024
    public static final LocalDate DATE_2024 = LocalDate.parse("2024-01-01");


    //ERROR MESSAGES
    public static final String NO_CREDENTIAL_ASSOCIATED_TO_USERNAME_ERROR = "There is no credential associated to the provided username.";
    public static final String DOCTOR_OR_PATIENT_DO_NOT_EXIST_ERROR = "The patient or doctor do not exist";
    public static final String FILE_DOES_NOT_EXIST_ERROR = "The data file does not exist";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND = "The data was not found";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID = "The data was not found. The provided ID is incorrect";
    public static final String DATA_RETRIEVAL_ERROR_NO_DATA = "There is no data available";
    public static final String DATA_IS_ALREADY_STORED_IN_XML_FILE_ERROR = "The data was already stored in the xml file";
    public static final String INTERNAL_ERROR = "There was an internal error";
    public static final String PATIENT_STILL_HAS_APPOINTMENTS_ERROR = "The patient still has appointments and thus cannot be deleted";
    public static final String APPOINTMENT_DELETION_QUESTION_ERROR = "The patient still has appointments, would you like to cancel them? (Y/N)";
    public static final String NO_PATIENTS_WITH_MEDICATION_ERROR = "No patients were prescribed the specified medication";
    public static final String NO_MEDICATION_PRESCRIBED_TO_PATIENT_ERROR = "This patient was not prescribed any medication";
    public static final String MEDICATION_INSERTION_DB_ERROR = "There was an error while inserting the prescribed medication into the database";
    public static final String ERROR_WRITING_XML = "There was an error while writing the xml file.";
    public static final String ERROR_SAVING_OLD_RECORDS = "Error saving the XML file. Old Medical Records were not deleted.";
    public static final String NO_OLD_RECORDS_FOUND = "There are no records older than 1 year. Nothing was deleted.";
    public static final String REQUESTED_MEDICATION_NOT_FOUND_ERROR = "The requested medication was not found in the database";
    public static final String NO_MEDICAL_RECORDS_FOUND_FOR_PATIENT_ERROR = "No medical records were found for the requested patient or the patient does not exist";
    public static final String PATIENT_INSERTION_ERROR = "There was an error while trying to insert the patient: ";
    public static final String PRESCRIBED_MEDICATION_INSERTION_ERROR = "There was an error while trying to insert prescribed medication into the medical record: ";
    public static final String PATIENT_DELETION_ERROR = "There was an error while trying to delete the patient: ";
    public static final String INVALID_NAME_INPUT_ERROR = "Please input a valid name for the patient.";
    public static final String INVALID_DATE_INPUT_ERROR = "Birth date is not valid. Please input a date before today.";
    public static final String INVALID_PHONE_INPUT_ERROR = "Phone number is not valid. Please include 9 digits only.";
    public static final String WRONG_PATIENT_OR_DOCTOR_ID_ERROR = "The patient or doctor ID is incorrect. Please try again.";
    public static final String WRONG_PATIENT_ID_ERROR = "The patient ID is incorrect. Please try again.";
    public static final String WRONG_MEDICAL_RECORD_ID_ERROR = "The medical record ID is incorrect. Please try again.";

    //UI STRINGS
    public static final String ERROR = "ERROR";

    private Constants() {
    }
}
