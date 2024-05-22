package dao.impl;

import common.Constants;
import dao.DaoPatient;
import dao.common.HqlQueries;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Credential;
import model.Patient;
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
    public Either<AppError, List<Patient>> getAll() {
        Either<AppError, List<Patient>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<Patient> patients = em.createQuery(HqlQueries.GET_ALL_PATIENTS_HQL, Patient.class).getResultList();

            if (patients.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(patients);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Patient> get(Patient patient) {
        Either<AppError, Patient> result;
        em = jpaUtil.getEntityManager();

        try {
            Patient patientFound = em.find(Patient.class, patient.getId());
            if (patientFound == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(patientFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(Patient patient) {
        Either<AppError, Integer> result;

        //we will save both the patient and its credential here :)

        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(patient);
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
    public Either<AppError, Integer> update(Patient patient) {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            //we will check if the patient exists first (cause if not, 'merge' will create it)
            Patient existingPatient = em.find(Patient.class, patient.getId());
            if (existingPatient == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                em.merge(patient);
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
    public Either<AppError, Integer> delete(Patient patient, Boolean confirmation) {
        Either<AppError, Integer> result;

        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            int patientId = patient.getId();
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
            //delete patient
            em.createQuery(HqlQueries.DELETE_PATIENT_BY_ID_HQL).setParameter(Constants.ID, patientId).executeUpdate();
            tx.commit();

            result = Either.right(1);
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            if (e instanceof ConstraintViolationException) {
                result = Either.left(new AppError(Constants.PATIENT_STILL_HAS_APPOINTMENTS_ERROR));
            } else {
                result = Either.left(new AppError(Constants.PATIENT_DELETION_ERROR + e.getMessage()));
            }
        } finally {
            em.close();
        }
        return result;
    }
}
