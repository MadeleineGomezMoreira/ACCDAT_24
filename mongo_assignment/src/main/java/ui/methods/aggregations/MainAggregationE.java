package ui.methods.aggregations;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import services.impl.AggregationServiceImpl;

public class MainAggregationE {

    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        System.out.println("The average number of medications per patient is the following: ");

        AggregationServiceImpl service = container.select(AggregationServiceImpl.class).get();

        System.out.println(service.getE().toString());
    }
}
