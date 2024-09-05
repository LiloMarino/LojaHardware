package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.loja.hardware.model.Produto;

@Repository
public class ProdutoRepository implements CrudRepository<Produto> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Produto produto) {
        if (!produto.getDescricao().isEmpty()) {
            String sql = "INSERT INTO produtos (nome, preco, quantidade_estoque, descricao, id_subcategoria, id_fabricante) VALUES (?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql, produto.getNome(), produto.getPreco(), produto.getEstoque(),
                    produto.getDescricao(), produto.getIdSubcategoria(), produto.getIdFabricante());
        } else {
            String sql = "INSERT INTO produtos (nome, preco, quantidade_estoque, id_subcategoria, id_fabricante) VALUES (?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql, produto.getNome(), produto.getPreco(), produto.getEstoque(),
                    produto.getIdSubcategoria(), produto.getIdFabricante());
        }
    }

    @Override
    public Produto findById(int id) {
        String sql = "SELECT id_produto, nome, preco, quantidade_estoque, descricao, id_subcategoria, id_fabricante FROM produtos WHERE id_produto = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Produto produto = new Produto();
            produto.setIdProduto(rs.getInt("id_produto"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco(rs.getBigDecimal("preco"));
            produto.setEstoque(rs.getInt("quantidade_estoque"));
            produto.setDescricao(rs.getString("descricao"));
            produto.setIdSubcategoria(rs.getInt("id_subcategoria"));
            produto.setIdFabricante(rs.getInt("id_fabricante"));
            return produto;
        }, id);
    }

    @Override
    public List<Produto> findAll() {
        String sql = "SELECT id_produto, nome, preco, quantidade_estoque, descricao, id_subcategoria, id_fabricante FROM produtos";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Produto produto = new Produto();
            produto.setIdProduto(rs.getInt("id_produto"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco(rs.getBigDecimal("preco"));
            produto.setEstoque(rs.getInt("quantidade_estoque"));
            produto.setDescricao(rs.getString("descricao"));
            produto.setIdSubcategoria(rs.getInt("id_subcategoria"));
            produto.setIdFabricante(rs.getInt("id_fabricante"));
            return produto;
        });
    }

    @Override
    public int update(int id, Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade_estoque = ?, descricao = ?, id_subcategoria = ?, id_fabricante = ? WHERE id_produto = ?";
        return jdbcTemplate.update(sql, produto.getNome(), produto.getPreco(), produto.getEstoque(),
                produto.getDescricao(), produto.getIdSubcategoria(), produto.getIdFabricante(), id);
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        return jdbcTemplate.update(sql, id);
    }
}
