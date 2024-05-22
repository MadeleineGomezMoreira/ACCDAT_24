package dao.impl;

import common.Constants;
import dao.common.HqlQueries;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.MedicalRecord;
import model.PrescribedMedication;
import model.error.AppError;

import java.util.ArrayList;
import java.util.List;

public class DaoMedicalRecordImpl implements dao.DaoMedicalRecord {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoMedicalRecordImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //get all medical records
    @Override
    public Either<AppError, List<MedicalRecord>> getAll() {
        Either<AppError, List<MedicalRecord>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<MedicalRecord> medicalRecords = em.createQuery(HqlQueries.GET_ALL_MEDICAL_RECORDS_HQL, MedicalRecord.class).getResultList();

            if (medicalRecords.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(medicalRecords);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //get all medical records by patient
    @Override
    public Either<AppError, List<MedicalRecord>> getAll(MedicalRecord medicalRecord) {
        Either<AppError, List<MedicalRecord>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<MedicalRecord> medicalRecords = em.createQuery(HqlQueries.GET_ALL_MEDICAL_RECORDS_BY_PATIENT_ID_HQL, MedicalRecord.class).setParameter(Constants.ID, medicalRecord.getPatientId()).getResultList();

            if (medicalRecords.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                medicalRecords.forEach(mr -> mr.getPrescribedMedication().size());
                result = Either.right(medicalRecords);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, MedicalRecord> get(MedicalRecord medicalRecord) {
        Either<AppError, MedicalRecord> result;
        em = jpaUtil.getEntityManager();

        try {
            MedicalRecord medicalRecordFound = em.find(MedicalRecord.class, medicalRecord.getId());
            if (medicalRecordFound == null) {
                medicalRecord.getPrescribedMedication().size();
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(medicalRecordFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(MedicalRecord medicalRecord) {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            MedicalRecord recordWithoutPrescriptions = new MedicalRecord(medicalRecord.getId(), medicalRecord.getAdmissionDate(), medicalRecord.getDiagnosis(), medicalRecord.getPatientId(), medicalRecord.getDoctorId());
            List<PrescribedMedication> prescribedMedications = medicalRecord.getPrescribedMedication();

            em.persist(recordWithoutPrescriptions);
            em.flush();

            int recordId = recordWithoutPrescriptions.getId();
            prescribedMedications.forEach(prescribedMedication -> {
                prescribedMedication.setMedicalRecordId(recordId);
                em.persist(prescribedMedication);
            });

            tx.commit();
            result = Either.right(medicalRecord.getId());
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            result = Either.left(new AppError(Constants.WRONG_PATIENT_OR_DOCTOR_ID_ERROR));
        } finally {
            em.close();
        }
        return result;
    }

    //delete all medical records older than 2024
    @Override
    public Either<AppError, Integer> delete() {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Query query = em.createQuery(HqlQueries.DELETE_ALL_PRESCRIBED_MEDICATION_FROM_MEDICAL_RECORDS_OLDER_THAN_2024_HQL);
            query.setParameter(Constants.DATE, Constants.DATE_2024);
            query.executeUpdate();

            query = em.createQuery(HqlQueries.DELETE_ALL_MEDICAL_RECORDS_OLDER_THAN_2024_HQL);
            query.setParameter(Constants.DATE, Constants.DATE_2024);
            int rowsUpdated = query.executeUpdate();
            tx.commit();
            result = Either.right(rowsUpdated);
        } catch (Exception e) {
            assert tx != null;
            if (tx.isActive()) tx.rollback();
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
