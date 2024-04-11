package model.xml;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    public LocalDate unmarshal(String dateString) {
        return LocalDate.parse(dateString);
    }

    public String marshal(LocalDate date) {
        return date.toString();
    }
}
