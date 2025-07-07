package dao;

import dao.generic.GenericDAO;
import domain.Client;

public class ClientDAO extends GenericDAO<Client, Long> implements IClientDAO {
    public ClientDAO() {
        super(Client.class);
    }
}
