package common;

public class Constants {

    //FILE PATHS
    public static final String SETTINGS_FILE_PATH = "src/main/resources/config/settings.xml";

    //ERROR MESSAGES
    public static final String NO_CREDENTIAL_ASSOCIATED_TO_USERNAME_ERROR = "There is no credential associated to the provided username.";
    public static final String DOCTOR_OR_PATIENT_DO_NOT_EXIST_ERROR = "The patient or doctor do not exist";
    public static final String FILE_DOES_NOT_EXIST_ERROR = "The data file does not exist";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND = "The data was not found";
    public static final String DATA_RETRIEVAL_ERROR_NO_DATA = "There is no data available";
    public static final String DATA_IS_ALREADY_STORED_IN_XML_FILE_ERROR = "The data was already stored in the xml file";
    public static final String INTERNAL_ERROR = "There was an internal error";
    public static final String PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR = "The patient has medication associated to their medical records and cannot be deleted";
    public static final String NO_PATIENTS_WITH_MEDICATION_ERROR = "No patients were prescribed the specified medication";
    public static final String NO_MEDICATION_PRESCRIBED_TO_PATIENT_ERROR = "This patient was not prescribed any medication";
    public static final String ADMIN_USER = "root";
    public static final String ADMIN_PASS = "2dam";
    public static final String MEDICATION_INSERTION_DB_ERROR = "There was an error while inserting the prescribed medication into the database";
    public static final String PATH_XML = "pathXML";
    public static final String BLANK_SPACE = " ";
    public static final String ERROR_WRITING_XML = "There was an error while writing the xml file.";
    public static final String ERROR_SAVING_OLD_RECORDS = "Error saving the XML file. Old Medical Records were not deleted.";
    public static final String NO_OLD_RECORDS_FOUND = "There are no records older than 1 year. Nothing was deleted.";
    public static final String REQUESTED_MEDICATION_NOT_FOUND_ERROR = "The requested medication was not found in the database";
    public static final String NO_MEDICAL_RECORDS_FOUND_FOR_PATIENT_ERROR = "No medical records were found for the requested patient or the patient does not exist";

    private Constants() {
    }
}
