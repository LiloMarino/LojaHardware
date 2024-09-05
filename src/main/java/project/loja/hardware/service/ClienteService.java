package project.loja.hardware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.loja.hardware.model.Cliente;
import project.loja.hardware.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Método para cadastrar um novo cliente
    public void cadastrarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    // Método para obter um cliente pelo seu ID
    public Cliente getClienteById(int id) {
        return clienteRepository.findById(id);
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Método para atualizar um cliente existente
    public void atualizarCliente(int id, Cliente clienteAtualizado) {
        clienteRepository.update(id, clienteAtualizado);
    }

    // Método para deletar um cliente pelo seu ID
    public void deletarCliente(int id) {
        clienteRepository.deleteById(id);
    }
}
