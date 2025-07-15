package dao;

import dao.generic.GenericDAO;
import domain.Client;
import exceptions.DAOException;

public class ClientDAO extends GenericDAO<Client, Long> implements IClientDAO {
    public ClientDAO(String persistenceUnit) {
        super(Client.class, persistenceUnit);
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
