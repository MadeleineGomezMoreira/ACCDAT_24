package dao.impl;

import common.Constants;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.Doctor;
import model.error.AppError;

public class DaoDoctorImpl implements dao.DaoDoctor {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoDoctorImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, Doctor> get(Doctor doctor) {
        Either<AppError, Doctor> result;
        em = jpaUtil.getEntityManager();

        try {
            Doctor doctorFound = em.find(Doctor.class, doctor.getId());
            if (doctorFound == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(doctorFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
