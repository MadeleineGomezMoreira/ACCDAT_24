package dao.common;

public class HqlQueries {
    private HqlQueries() {
    }

    //PATIENTS CRUD
    public static final String GET_ALL_PATIENTS_HQL = "from Patient";
    public static final String GET_PATIENT_BY_ID_HQL = "from Patient where id = :id";
    public static final String DELETE_PATIENT_BY_ID_HQL = "delete from Patient where id = :id";
    public static final String UPDATE_PATIENT_HQL = "update Patient set name = :name, birthDate = :birthDate, phone = :phone where id = :id";
    public static final String INSERT_PATIENT = "insert into Patient (id, name, birthDate, phone) select (:id, :name, :birthDate, :phone)";

    //CREDENTIALS
    public static final String DELETE_CREDENTIAL_BY_PATIENT_ID_HQL = "delete from Credential where patientId = :id";
    public static final String GET_CREDENTIAL_BY_USERNAME_HQL = "from Credential where username = :username";

    //APPOINTMENTS
    public static final String DELETE_APPOINTMENTS_BY_PATIENT_ID_HQL = "delete from Appointment where patientId = :id";

    //PAYMENTS
    public static final String DELETE_PAYMENTS_BY_PATIENT_ID_HQL = "delete from Payment where patientId = :id";
    public static final String GET_ALL_PAYMENTS_HQL = "from Payment";

    //PRESCRIBED MEDICATION
    public static final String GET_ALL_PRESCRIBED_MEDICATION_HQL = "from PrescribedMedication";
    public static final String DELETE_PRESCRIBED_MEDICATION_BY_PATIENT_ID_HQL = "delete from PrescribedMedication where medicalRecordId in (select m.id from MedicalRecord m where m.patientId = :id)";
    public static final String DELETE_ALL_PRESCRIBED_MEDICATION_FROM_MEDICAL_RECORDS_OLDER_THAN_2024_HQL = "delete from PrescribedMedication where medicalRecordId in (select m.id from MedicalRecord m where m.admissionDate < :date)";

    //MEDICAL RECORDS

    public static final String GET_ALL_MEDICAL_RECORDS_HQL = "from MedicalRecord";
    public static final String DELETE_MEDICAL_RECORDS_BY_PATIENT_ID_HQL = "delete from MedicalRecord where patientId = :id";
    public static final String GET_ALL_MEDICAL_RECORDS_BY_PATIENT_ID_HQL = "from MedicalRecord where patientId = :id";
    public static final String DELETE_ALL_MEDICAL_RECORDS_OLDER_THAN_2024_HQL = "delete from MedicalRecord where admissionDate < :date";

    //EXTRA QUERIES
    public static final String GET_PATIENT_ID_WITH_MOST_MEDICAL_RECORDS_HQL = "select patientId from MedicalRecord group by patientId order by count(patientId) desc limit 1";
    public static final String GET_ADMISSION_DATE_WITH_MOST_PATIENTS_HQL = "select admissionDate from MedicalRecord group by admissionDate order by count(admissionDate) desc limit 1";
    public static final String GET_THE_NAME_AND_NUMBER_OF_PRESCRIBED_MEDICINES_OF_EACH_PATIENT_HQL = "select p.name, count(pm.id) from MedicalRecord mr join Patient p on mr.patientId = p.id join PrescribedMedication pm on pm.medicalRecordId = mr.id group by p.id, p.name";
    public static final String HQL_GET_TOTAL_AMOUNT_PAID_BY_EACH_PATIENT = "select p.patientId, sum(p.quantity) from Payment p group by p.patientId order by sum(p.quantity) desc";


}
