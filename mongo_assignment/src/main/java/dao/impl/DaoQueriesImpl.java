package dao.impl;

import common.Constants;
import dao.DaoQueries;
import dao.common.HqlQueries;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.dto.PatientWithMedNumber;
import model.error.AppError;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoQueriesImpl implements DaoQueries {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoQueriesImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    //GET THE PATIENT ID WITH THE MOST MEDICAL RECORDS
    @Override
    public Either<AppError, Integer> getQueryOne() {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        try {
            Integer idResult = em.createQuery(HqlQueries.GET_PATIENT_ID_WITH_MOST_MEDICAL_RECORDS_HQL, Integer.class)
                    .setMaxResults(1)
                    .getSingleResult();
            if (idResult == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(idResult);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //GET THE DATE WHEN MOST PATIENTS WERE ADMITTED
    @Override
    public Either<AppError, LocalDate> getQueryTwo() {
        Either<AppError, LocalDate> result;
        em = jpaUtil.getEntityManager();
        try {
            LocalDate dateResult = em.createQuery(HqlQueries.GET_ADMISSION_DATE_WITH_MOST_PATIENTS_HQL, LocalDate.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (dateResult == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(dateResult);
            }

        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //GET THE NAME AND THE NUMBER OF MEDICATIONS OF EACH PATIENT
    @Override
    public Either<AppError, List<PatientWithMedNumber>> getQueryThree() {
        Either<AppError, List<PatientWithMedNumber>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<PatientWithMedNumber> patientResults =
                    em.createQuery(HqlQueries.GET_THE_NAME_AND_NUMBER_OF_PRESCRIBED_MEDICINES_OF_EACH_PATIENT_HQL, PatientWithMedNumber.class)
                            .getResultList();

            if (patientResults == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(patientResults);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    //GET THE TOTAL AMOUNT PAID BY EACH PATIENT
    @Override
    public Either<AppError, Map<Integer, Double>> getQueryFour() {
        Either<AppError, Map<Integer, Double>> result;
        em = jpaUtil.getEntityManager();
        try {
            List<Object[]> results =
                    em.createQuery(HqlQueries.HQL_GET_TOTAL_AMOUNT_PAID_BY_EACH_PATIENT, Object[].class)
                            .getResultList();

            Map<Integer, Double> totalAmountPaidByPatient = new HashMap<>();

            for (Object[] obj : results) {
                totalAmountPaidByPatient.put((Integer) obj[0], (Double) obj[1]);
            }

            if (totalAmountPaidByPatient.isEmpty()) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                result = Either.right(totalAmountPaidByPatient);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
