package dao.impl;

import common.Constants;
import dao.common.HqlQueries;
import dao.connection.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.Credential;
import model.error.AppError;

public class DaoCredentialImpl implements dao.DaoCredential {

    private final JPAUtil jpaUtil;
    private EntityManager em;

    @Inject
    public DaoCredentialImpl(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public Either<AppError, Credential> get(Credential credential) {
        Either<AppError, Credential> result;
        em = jpaUtil.getEntityManager();

        try {
            Credential credentialFound = em.createQuery(HqlQueries.GET_CREDENTIAL_BY_USERNAME_HQL, Credential.class).setParameter(Constants.USERNAME, credential.getUsername()).getSingleResult();
            if (credentialFound == null) {
                result = Either.left(new AppError(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND_INCORRECT_ID));
            } else {
                result = Either.right(credentialFound);
            }
        } catch (Exception e) {
            result = Either.left(new AppError(e.getMessage()));
        } finally {
            em.close();
        }
        return result;
    }
}
