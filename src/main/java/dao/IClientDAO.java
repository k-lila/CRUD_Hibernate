package dao;

import dao.generic.IGenericDAO;
import domain.Client;
import exceptions.DAOException;

public interface IClientDAO extends IGenericDAO<Client, Long> {
    public Client searchByCPF(String cpf) throws DAOException;
}
