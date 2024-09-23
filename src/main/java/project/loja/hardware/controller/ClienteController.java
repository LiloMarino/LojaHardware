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
import project.loja.hardware.model.Compra;
import project.loja.hardware.model.ItemCompra;
import project.loja.hardware.service.ClienteService;
import project.loja.hardware.service.CompraService;

@RestController
@RequestMapping("/clientes")
public class ClienteController implements CrudController<Cliente> {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CompraService compraService;

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

    @GetMapping("/{id}/comprar")
    public ResponseEntity<String> realizarCompra(@PathVariable int id) {
        int idCompra = compraService.realizarCompra(id);
        String notaFiscal = compraService.gerarNotaFiscal(idCompra);
        return new ResponseEntity<>(notaFiscal, HttpStatus.OK);
    }

    @GetMapping("/{id}/compras")
    public ResponseEntity<List<Compra>> verCompras(@PathVariable int id) {
        List<Compra> compras = compraService.listarComprasPorCliente(id);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/{idCliente}/compra/{idCompra}")
    public ResponseEntity<String> verCompra(@PathVariable int idCliente, @PathVariable int idCompra) {
        String notaFiscal = compraService.gerarNotaFiscal(idCompra);
        return new ResponseEntity<>(notaFiscal, HttpStatus.OK);
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

    @PutMapping("/{idCliente}/compra/{idCompra}")
    public ResponseEntity<String> reembolsoParcial(@PathVariable int idCliente, @PathVariable int idCompra,
            @RequestBody ItemCompra itemCompraAtualizado) {
        compraService.atualizarItensCompra(idCompra, idCompra, itemCompraAtualizado);
        String notaFiscal = compraService.gerarNotaFiscal(idCompra);
        return new ResponseEntity<>(notaFiscal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        clienteService.deletarCliente(id);
        return new ResponseEntity<>("Cliente deletado com sucesso!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{idCliente}/compra/{idCompra}")
    public ResponseEntity<String> reembolsarCompra(@PathVariable int idCliente, @PathVariable int idCompra) {
        compraService.deletarCompra(idCompra);
        String notaFiscal = compraService.gerarNotaFiscal(idCompra);
        return new ResponseEntity<>(notaFiscal, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{idCliente}/compra/{idCompra}/{idProduto}")
    public ResponseEntity<String> reembolsarItem(@PathVariable int idCliente, @PathVariable int idCompra,
            @PathVariable int idProduto) {
        compraService.deletarItemCompra(idCompra, idProduto);
        String notaFiscal = compraService.gerarNotaFiscal(idCompra);
        return new ResponseEntity<>(notaFiscal, HttpStatus.NO_CONTENT);
    }
}
