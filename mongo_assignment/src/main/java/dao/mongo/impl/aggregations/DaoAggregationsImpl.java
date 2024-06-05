package dao.mongo.impl.aggregations;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Field;
import jakarta.inject.Inject;
import model.dto.MedicalRecordWithAppointments;
import model.mongo.MedicalRecord;
import model.mongo.PrescribedMedication;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static java.util.Arrays.asList;

public class DaoAggregationsImpl implements dao.mongo.DaoAggregations {

    private final MongoCollection<Document> collectionPatients;
    private final MongoCollection<Document> collectionDoctors;
    private final Gson gson;

    @Inject
    public DaoAggregationsImpl(MongoConnection mongoConnection, Gson gson) {
        this.collectionPatients = mongoConnection.getPatients();
        this.collectionDoctors = mongoConnection.getDoctors();
        this.gson = gson;
    }


    //a. Get the name of the medication with the highest dosage
    @Override
    public String getA() {
        List<Document> list = collectionPatients.aggregate(
                asList(
                        unwind("$medical_records"),
                        unwind("$medical_records.prescribed_medication"),
                        project(fields(
                                exclude("_id"),
                                computed("name", "$medical_records.prescribed_medication.name"),
                                computed("dose", "$medical_records.prescribed_medication.dose")
                        )),
                        //as I added dosage as a string, I need to extract its numeric value
                        addFields(new Field<>("dosageNumeric", new Document("$regexFind", new Document("input", "$dose").append("regex", "\\d+")))),
                        //then I sort by the extracted numeric value
                        sort(descending("dosageNumeric.matchedText")),
                        limit(1)
                )
        ).into(new ArrayList<>());
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0).get("name", String.class);
        }
    }

    // Get the medical records of a given patient showing the name of the patient and the number of appointments
    @Override
    public List<MedicalRecordWithAppointments> getB() {
        // First, get the number of appointments for the patient
        List<Document> appointmentCountDocuments = collectionPatients.aggregate(
                List.of(
                        project(
                                fields(
                                        exclude("_id"),
                                        include("name"),
                                        computed("number_of_appointments",
                                                new Document("$size",
                                                        new Document("$ifNull", List.of("$medical_records", List.of())))
                                        )
                                )
                        )
                )
        ).into(new ArrayList<>());

        HashMap<String, Integer> appointmentCounts = new HashMap<>();
        for (Document document : appointmentCountDocuments) {
            appointmentCounts.put(document.getString("name"), document.getInteger("number_of_appointments"));
        }

        //then we get the medical records of the patient
        List<Document> mrDocuments = collectionPatients.aggregate(
                List.of(
                        unwind("$medical_records"),
                        project(fields(
                                include("medical_records"),
                                computed("name", "$name")
                        ))
                )
        ).into(new ArrayList<>());

        List<MedicalRecordWithAppointments> medicalRecords = new ArrayList<>();
        for (Document document : mrDocuments) {
            Document medicalRecord = document.get("medical_records", Document.class);
            if (medicalRecord != null) {
                MedicalRecord mr = gson.fromJson(medicalRecord.toJson(), MedicalRecord.class);
                String patientName = document.getString("name");
                Integer numAppointments = appointmentCounts.get(patientName);
                if (numAppointments == null) {
                    numAppointments = 0;
                }
                medicalRecords.add(MedicalRecordWithAppointments.builder()
                                .patientName(patientName)
                                .appointmentNumber(numAppointments)
                                .admissionDate(mr.getAdmissionDate())
                                .diagnosis(mr.getDiagnosis())
                                .doctorId(mr.getDoctorId())
                        .build());
            }
        }

        return medicalRecords;
    }

    //c. Get the number of medications of each patient
    @Override
    public HashMap<String, Integer> getC() {
        List<Document> documents = collectionPatients.aggregate(
                List.of(
                        unwind("$medical_records"),
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name"),
                                        computed("num_medications", new Document("$size", "$medical_records.prescribed_medication"))
                                )
                        )
                )
        ).into(new ArrayList<>());
        HashMap<String, Integer> map = new HashMap<>();
        for (Document document : documents) {
            map.put(document.getString("name"), document.getInteger("num_medications"));
        }
        return map;
}

    //d. Get the name of the patients prescribed with amoxicillin
    @Override
    public List<String> getD() {
        List<Document> documents = collectionPatients.aggregate(
                asList(
                        unwind("$medical_records"),
                        unwind("$medical_records.prescribed_medication"),
                        match(eq("medical_records.prescribed_medication.name", "amoxicillin")),
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name")
                                )
                        )
                )
        ).into(new ArrayList<>());
        List<String> names = new ArrayList<>();
        for (Document document : documents) {
            names.add(document.getString("name"));
        }
        return names;
    }

    //e. Get the average number of medications per patient (it means for every patient)
    @Override
    public HashMap<String, Integer> getE() {
        List<Document> documents = collectionPatients.aggregate(
                List.of(
                        unwind("$medical_records"),  // Unwind the medical_records array to handle nested arrays properly
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name"),
                                        computed("num_medications", new Document("$size", "$medical_records.prescribed_medication"))
                                )
                        ),
                        group("$name", avg("average_medications", "$num_medications")) // Group by patient name and calculate average medications
                )
        ).into(new ArrayList<>());

        HashMap<String, Integer> map = new HashMap<>();
        for (Document document : documents) {
            String name = document.getString("_id"); //for some reason the name is stored in _id
            Double averageMedications = document.getDouble("average_medications");
            if (averageMedications != null) {
                map.put(name, averageMedications.intValue());
            } else {
                map.put(name, 0);
            }
        }
        return map;
    }

    //f. Get the name of the most prescribed medication
    @Override
    public String getF() {
        List<Document> documents = collectionPatients.aggregate(
                asList(
                        unwind("$medical_records"),
                        unwind("$medical_records.prescribed_medication"),
                        group("$medical_records.prescribed_medication.name", sum("count", 1)),
                        sort(descending("count")),
                        limit(1)
                )
        ).into(new ArrayList<>());
        if (documents.isEmpty()) {
            return null;
        } else {
            return documents.get(0).getString("_id");
        }
    }

    // g. Show a list with the medications of a selected patient
    @Override
    public List<PrescribedMedication> getG(String patientName) {
        List<Document> documents = collectionPatients.aggregate(
                asList(
                        match(eq("name", patientName)),
                        unwind("$medical_records"),  // Unwind the medical_records array
                        unwind("$medical_records.prescribed_medication"),  // Unwind the prescribed_medication array
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name"),
                                        computed("medication", "$medical_records.prescribed_medication")
                                )
                        )
                )
        ).into(new ArrayList<>());

        if (documents.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<PrescribedMedication> medications = new ArrayList<>();
            for (Document document : documents) {
                Document med = document.get("medication", Document.class);
                if (med != null) {
                    medications.add(gson.fromJson(med.toJson(), PrescribedMedication.class));
                }
            }
            return medications;
        }
    }

    // h. Get the name of the most prescribed medication per patient (include medication/patient name)
    @Override
    public HashMap<String, String> getH() {
        List<Document> documents = collectionPatients.aggregate(Arrays.asList(
                unwind("$medical_records"),
                unwind("$medical_records.prescribed_medication"),
                group(new Document("name", "$name").append("medication", "$medical_records.prescribed_medication.name"), sum("count", 1)),
                sort(descending("count")),
                group("$_id.name", first("mostPopularMedication", "$_id.medication"))
        )).into(new ArrayList<>());

        HashMap<String, String> map = new HashMap<>();
        for (Document document : documents) {
            map.put(document.getString("_id"), document.getString("mostPopularMedication"));
        }
        return map;
    }

    //i. Get name of the doctors that don’t have any patient
    @Override
    public List<String> getI() {
        List<Document> documents = collectionDoctors.aggregate(
                asList(
                        lookup("patients", "_id", "doctor_id", "patients"),
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name"),
                                        computed("patients", "$patients")
                                )
                        ),
                        match(eq("patients", new ArrayList<>())),
                        project(
                                fields(
                                        exclude("patients")
                                )
                        )
                )
        ).into(new ArrayList<>());
        List<String> names = new ArrayList<>();
        for (Document document : documents) {
            names.add(document.getString("name"));
        }
        return names;
    }

    //j. Get the name of the doctor with more patients
    @Override
    public String getJ() {
        List<Document> documents = collectionDoctors.aggregate(
                asList(
                        lookup("patients", "_id", "doctor_id", "patients"),
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name"),
                                        computed("num_patients", new Document("$size", "$patients"))
                                )
                        ),
                        sort(descending("num_patients")),
                        limit(1)
                )
        ).into(new ArrayList<>());
        if (documents.isEmpty()) {
            return null;
        } else {
            return documents.get(0).getString("name");
        }
    }

    //k. Get the name of the patient with the most medical records
    @Override
    public String getK() {
        List<Document> documents = collectionPatients.aggregate(
                Arrays.asList(
                        project(
                                fields(
                                        exclude("_id"),
                                        computed("name", "$name"),
                                        computed("num_medical_records", new Document("$size", new Document("$ifNull", Arrays.asList("$medical_records", Arrays.asList()))))
                                )
                        ),
                        sort(descending("num_medical_records")),
                        limit(1)
                )
        ).into(new ArrayList<>());
        if (documents.isEmpty()) {
            return null;
        } else {
            return documents.get(0).getString("name");
        }
    }

}
