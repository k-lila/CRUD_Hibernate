package services;

import dao.IClientDAO;
import domain.Client;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.generic.GenericService;

public class ClientService extends GenericService<Client, Long> implements IClientService {
    private final IClientDAO clientDAO;

    public ClientService(IClientDAO iClientDAO) {
        super(iClientDAO);
        this.clientDAO = iClientDAO;
    }

    @Override
    public Client searchByCPF(String cpf) throws DAOException, ServiceException{
        try {
            return clientDAO.searchByCPF(cpf);
        } catch(Exception e) {
            throw new ServiceException("Erro ao procurar pelo CPF", e);
        }
    }
}
