package services;

import domain.Client;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.IGenericService;

public interface IClientService extends IGenericService<Client, Long> {
    public Client searchByCPF(String cpf) throws DAOException, ServiceException;
}
