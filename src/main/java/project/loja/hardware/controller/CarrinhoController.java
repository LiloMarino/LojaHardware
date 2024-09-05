package project.loja.hardware.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.loja.hardware.model.ItensCarrinho;
import project.loja.hardware.service.CarrinhoService;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    // Adiciona um item ao carrinho
    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionarItem(@RequestBody ItensCarrinho itemCarrinho) {
        carrinhoService.adicionarItem(itemCarrinho);
        return new ResponseEntity<>("Item adicionado ao carrinho com sucesso!", HttpStatus.CREATED);
    }

    // Exibe os itens no carrinho de um cliente
    @GetMapping("/{idCliente}")
    public ResponseEntity<List<ItensCarrinho>> verItensCarrinho(@PathVariable int idCliente) {
        List<ItensCarrinho> itens = carrinhoService.verItensCarrinho(idCliente);
        return new ResponseEntity<>(itens, HttpStatus.OK);
    }

    // Remove um item do carrinho
    @DeleteMapping("/remover/{idCliente}/{idProduto}")
    public ResponseEntity<String> removerItem(@PathVariable int idCliente, @PathVariable int idProduto) {
        carrinhoService.removerItem(idCliente, idProduto);
        return new ResponseEntity<>("Item removido do carrinho com sucesso!", HttpStatus.NO_CONTENT);
    }
}
