package project.loja.hardware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.loja.hardware.model.Produto;
import project.loja.hardware.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public void cadastrarProduto(Produto produto) {
        produtoRepository.save(produto);
    }

    public Produto getProdutoById(int id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> getFabricanteById(int id) {
        return produtoRepository.findByIdFabricante(id);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public void atualizarProduto(int id, Produto produtoAtualizado) {
        produtoRepository.update(id, produtoAtualizado);
    }

    public void deletarProduto(int id) {
        produtoRepository.deleteById(id);
    }

}
