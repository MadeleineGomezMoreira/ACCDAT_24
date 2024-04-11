package model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement(name = "medical_records")
@XmlAccessorType(XmlAccessType.FIELD)
public class MedicalRecordsXML {

    @XmlElement(name = "medical_record")
    private List<MedicalRecordXML> medicalRecords;

}
