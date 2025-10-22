package dao;

import dao.generic.GenericDAO;
import domain.Client;
import exceptions.DAOException;
import jakarta.persistence.EntityManagerFactory;

public class ClientDAO extends GenericDAO<Client, Long> implements IClientDAO {
    public ClientDAO(EntityManagerFactory entityManagerFactory) {
        super(Client.class, entityManagerFactory);
    }

    @Override
    public Client searchByCPF(String cpf) throws DAOException {
        try {
            openConnection();
            String jpql = "SELECT c FROM Client c WHERE c.cpf = :cpf";
            Client client = entityManager
                .createQuery(jpql, Client.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
            entityManager.getTransaction().commit();
            return client;
        } catch (Exception e) {
            throw new DAOException("ERRO AO BUSCAR CLIENTE POR CPF", e);
        } finally {
            closeConnection();
        }
    }
}
