package ui.methods;

import jakarta.inject.Inject;
import services.QueriesService;

public class ShowPatientPrescriptionAmount {

    private final QueriesService service;

    @Inject
    public ShowPatientPrescriptionAmount(QueriesService service) {
        this.service = service;
    }

    public void showMostSharedAppointmentDate() {
        service.getQueryTwo().peek(e -> System.out.println("PATIENTS WITH THEIR TOTAL AMOUNT OF PRESCRIPTIONS: " + "\n" + e.toString())).peekLeft(e -> System.out.println("ERROR: " + e.getMessage()));
    }
}
