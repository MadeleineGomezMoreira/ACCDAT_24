package dao.hibernate.common;

public class HqlQueries {
    private HqlQueries() {
    }

    //PATIENTS
    public static final String GET_ALL_PATIENTS_HQL = "from PatientEntity";

    //CREDENTIALS
    public static final String GET_CREDENTIAL_BY_USERNAME_HQL = "from CredentialEntity where username = :username";

    //DOCTORS
    public static final String GET_ALL_DOCTORS_HQL = "from DoctorEntity";
}
