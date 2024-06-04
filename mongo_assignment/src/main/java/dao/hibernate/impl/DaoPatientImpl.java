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
}
