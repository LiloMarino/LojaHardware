package project.loja.hardware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.loja.hardware.model.ItemCarrinho;
import project.loja.hardware.repository.ItemCarrinhoRepository;

@Service
public class ItemCarrinhoService {

    @Autowired
    private ItemCarrinhoRepository carrinhoRepository;

    // Adiciona um item ao carrinho
    public void adicionarItem(ItemCarrinho itemCarrinho) {
        // Verifica se o item já está no carrinho, se sim, atualiza a quantidade
        ItemCarrinho itemExistente = carrinhoRepository.findByIdClienteAndIdProduto(itemCarrinho.getIdCliente(), itemCarrinho.getIdProduto());
        
        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + itemCarrinho.getQuantidade());
            carrinhoRepository.update(itemExistente.getIdCliente(), itemExistente.getIdProduto(), itemExistente);
        } else {
            // Caso contrário, adiciona um novo item ao carrinho
            carrinhoRepository.save(itemCarrinho);
        }
    }

    // Exibe todos os itens no carrinho de um cliente
    public List<ItemCarrinho> verItemCarrinho(int idCliente) {
        return carrinhoRepository.findByIdCliente(idCliente);
    }

    // Remove um item do carrinho
    public void removerItem(int idCliente, int idProduto) {
        carrinhoRepository.deleteByIdClienteAndIdProduto(idCliente, idProduto);
    }
}