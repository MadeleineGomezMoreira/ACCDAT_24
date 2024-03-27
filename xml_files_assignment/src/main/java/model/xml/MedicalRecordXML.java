package model.xml;

import jakarta.xml.bind.annotation.XmlElement;

import java.time.LocalDate;
import java.util.List;

public class MedicalRecordXML {

    @XmlElement(name = "admission_date")
    private LocalDate admissionDate;
    private String diagnosis;
    private String doctor;

    @XmlElement(name = "medication")
    private List<PrescribedMedicationXML> prescribedMedication;

}
