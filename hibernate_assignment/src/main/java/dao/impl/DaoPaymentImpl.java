package dao.impl;

import common.Constants;
import dao.common.HqlQueries;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.Payment;
import model.error.AppError;

import java.util.List;

public class DaoPaymentImpl implements dao.DaoPayment {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoPaymentImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, List<Payment>> getAll() {
        Either<AppError, List<Payment>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<Payment> payments = em.createQuery(HqlQueries.GET_ALL_PAYMENTS_HQL, Payment.class).getResultList();

            if (payments.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(payments);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
