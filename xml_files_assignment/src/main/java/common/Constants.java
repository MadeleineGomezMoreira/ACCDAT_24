package common;

public class Constants {

    private Constants() {
    }

    //DATA FILES PATH PROPERTY NAMES
    public static final String PATH_PATIENTS = "pathPatients";
    public static final String PATH_DOCTORS = "pathDoctors";
    public static final String PATH_MEDICAL_RECORDS = "pathMedicalRecords";
    public static final String PATH_PRESCRIBED_MEDICATION = "pathPrescribedMedication";
    public static final String PATH_XML = "pathXML";

    //PROPERTIES FILE PATH
    public static final String CONFIG_FILE_PATH = "config/config.properties";
    public static final String CONFIG_FILE_PATH_XML = "config/config.xml";

    //YAML USER CONFIG STRINGS

    public static final String USERS_FILE_PATH = "config/users.yaml";
    public static final String USER_CONFIG = "username";
    public static final String PASSWORD_CONFIG = "password";

    //MODEL TXT CONFIG STRINGS
    public static final String SEMICOLON = ";";
    public static final String COMMA = ",";
    public static final String BLANK_SPACE = " ";
    public static final String EMPTY_STRING = "";

    //ERROR MESSAGES
    public static final String DOCTOR_OR_PATIENT_DO_NOT_EXIST_ERROR = "The patient or doctor do not exist";
    public static final String FILE_DOES_NOT_EXIST_ERROR = "The data file does not exist";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND = "The data was not found";
    public static final String DATA_RETRIEVAL_ERROR_NO_DATA = "There is no data available";
    public static final String DATA_IS_ALREADY_STORED_IN_XML_FILE_ERROR = "The data was already stored in the xml file";
    public static final String INTERNAL_ERROR = "There was an internal error";
    public static final String PATIENT_HAS_MEDICATION_ASSOCIATED_TO_MEDICAL_RECORDS_ERROR = "The patient has medication associated to their medical records and cannot be deleted";
    public static final String ERROR_READING_XML = "There was an error while reading the xml.";
    public static final String ERROR_WRITING_XML = "There was an error while writing the xml.";
}
