package dao.hibernate.impl;

import common.Constants;
import dao.hibernate.DaoCredential;
import dao.hibernate.common.HqlQueries;
import dao.hibernate.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.error.AppError;
import model.hibernate.CredentialEntity;

public class DaoCredentialImpl implements DaoCredential {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoCredentialImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, CredentialEntity> get(CredentialEntity credentialEntity) {
        Either<AppError, CredentialEntity> result;
        em = jpaUtil.getEntityManager();

        try {
            CredentialEntity credentialEntityFound = em.createQuery(HqlQueries.GET_CREDENTIAL_BY_USERNAME_HQL, CredentialEntity.class).setParameter(Constants.USERNAME, credentialEntity.getUsername()).getSingleResult();
            if (credentialEntityFound == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(credentialEntityFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public Either<AppError, Integer> save(CredentialEntity credentialEntity) {
        Either<AppError, Integer> result;
        em = jpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(credentialEntity);
            em.getTransaction().commit();
            result = Either.right(1);
        } catch (Exception e) {
            em.getTransaction().rollback();
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
