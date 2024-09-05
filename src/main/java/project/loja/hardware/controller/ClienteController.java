package project.loja.hardware.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.loja.hardware.model.Cliente;
import project.loja.hardware.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController implements CrudController<Cliente> {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Cliente cliente) {
        clienteService.cadastrarCliente(cliente);
        return new ResponseEntity<>("Cliente criado com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable int id) {
        Cliente cliente = clienteService.getClienteById(id);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        List<Cliente> clientes = clienteService.listarClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Cliente clienteAtualizado) {
        clienteService.atualizarCliente(id, clienteAtualizado);
        return new ResponseEntity<>("Cliente atualizado com sucesso!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        clienteService.deletarCliente(id);
        return new ResponseEntity<>("Cliente deletado com sucesso!", HttpStatus.NO_CONTENT);
    }
}
