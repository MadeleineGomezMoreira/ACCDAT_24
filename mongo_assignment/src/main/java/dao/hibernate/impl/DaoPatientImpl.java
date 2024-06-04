package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoPatient;
import dao.hibernate.common.HqlQueries;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.hibernate.PatientEntity;
import model.error.AppError;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class DaoPatientImpl implements DaoPatient {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoPatientImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, List<PatientEntity>> getAll() {
        Either<AppError, List<PatientEntity>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<PatientEntity> patientEntities = em.createQuery(HqlQueries.GET_ALL_PATIENTS_HQL, PatientEntity.class).getResultList();

            if (patientEntities.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(patientEntities);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, PatientEntity> get(PatientEntity patientEntity) {
        Either<AppError, PatientEntity> result;
        em = jpaUtil.getEntityManager();

        try {
            PatientEntity patientEntityFound = em.find(PatientEntity.class, patientEntity.getId());
            if (patientEntityFound == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(patientEntityFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(PatientEntity patientEntity) {
        Either<AppError, Integer> result;

        //we will save both the patientEntity and its credential here :)

        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(patientEntity);
            tx.commit();
            result = Either.right(1);
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            result = Either.left(new AppError(Constants.PATIENT_INSERTION_ERROR + e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> update(PatientEntity patientEntity) {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            //we will check if the patientEntity exists first (cause if not, 'merge' will create it)
            PatientEntity existingPatientEntity = em.find(PatientEntity.class, patientEntity.getId());
            if (existingPatientEntity == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                em.merge(patientEntity);
                tx.commit();
                result = Either.right(1);
            }
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            result = Either.left(new AppError(Constants.WRONG_PATIENT_ID_ERROR + e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> delete(PatientEntity patientEntity, Boolean confirmation) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            int patientId = patientEntity.getId();
            tx.begin();

            //if unconfirmed, try to delete all but appointments
            //if confirmed, delete all
            if (Boolean.TRUE.equals(confirmation)) {
                //delete all appointments
                em.createQuery(HqlQueries.DELETE_APPOINTMENTS_BY_PATIENT_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            }

            //delete payments
            em.createQuery(HqlQueries.DELETE_PAYMENTS_BY_PATIENT_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            //delete medical records along with prescribed medication
            em.createQuery(HqlQueries.DELETE_PRESCRIBED_MEDICATION_BY_PATIENT_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            //delete prescribed medication
            em.createQuery(HqlQueries.DELETE_MEDICAL_RECORDS_BY_PATIENT_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            //delete credential
            em.createQuery(HqlQueries.DELETE_CREDENTIAL_BY_PATIENT_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            //delete patientEntity
            em.createQuery(HqlQueries.DELETE_PATIENT_BY_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            tx.commit();

            result = Either.right(1);
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            if (e instanceof ConstraintViolationException) {
                result = Either.left(new AppError(Constants.PATIENT_STILL_HAS_MEDICAL_RECORDS_ERROR));
            } else {
                result = Either.left(new AppError(Constants.PATIENT_DELETION_ERROR + e.getMessage()));
            }
        } finally {
            em.close();
        }
        return result;
    }
}
