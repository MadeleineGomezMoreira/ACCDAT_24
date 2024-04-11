package ui.methods;

import jakarta.inject.Inject;
import services.AppointmentService;

public class ShowNameAndNumberOfPatientPrescriptions {

    private final AppointmentService service;

    @Inject
    public ShowNameAndNumberOfPatientPrescriptions(AppointmentService service) {
        this.service = service;
    }

    public void showMostSharedAppointmentDate() {
        service.getMostSharedDate().peek(e -> System.out.println("THE DATE IN WHICH MOST PATIENTS HAD APPOINTMENTS: " + e.toString())).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
