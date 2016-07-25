package com.improve.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {

    private static EntityManagerFactory entityManagerFactory;

    public static void buildEntityManagerFactory() {
        if (entityManagerFactory != null) {
            return;
        }
        entityManagerFactory = Persistence.createEntityManagerFactory("mysql-unit");
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void killEntityManagerFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

}
