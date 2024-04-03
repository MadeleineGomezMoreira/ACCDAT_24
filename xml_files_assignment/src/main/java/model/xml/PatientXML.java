package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientXML {

    private int id;
    private String name;
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlElement(name = "birth_date")
    private LocalDate birthDate;
    private String phone;

    @XmlElement(name = "medical_records")
    private MedicalRecordsXML medicalRecords;

}
