package test.serviceTest;

import java.time.Instant;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.ClientDAO;
import dao.IClientDAO;
import domain.Client;
import exceptions.DAOException;
import exceptions.ServiceException;
import services.ClientService;
import services.IClientService;

public class ClientServiceTest {

    private IClientService clientService;

    public ClientServiceTest() {
        IClientDAO dao = new ClientDAO();
        clientService = new ClientService(dao);
    }

    public Client createClient(String cpf) {
        Client client = new Client();
        client.setName("teste");
        client.setCpf(cpf);
        client.setAddress("endereço");
        client.setNumber(1);
        client.setCity("cidade");
        client.setState("estado");
        client.setFone(9l);
        client.setCreationDate(Instant.now());
        return client;
    }

    @AfterEach
    public void cleanup() throws DAOException, ServiceException {
        for (Client client : clientService.showAll()) {
            clientService.remove(client.getId());
        }
    }

    @Test
    public void createTest() throws DAOException, ServiceException {
        Boolean add = clientService.register(createClient("service"));
        Assertions.assertTrue(add);
    }

    @Test
    public void readTest() throws Exception, ServiceException {
        Client client = createClient("service");
        clientService.register(client);
        Client read = clientService.search(client.getId());
        Assertions.assertNotNull(read);
        Client readByCode = clientService.searchByCPF("service");
        Assertions.assertNotNull(readByCode);
    }

    @Test void updateTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        clientService.register(client);
        Client read = clientService.search(client.getId());
        read.setName("Modificado");
        Boolean edited = clientService.edit(read);
        Assertions.assertTrue(edited);
    }

    @Test
    public void deleteTest() throws DAOException, ServiceException {
        Client client = createClient("service");
        Boolean add = clientService.register(client);
        Assertions.assertTrue(add);
        Boolean delete = clientService.remove(client.getId());
        Assertions.assertTrue(delete);
    }

    @Test
    public void showAllTest() throws DAOException, ServiceException {
        for (int i = 0; i < 5; i++) {
            clientService.register(createClient(String.valueOf(i)));
        }
        Collection<Client> all = clientService.showAll();
        Assertions.assertEquals(5, all.size());
    }
}
