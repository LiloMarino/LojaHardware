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

import project.loja.hardware.model.Subcategoria;
import project.loja.hardware.service.SubcategoriaService;

@RestController
@RequestMapping("/subcategorias")
public class SubcategoriaController implements CrudController<Subcategoria> {

    @Autowired
    private SubcategoriaService subcategoriaService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Subcategoria subcategoria) {
        subcategoriaService.cadastrarSubcategoria(subcategoria);
        return new ResponseEntity<>("Subcategoria criada com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subcategoria> getById(@PathVariable int id) {
        Subcategoria subcategoria = subcategoriaService.getSubcategoriaById(id);
        return new ResponseEntity<>(subcategoria, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Subcategoria>> getAll() {
        List<Subcategoria> subcategorias = subcategoriaService.listarSubcategorias();
        return new ResponseEntity<>(subcategorias, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Subcategoria subcategoriaAtualizada) {
        subcategoriaService.atualizarSubcategoria(id, subcategoriaAtualizada);
        return new ResponseEntity<>("Subcategoria atualizada com sucesso!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        subcategoriaService.deletarSubcategoria(id);
        return new ResponseEntity<>("Subcategoria deletada com sucesso!", HttpStatus.NO_CONTENT);
    }
}
