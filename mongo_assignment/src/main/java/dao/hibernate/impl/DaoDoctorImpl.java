package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoDoctor;
import dao.hibernate.common.HqlQueries;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.hibernate.DoctorEntity;
import model.error.AppError;

import java.util.List;

public class DaoDoctorImpl implements DaoDoctor {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoDoctorImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //get all
    @Override
    public Either<AppError, List<DoctorEntity>> getAll() {
        Either<AppError, List<DoctorEntity>> result;
        em = jpaUtil.getEntityManager();

        try {
            List<DoctorEntity> doctorEntities = em.createQuery(HqlQueries.GET_ALL_DOCTORS_HQL, DoctorEntity.class).getResultList();
            if (doctorEntities.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(doctorEntities);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
