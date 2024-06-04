package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoMedicalRecord;
import dao.hibernate.common.HqlQueries;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.hibernate.MedicalRecordEntity;
import model.hibernate.PrescribedMedicationEntity;
import model.error.AppError;

import java.util.List;

public class DaoMedicalRecordImpl implements DaoMedicalRecord {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoMedicalRecordImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //get all medical records
    @Override
    public Either<AppError, List<MedicalRecordEntity>> getAll() {
        Either<AppError, List<MedicalRecordEntity>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<MedicalRecordEntity> medicalRecordEntities = em.createQuery(HqlQueries.GET_ALL_MEDICAL_RECORDS_HQL, MedicalRecordEntity.class).getResultList();

            if (medicalRecordEntities.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(medicalRecordEntities);
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
    public Either<AppError, List<MedicalRecordEntity>> getAll(MedicalRecordEntity medicalRecordEntity) {
        Either<AppError, List<MedicalRecordEntity>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<MedicalRecordEntity> medicalRecordEntities = em.createQuery(HqlQueries.GET_ALL_MEDICAL_RECORDS_BY_PATIENT_ID_HQL, MedicalRecordEntity.class).setParameter(Constants.ID, medicalRecordEntity.getPatientId()).getResultList();

            if (medicalRecordEntities.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                medicalRecordEntities.forEach(mr -> mr.getPrescribedMedication().size());
                result = Either.right(medicalRecordEntities);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, MedicalRecordEntity> get(MedicalRecordEntity medicalRecordEntity) {
        Either<AppError, MedicalRecordEntity> result;
        em = jpaUtil.getEntityManager();

        try {
            MedicalRecordEntity medicalRecordEntityFound = em.find(MedicalRecordEntity.class, medicalRecordEntity.getId());
            if (medicalRecordEntityFound == null) {
                medicalRecordEntity.getPrescribedMedication().size();
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(medicalRecordEntityFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(MedicalRecordEntity medicalRecordEntity) {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            MedicalRecordEntity recordWithoutPrescriptions = new MedicalRecordEntity(medicalRecordEntity.getId(), medicalRecordEntity.getAdmissionDate(), medicalRecordEntity.getDiagnosis(), medicalRecordEntity.getPatientId(), medicalRecordEntity.getDoctorId());
            List<PrescribedMedicationEntity> prescribedMedicationEntities = medicalRecordEntity.getPrescribedMedication();

            em.persist(recordWithoutPrescriptions);
            em.flush();

            int recordId = recordWithoutPrescriptions.getId();
            prescribedMedicationEntities.forEach(prescribedMedication -> {
                prescribedMedication.setMedicalRecordId(recordId);
                em.persist(prescribedMedication);
            });

            tx.commit();
            result = Either.right(medicalRecordEntity.getId());
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
