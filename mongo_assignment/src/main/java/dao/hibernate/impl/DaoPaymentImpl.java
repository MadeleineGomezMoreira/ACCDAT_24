package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoPayment;
import dao.hibernate.common.HqlQueries;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.hibernate.PaymentEntity;
import model.error.AppError;

import java.util.List;

public class DaoPaymentImpl implements DaoPayment {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoPaymentImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, List<PaymentEntity>> getAll() {
        Either<AppError, List<PaymentEntity>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<PaymentEntity> paymentEntities = em.createQuery(HqlQueries.GET_ALL_PAYMENTS_HQL, PaymentEntity.class).getResultList();

            if (paymentEntities.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(paymentEntities);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
