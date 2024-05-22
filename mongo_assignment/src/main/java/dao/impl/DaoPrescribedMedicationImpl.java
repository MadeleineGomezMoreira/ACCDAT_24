package dao.impl;

import common.Constants;
import dao.common.HqlQueries;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.List;

import static common.Constants.WRONG_PATIENT_ID_ERROR;

public class DaoPrescribedMedicationImpl implements dao.DaoPrescribedMedication {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoPrescribedMedicationImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, List<PrescribedMedication>> getAll() {
        Either<AppError, List<PrescribedMedication>> result;

        em = jpaUtil.getEntityManager();
        try {
            List<PrescribedMedication> records = em.createQuery(HqlQueries.GET_ALL_PRESCRIBED_MEDICATION_HQL, PrescribedMedication.class).getResultList();
            result = Either.right(records);
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(PrescribedMedication prescribedMedication) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {

            tx.begin();

            em.persist(prescribedMedication);
            tx.commit();

            result = Either.right(1);
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            result = Either.left(new AppError(Constants.PRESCRIBED_MEDICATION_INSERTION_ERROR + Constants.WRONG_MEDICAL_RECORD_ID_ERROR + e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }


}
