package dao.mongo;

import model.dto.MedicalRecordWithAppointments;
import model.mongo.MedicalRecord;
import model.mongo.PrescribedMedication;

import java.util.HashMap;
import java.util.List;

public interface DaoAggregations {
    //a. Get the name of the medication with the highest dosage
    String getA();

    //b. Get the medical records of a given patient
    List<MedicalRecordWithAppointments> getB();

    //c. Get the number of medications of each patient
    HashMap<String, Integer> getC();

    //d. Get the name of the patients prescribed with amoxicillin
    List<String> getD();

    //e. Get the average number of medications per patient (it means for every patient)
    HashMap<String, Integer> getE();

    //f. Get the name of the most prescribed medication
    String getF();

    // g. Show a list with the medications of a selected patient
    List<PrescribedMedication> getG(String patientName);

    //h. Get the name of the most prescribed medication per patient (include medication/patient name)
    HashMap<String,String> getH();

    //i. Get name of the doctors that don’t have any patient
    List<String> getI();

    //j. Get the name of the doctor with more patients
    String getJ();

    //k. Get the name of the patient with more medical records
    String getK();
}
