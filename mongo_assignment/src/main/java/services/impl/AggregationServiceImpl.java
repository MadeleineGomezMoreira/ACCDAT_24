package services.impl;

import dao.mongo.DaoAggregations;
import jakarta.inject.Inject;
import model.dto.MedicalRecordWithAppointments;
import model.mongo.MedicalRecord;
import model.mongo.PrescribedMedication;

import java.util.HashMap;
import java.util.List;

public class AggregationServiceImpl {

    private final DaoAggregations daoAggregations;

    @Inject
    public AggregationServiceImpl(DaoAggregations daoAggregations) {
        this.daoAggregations = daoAggregations;
    }

    public String getA() {
        return daoAggregations.getA();
    }

    public List<MedicalRecordWithAppointments> getB(){
        return daoAggregations.getB();
    }

    public HashMap<String,Integer> getC(){
        return daoAggregations.getC();
    }

    public List<String> getD(){
        return daoAggregations.getD();
    }

    public HashMap<String,Integer> getE(){
        return daoAggregations.getE();
    }

    public String getF(){
        return daoAggregations.getF();
    }

    public List<PrescribedMedication> getG(String patientName){
        return daoAggregations.getG(patientName);
    }

    public HashMap<String,String> getH(){
        return daoAggregations.getH();
    }

    public List<String> getI(){
        return daoAggregations.getI();
    }

    public String getJ(){
        return daoAggregations.getJ();
    }

    public String getK(){
        return daoAggregations.getK();
    }


}
