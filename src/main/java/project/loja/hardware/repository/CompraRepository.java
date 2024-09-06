package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.loja.hardware.model.Compra;
import project.loja.hardware.model.ItemCompra;

@Repository
public class CompraRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public int save(Compra compra) {
        // Insere uma nova compra
        String insertCompraSql = "INSERT INTO compras (id_cliente, data_compra, valor_total) " +
                "VALUES (?, ?, (SELECT SUM(ic.quantidade * p.preco) FROM itens_carrinho AS ic " +
                "INNER JOIN produtos AS p ON p.id_produtos = ic.id_produtos " +
                "WHERE ic.id_cliente = ?)) RETURNING id_compras";

        // Recupera o ID da compra
        int idCompra = jdbcTemplate.queryForObject(insertCompraSql, Integer.class, compra.getIdCliente(),
                compra.getDataCompra(), compra.getIdCliente());

        // Insere os itens na tabela itens_compra
        String insertItensCompraSql = "INSERT INTO itens_compra " +
                "(id_compras, id_cliente, id_produtos, quantidade, preco_unitario) " +
                "SELECT ?, ic.id_cliente, ic.id_produtos, ic.quantidade, p.preco FROM itens_carrinho AS ic " +
                "INNER JOIN produtos AS p ON ic.id_produtos = p.id_produtos " +
                "WHERE ic.id_cliente = ?";
        jdbcTemplate.update(insertItensCompraSql, idCompra, compra.getIdCliente());

        // Atualiza a quantidade de estoque dos produtos
        String updateEstoqueSql = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ic.quantidade " +
                "FROM itens_carrinho AS ic WHERE produtos.id_produtos = ic.id_produtos " +
                "AND ic.id_cliente = ?";
        jdbcTemplate.update(updateEstoqueSql, compra.getIdCliente());

        // Remove os itens do carrinho
        String deleteItensCarrinhoSql = "DELETE FROM itens_carrinho WHERE id_cliente = ?";
        jdbcTemplate.update(deleteItensCarrinhoSql, compra.getIdCliente());
        return idCompra;
    }

    public Compra findByIdCompra(int idCompra) {
        String sql = "SELECT id_compras, id_cliente, data_compra, valor_total FROM compras WHERE id_compras = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Compra compra = new Compra();
            compra.setIdCompra(rs.getInt("id_compras"));
            compra.setIdCliente(rs.getInt("id_cliente"));
            compra.setDataCompra(rs.getDate("data_compra"));
            compra.setValorTotal(rs.getBigDecimal("valor_total"));
            return compra;
        }, idCompra);
    }

    public List<Compra> findByIdCliente(int idCliente) {
        String sql = "SELECT id_compras, data_compra, valor_total FROM compras WHERE id_cliente = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Compra compra = new Compra();
            compra.setIdCompra(rs.getInt("id_compras"));
            compra.setDataCompra(rs.getDate("data_compra"));
            compra.setValorTotal(rs.getBigDecimal("valor_total"));
            return compra;
        }, idCliente);
    }

    public List<ItemCompra> findItemsByCompra(Compra compra) {
        String sql = "SELECT id_cliente, id_compras, id_produtos, preco_unitario, quantidade FROM itens_compra WHERE id_compras = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setIdCliente(rs.getInt("id_cliente"));
            itemCompra.setIdCompra(rs.getInt("id_compras"));
            itemCompra.setIdProduto(rs.getInt("id_produtos"));
            itemCompra.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
            itemCompra.setQuantidade(rs.getInt("quantidade"));
            return itemCompra;
        });
    }

    public int updateItensCompra(int idCompra, int idProduto, ItemCompra itemCompra) {
        String sql = "UPDATE itens_compra SET preco_unitario = ?, quantidade = ? WHERE id_compras = ? AND id_produtos = ?";
        return jdbcTemplate.update(sql, itemCompra.getPrecoUnitario(), itemCompra.getQuantidade(), idCompra, idProduto);
    }

    public int deleteByIdCompra(int idCompra) {
        String sql = "DELETE FROM compras WHERE id_compras = ?";
        return jdbcTemplate.update(sql, idCompra);
    }

    public int deleteByIdCompraAndIdProduto(int idCompra, int idProduto) {
        String sql = "DELETE FROM itens_compra WHERE id_compras = ? AND id_produtos = ?";
        return jdbcTemplate.update(sql, idCompra, idProduto);
    }

}
