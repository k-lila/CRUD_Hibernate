package Test.DAOTest;


import java.time.Instant;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dao.ClientDAO;
import dao.IClientDAO;
import domain.Client;
import exceptions.DAOException;

public class ClientDAOTest {

    private IClientDAO clientDAO;
    private Client clientTest;

    public ClientDAOTest() {
        this.clientDAO = new ClientDAO();
        this.clientTest = new Client();
        clientTest.setName("teste");
        clientTest.setCpf(1l);
        clientTest.setAddress("endereço");
        clientTest.setNumber(1);
        clientTest.setCity("cidade");
        clientTest.setState("estado");
        clientTest.setFone(9l);
        clientTest.setCreationDate(Instant.now());
    }

    @AfterEach
    public void cleanup() throws DAOException {
        for (Client client : clientDAO.showAll()) {
            clientDAO.delete(client.getId());
        }
    }

    @Test
    public void testCreate() throws DAOException {
        Boolean result = clientDAO.create(clientTest);
        Assertions.assertTrue(result);
        Assertions.assertNotNull(clientTest.getId());
    }

    @Test
    public void testRead() throws DAOException {
        Boolean result = clientDAO.create(clientTest);
        Assertions.assertTrue(result);
        Assertions.assertNotNull(clientTest.getId());
        Client client = clientDAO.read(clientTest.getId());
        Assertions.assertNotNull(client);
        Assertions.assertEquals(clientTest.getCpf(), client.getCpf());
    }

    @Test
    public void testUpdate() throws DAOException {
        clientDAO.create(clientTest);
        Long id = clientTest.getId();
        clientTest.setCity("update");
        Boolean result = clientDAO.update(clientTest);
        Assertions.assertTrue(result);
        Client updated = clientDAO.read(id);
        Assertions.assertEquals("update", updated.getCity());
    }

    @Test
    public void testDelete() throws DAOException {
        clientDAO.create(clientTest);
        Long id = clientTest.getId();
        Boolean deleted = clientDAO.delete(id);
        Assertions.assertTrue(deleted);
        Client result = clientDAO.read(id);
        Assertions.assertNull(result);
    }

    @Test
    public void testShowAll() throws DAOException {
        clientDAO.create(clientTest);
        Client another = new Client();
        another.setName("novo");
        another.setCpf(2L);
        another.setAddress("novo");
        another.setNumber(1);
        another.setCity("novo");
        another.setState("novo");
        another.setFone(999999L);
        another.setCreationDate(Instant.now());
        clientDAO.create(another);
        Collection<Client> allClients = clientDAO.showAll();
        Assertions.assertEquals(2, allClients.size());
    }
}

