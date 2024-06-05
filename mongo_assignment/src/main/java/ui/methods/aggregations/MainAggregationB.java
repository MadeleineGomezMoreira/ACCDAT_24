package ui.methods.aggregations;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import services.impl.AggregationServiceImpl;

import java.util.Scanner;

public class MainAggregationB { //IT WORKS!

    public static void main(String[] args) {

        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        System.out.println("These are the medical records of each patient (along with their name and number of appointments):");


        AggregationServiceImpl service = container.select(AggregationServiceImpl.class).get();

        System.out.println(service.getB());
    }
}
