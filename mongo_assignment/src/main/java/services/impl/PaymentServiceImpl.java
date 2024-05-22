package services.impl;

import dao.DaoPayment;
import dao.DaoQueries;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Payment;
import model.dto.TotalPaymentByPatientDTO;
import model.error.AppError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements services.PaymentService {

    private final DaoPayment daoPayment;
    private final DaoQueries daoQueries;

    @Inject
    public PaymentServiceImpl(DaoPayment daoPayment, DaoQueries daoQueries) {
        this.daoPayment = daoPayment;
        this.daoQueries = daoQueries;
    }

    @Override
    public Either<AppError, List<TotalPaymentByPatientDTO>> getTotalPaymentByPatient() {
        Either<AppError, List<TotalPaymentByPatientDTO>> result;

        Either<AppError, Map<Integer, Double>> payments = daoQueries.getQueryFour();

        if (payments.isLeft()) {
            result = Either.left(payments.getLeft());
        } else {
            Map<Integer, Double> paymentList = payments.get();
            List<TotalPaymentByPatientDTO> paymentsByPatientList = new ArrayList<>();

            for (Map.Entry<Integer, Double> entry : paymentList.entrySet()) {
                Integer patientId = entry.getKey();
                Double totalAmountPaid = entry.getValue();

                TotalPaymentByPatientDTO totalPaymentByPatientDTO = new TotalPaymentByPatientDTO(patientId, totalAmountPaid);
                paymentsByPatientList.add(totalPaymentByPatientDTO);
            }

            result = Either.right(paymentsByPatientList);
        }
        return result;
    }
}
