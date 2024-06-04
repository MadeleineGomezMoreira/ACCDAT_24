package ui.methods.aggregations;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import services.impl.AggregationServiceImpl;

import java.util.Scanner;

public class MainAggregationB { //TODO: FIX IT RETURNS THE MEDICAL RECORDS BUT WITH NULL FIELDS????

    public static void main(String[] args) {

        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        System.out.println("These are the medical records of a given patient: (Cesar Millan)");

        String patientName = "Thomas Brim";

        AggregationServiceImpl service = container.select(AggregationServiceImpl.class).get();

        System.out.println(service.getB(patientName));
    }
}
