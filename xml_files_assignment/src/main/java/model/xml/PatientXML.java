package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientXML {

    private int id;
    private String name;
    @XmlElement(name = "birth_date")
    private LocalDate birthDate;
    private String phone;

    @XmlElement(name = "medical_record")
    private List<MedicalRecordXML> medicalRecords;

}
