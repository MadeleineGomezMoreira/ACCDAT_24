package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoPrescribedMedication;
import dao.hibernate.common.HqlQueries;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.hibernate.PrescribedMedicationEntity;
import model.error.AppError;

import java.util.List;

public class DaoPrescribedMedicationImpl implements DaoPrescribedMedication {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoPrescribedMedicationImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, List<PrescribedMedicationEntity>> getAll() {
        Either<AppError, List<PrescribedMedicationEntity>> result;

        em = jpaUtil.getEntityManager();
        try {
            List<PrescribedMedicationEntity> records = em.createQuery(HqlQueries.GET_ALL_PRESCRIBED_MEDICATION_HQL, PrescribedMedicationEntity.class).getResultList();
            result = Either.right(records);
        } catch (Exception e) {
            result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND + e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(PrescribedMedicationEntity prescribedMedicationEntity) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {

            tx.begin();

            em.persist(prescribedMedicationEntity);
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
