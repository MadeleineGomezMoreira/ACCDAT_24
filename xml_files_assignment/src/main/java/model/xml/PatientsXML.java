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
@XmlRootElement(name="patients")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientsXML {

    @XmlElement(name = "patient")
    private List<PatientXML> patients;
}
