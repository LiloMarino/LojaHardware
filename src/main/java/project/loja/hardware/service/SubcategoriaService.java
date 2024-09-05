package project.loja.hardware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.loja.hardware.model.Subcategoria;
import project.loja.hardware.repository.SubcategoriaRepository;

@Service
public class SubcategoriaService {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    // Método para cadastrar uma nova subcategoria
    public void cadastrarSubcategoria(Subcategoria subcategoria) {
        subcategoriaRepository.save(subcategoria);
    }

    // Método para obter uma subcategoria pelo seu ID
    public Subcategoria getSubcategoriaById(int id) {
        return subcategoriaRepository.findById(id);
    }

    // Método para listar todas as subcategorias
    public List<Subcategoria> listarSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    // Método para atualizar uma subcategoria existente
    public void atualizarSubcategoria(int id, Subcategoria subcategoriaAtualizada) {
        subcategoriaRepository.update(id, subcategoriaAtualizada);
    }

    // Método para deletar uma subcategoria pelo seu ID
    public void deletarSubcategoria(int id) {
        subcategoriaRepository.deleteById(id);
    }
}
