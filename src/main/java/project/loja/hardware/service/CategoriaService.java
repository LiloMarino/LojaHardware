package project.loja.hardware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.loja.hardware.model.Categoria;
import project.loja.hardware.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Método para cadastrar uma nova categoria
    public void cadastrarCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    // Método para obter uma categoria pelo seu ID
    public Categoria getCategoriaById(int id) {
        return categoriaRepository.findById(id);
    }

    // Método para listar todas as categorias
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // Método para atualizar uma categoria existente
    public void atualizarCategoria(int id, Categoria categoriaAtualizada) {
        categoriaRepository.update(id, categoriaAtualizada);
    }

    // Método para deletar uma categoria pelo seu ID
    public void deletarCategoria(int id) {
        categoriaRepository.deleteById(id);
    }

}

