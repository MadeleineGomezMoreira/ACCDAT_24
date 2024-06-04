package ui.methods.aggregations;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import services.impl.AggregationServiceImpl;

public class MainAggregationJ { //IT WORKS!

    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        System.out.println("The name of the doctor with more patients is the following: ");

        AggregationServiceImpl service = container.select(AggregationServiceImpl.class).get();

        System.out.println(service.getJ());
    }
}
