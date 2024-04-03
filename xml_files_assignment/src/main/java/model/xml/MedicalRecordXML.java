package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class MedicalRecordXML {

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlElement(name = "admission_date")
    private LocalDate admissionDate;
    private String diagnosis;
    private String doctor;

    @XmlElement(name = "medication")
    private PrescribedMedicationsXML prescribedMedication;

}
