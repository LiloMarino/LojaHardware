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

import project.loja.hardware.model.Categoria;
import project.loja.hardware.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController implements CrudController<Categoria> {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Categoria categoria) {
        categoriaService.cadastrarCategoria(categoria);
        return new ResponseEntity<>("Categoria criada com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable int id) {
        Categoria categoria = categoriaService.getCategoriaById(id);
        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id,
            @RequestBody Categoria categoriaAtualizada) {
        categoriaService.atualizarCategoria(id, categoriaAtualizada);
        return new ResponseEntity<>("Categoria atualizada com sucesso!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        categoriaService.deletarCategoria(id);
        return new ResponseEntity<>("Categoria deletada com sucesso!", HttpStatus.NO_CONTENT);
    }
}

