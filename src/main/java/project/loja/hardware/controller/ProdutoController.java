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

import project.loja.hardware.model.Produto;
import project.loja.hardware.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController implements CrudController<Produto> {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<String> create(Produto produto) {
        produtoService.cadastrarProduto(produto);
        return new ResponseEntity<>("Produto criado com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(int id) {
        Produto produto = produtoService.getProdutoById(id);
        return new ResponseEntity<>(produto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> Produtos = produtoService.listarProdutos();
        return new ResponseEntity<>(Produtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id,
            @RequestBody Produto produtoAtualizado) {
        produtoService.atualizarProduto(id, produtoAtualizado);
        return new ResponseEntity<>("Produto atualizado com sucesso!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(int id) {
        produtoService.deletarProduto(id);
        return new ResponseEntity<>("Produto deletado com sucesso!", HttpStatus.NO_CONTENT);
    }
}
