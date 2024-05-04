package dao.common;

public class QueryStrings {


    public static final String GET_ALL_PATIENTS = "SELECT * FROM patients";
    public static final String GET_DOCTOR_BY_ID = "SELECT * FROM doctors WHERE id = ?";
    public static final String GET_ALL_PATIENTS_BY_PRESCRIBED_MEDICATION_NAME = "SELECT DISTINCT p.* FROM patients p JOIN medical_records mr ON p.id = mr.id_patient JOIN prescribed_medication pm ON mr.id = pm.id_medical_record WHERE pm.name = ?";
    public static final String GET_ALL_PAYMENTS_BY_PATIENT_ID = "SELECT * FROM payments where id_patient = ?";
    public static final String GET_CREDENTIAL_BY_USERNAME = "SELECT * FROM credentials WHERE username = ?";
    public static final String GET_ALL_MEDICAL_RECORDS_BY_PATIENT_ID = "SELECT * FROM medical_records WHERE id_patient = ?";
    public static final String GET_ALL_OLD_PRESCRIBED_MEDICATIONS = "SELECT * FROM prescribed_medication WHERE id_medical_record IN(SELECT mr.id FROM medical_records mr WHERE mr.admission_date < DATE_SUB(NOW(), INTERVAL 1 YEAR))";
    public static final String DELETE_CREDENTIAL_BY_PATIENT_ID = "DELETE FROM credentials WHERE id_patient = ?";
    public static final String DELETE_PAYMENTS_BY_PATIENT_ID = "DELETE FROM payments WHERE id_patient = ?";
    public static final String DELETE_APPOINTMENTS_BY_PATIENT_ID = "DELETE FROM appointments WHERE id_patient = ?";
    public static final String DELETE_PATIENT_BY_ID = "DELETE FROM patients WHERE id = ?";
    public static final String GET_PATIENT_BY_ID = "SELECT * FROM patients WHERE id = ?";
    public static final String DELETE_MEDICAL_RECORDS_BY_PATIENT_ID = "DELETE FROM medical_records WHERE id_patient = ?";
    public static final String SELECT_PRESCRIBED_MEDICATION_BY_PATIENT_ID = "SELECT * FROM prescribed_medication pm JOIN medical_records mr ON pm.id_medical_record = mr.id WHERE mr.id_patient = ?";
    public static final String DELETE_PRESCRIBED_MEDICATION_BY_PATIENT_ID = "DELETE FROM prescribed_medication WHERE id_medical_record IN(SELECT mr.id FROM medical_records mr WHERE mr.id_patient = ?)";
    //old medical records = older than 1 year (https://www.w3schools.com/mysql/func_mysql_date_sub.asp)
    public static final String DELETE_PRESCRIBED_MEDICATION_FROM_OLD_MEDICAL_RECORDS = "DELETE FROM prescribed_medication WHERE id_medical_record IN(SELECT mr.id FROM medical_records mr WHERE mr.admission_date < DATE_SUB(NOW(), INTERVAL 1 YEAR))";
    public static final String DELETE_OLD_MEDICAL_RECORDS = "DELETE FROM medical_records WHERE admission_date < DATE_SUB(NOW(), INTERVAL 1 YEAR)";
    public static final String INSERT_MEDICAL_RECORD = "INSERT INTO medical_records (admission_date, diagnosis, id_patient, id_doctor) VALUES (?, ?, ?, ?)";
    public static final String INSERT_PRESCRIBED_MEDICATION = "INSERT INTO prescribed_medication (name, dose, id_medical_record) VALUES (?, ?, ?)";
    public static final String GET_PATIENT_WITH_THE_HIGHEST_NUMBER_OF_RECORDS = "SELECT p.* FROM patients p JOIN medical_records mr ON mr.id_patient = p.id GROUP BY mr.id_patient ORDER BY COUNT(mr.id_patient) LIMIT 1";
    public static final String GET_APPOINTMENT_DATE_SHARED_BY_MOST_PATIENTS = "SELECT a.appointment_date FROM appointments a JOIN patients p ON a.id_patient = p.id GROUP BY a.appointment_date ORDER BY COUNT(a.appointment_date) DESC LIMIT 1";
    public static final String GET_PATIENT_BY_USERNAME = "SELECT * FROM patients p JOIN credentials c ON p.id = c.id_patient WHERE c.username = ?";
    public static final String GET_ALL_OLD_MEDICAL_RECORDS = "SELECT * FROM medical_records WHERE admission_date < DATE_SUB(NOW(), INTERVAL 1 YEAR)";
    public static final String GET_ALL_MEDICATION = "SELECT * FROM prescribed_medication";
    public static final String GET_NEWEST_MEDICAL_RECORD_BY_PATIENT_ID = "SELECT * FROM medical_records WHERE id_patient = ? ORDER BY admission_date DESC LIMIT 1";
    public static final String UPDATE_PRESCRIBED_MEDICATION_DOSE_BY_ID = "UPDATE prescribed_medication SET dose = ? WHERE id = ?";
    public static final String GET_ALL_MEDICAL_RECORDS = "SELECT * FROM medical_records";
    public static final String GET_NAMES_OF_PATIENTS_MEDICATED_WITH_400_MG_IBUPROFEN = "SELECT DISTINCT p.name FROM patients p JOIN medical_records mr ON p.id = mr.id_patient JOIN prescribed_medication pm ON mr.id = pm.id_medical_record WHERE pm.name = 'Ibuprofen' AND pm.dose = '400mg'";
    public static final String GET_NAME_AND_TOTAL_PRESCRIBED_MEDICATIONS_OF_PATIENT = "SELECT p.name, COUNT(pm.id) as medication_amount FROM patients p JOIN medical_records mr ON p.id = mr.id_patient JOIN prescribed_medication pm ON mr.id = pm.id_medical_record GROUP BY p.id";

    private QueryStrings() {
    }
}
