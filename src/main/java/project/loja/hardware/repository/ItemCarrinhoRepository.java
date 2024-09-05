package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.loja.hardware.model.ItemCarrinho;

@Repository
public class ItemCarrinhoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(ItemCarrinho itemCarrinho) {
        String sql = "INSERT INTO itens_carrinho (id_cliente, id_produtos, quantidade) VALUES (?,?,?)";
        return jdbcTemplate.update(sql, itemCarrinho.getIdCliente(), itemCarrinho.getIdProduto(),
                itemCarrinho.getQuantidade());
    }

    public List<ItemCarrinho> findByIdCliente(int idCliente) {
        String sql = "SELECT id_cliente, id_produtos, quantidade FROM itens_carrinho WHERE id_cliente = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemCarrinho itemCarrinho = new ItemCarrinho();
            itemCarrinho.setIdCliente(rs.getInt("id_cliente"));
            itemCarrinho.setIdProduto(rs.getInt("id_produtos"));
            itemCarrinho.setQuantidade(rs.getInt("quantidade"));
            return itemCarrinho;
        }, idCliente);
    }

    public ItemCarrinho findByIdClienteAndIdProduto(int idCliente, int idProduto) {
        String sql = "SELECT id_cliente, id_produtos, quantidade FROM itens_carrinho WHERE id_cliente = ? AND id_produtos = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            ItemCarrinho itemCarrinho = new ItemCarrinho();
            itemCarrinho.setIdCliente(rs.getInt("id_cliente"));
            itemCarrinho.setIdProduto(rs.getInt("id_produtos"));
            itemCarrinho.setQuantidade(rs.getInt("quantidade"));
            return itemCarrinho;
        }, idCliente, idProduto);
    }

    public int update(int idCliente, int idProduto, ItemCarrinho itemCarrinho) {
        String sql = "UPDATE itens_carrinho SET quantidade = ? WHERE id_cliente = ? AND id_produtos = ?";
        return jdbcTemplate.update(sql, itemCarrinho.getQuantidade(), itemCarrinho.getIdCliente(),
                itemCarrinho.getIdProduto());
    }

    public int deleteByIdCliente(int id) {
        String sql = "DELETE FROM itens_carrinho WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByIdClienteAndIdProduto(int idCliente, int idProduto) {
        String sql = "DELETE FROM itens_carrinho WHERE id_cliente = ? AND id_produtos = ?";
        return jdbcTemplate.update(sql, idCliente, idProduto);
    }

}
