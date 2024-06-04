package ui.methods.aggregations;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import services.impl.AggregationServiceImpl;

public class MainAggregationC { //IT WORKS!

    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        System.out.println("The number of medications of each patient are the following: ");

        AggregationServiceImpl service = container.select(AggregationServiceImpl.class).get();

        System.out.println(service.getC().toString());
    }
}
