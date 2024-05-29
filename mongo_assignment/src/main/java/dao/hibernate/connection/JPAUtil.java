package dao.hibernate.connection;

import common.Constants;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Singleton
public class JPAUtil {

    private EntityManagerFactory emf;

    public JPAUtil() {
        emf = getEmf();
    }

    private EntityManagerFactory getEmf() {
        return Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
