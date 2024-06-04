package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoDoctor;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.hibernate.DoctorEntity;
import model.error.AppError;

public class DaoDoctorImpl implements DaoDoctor {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoDoctorImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, DoctorEntity> get(DoctorEntity doctorEntity) {
        Either<AppError, DoctorEntity> result;
        em = jpaUtil.getEntityManager();

        try {
            DoctorEntity doctorEntityFound = em.find(DoctorEntity.class, doctorEntity.getId());
            if (doctorEntityFound == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(doctorEntityFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
