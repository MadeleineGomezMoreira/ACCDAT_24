package ui.methods;

import jakarta.inject.Inject;
import services.PaymentService;

public class GetTotalAmountPaidByPatient {

    private final PaymentService paymentService;

    @Inject
    public GetTotalAmountPaidByPatient(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void getTotalAmountPaidByPatient() {
        paymentService.getTotalPaymentByPatient()
                .peek(payment -> payment.forEach(System.out::println))
                .peekLeft(error -> System.out.println("ERROR: " + error.getMessage()));
    }
}
