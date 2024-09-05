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

import project.loja.hardware.model.Fabricante;
import project.loja.hardware.model.Produto;
import project.loja.hardware.service.FabricanteService;
import project.loja.hardware.service.ProdutoService;

@RestController
@RequestMapping("/fabricantes")
public class FabricanteController implements CrudController<Fabricante> {

    @Autowired
    private FabricanteService fabricanteService;

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Fabricante fabricante) {
        fabricanteService.cadastrarFabricante(fabricante);
        return new ResponseEntity<>("Fabricante criado com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fabricante> getById(@PathVariable int id) {
        Fabricante fabricante = fabricanteService.getFabricanteById(id);
        return new ResponseEntity<>(fabricante, HttpStatus.OK);
    }

    @GetMapping("/{id}/produtos")
    public ResponseEntity<List<Produto>> getProdutos(@PathVariable int id) {
        List<Produto> produtos = produtoService.getFabricanteById(id);
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Fabricante>> getAll() {
        List<Fabricante> fabricantes = fabricanteService.listarFabricantes();
        return new ResponseEntity<>(fabricantes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id,
            @RequestBody Fabricante fabricanteAtualizado) {
        fabricanteService.atualizarFabricante(id, fabricanteAtualizado);
        return new ResponseEntity<>("Fabricante atualizado com sucesso!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        fabricanteService.deletarFabricante(id);
        return new ResponseEntity<>("Fabricante deletado com sucesso!", HttpStatus.NO_CONTENT);
    }
}
