package project.loja.hardware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.loja.hardware.model.Fabricante;
import project.loja.hardware.repository.FabricanteRepository;

@Service
public class FabricanteService {

    @Autowired
    private FabricanteRepository fabricanteRepository;

    public void cadastrarFabricante(Fabricante fabricante) {
        fabricanteRepository.save(fabricante);
    }

    public Fabricante getFabricanteById(int id) {
        return fabricanteRepository.findById(id);
    }

    public List<Fabricante> listarFabricantes() {
        return fabricanteRepository.findAll();
    }

    public void atualizarFabricante(Fabricante fabricanteAtualizado) {
        fabricanteRepository.update(fabricanteAtualizado);
    }

    public void deletarFabricante(int id) {
        fabricanteRepository.deleteById(id);
    }

}
