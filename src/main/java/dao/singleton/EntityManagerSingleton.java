package dao.singleton;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerSingleton {

    private static final String persistenceUnit = "crudTest";
    private static EntityManagerFactory instance;

    private EntityManagerSingleton() {}

    public static synchronized EntityManagerFactory getInstance() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory(persistenceUnit);
        }
        return instance;
    }

    public static void close() {
        if (instance != null && instance.isOpen()) {
            instance.close();
        }
    }
}
