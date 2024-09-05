package project.loja.hardware.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RelatorioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getTopCategorias() {
        String sql = "SELECT c.nome AS categoria, SUM(ic.quantidade) AS total_vendido " +
                "FROM itens_compra ic " +
                "INNER JOIN produtos p ON ic.id_produtos = p.id_produtos " +
                "INNER JOIN subcategoria sc ON p.id_subcategoria = sc.id_subcategoria " +
                "INNER JOIN categoria c ON sc.id_categoria = c.id_categoria " +
                "GROUP BY c.nome " +
                "ORDER BY total_vendido DESC " +
                "LIMIT 10";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getTopProdutos() {
        String sql = "SELECT p.nome AS produto, SUM(ic.quantidade) AS total_vendido " +
                "FROM itens_compra ic " +
                "INNER JOIN produtos p ON ic.id_produtos = p.id_produtos " +
                "GROUP BY p.nome " +
                "ORDER BY total_vendido DESC " +
                "LIMIT 10";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getTopFabricantes() {
        String sql = "SELECT f.nome AS fabricante, SUM(ic.quantidade) AS total_vendido " +
                "FROM itens_compra ic " +
                "INNER JOIN produtos p ON ic.id_produtos = p.id_produtos " +
                "INNER JOIN fabricante f ON p.id_fabricante = f.id_fabricante " +
                "GROUP BY f.nome " +
                "ORDER BY total_vendido DESC " +
                "LIMIT 10";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getSellHistoryLastYear() {
        String sql = "SELECT " +
                "TO_CHAR(c.data_compra, 'YYYY-MM') AS mes, " +
                "SUM(c.valor_total) AS total_vendas " +
                "FROM compras c " +
                "WHERE c.data_compra >= NOW() - INTERVAL '1 year' " +
                "GROUP BY TO_CHAR(c.data_compra, 'YYYY-MM') " +
                "ORDER BY mes";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getLowStockProdutos() {
        String sql = "SELECT p.nome AS produto, p.quantidade_estoque AS estoque_atual" +
                "FROM produtos p " +
                "WHERE p.quantidade_estoque < 10 " +
                "ORDER BY p.quantidade_estoque ASC ";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getBestProdutoCombination() {
        String subconsulta = "WITH compra_comum AS ( " +
                "SELECT ic1.id_produtos AS produto1, ic2.id_produtos AS produto2, COUNT(*) AS vezes_comprados_juntos " +
                "FROM itens_compra ic1 " +
                "INNER JOIN itens_compra ic2  " +
                "ON ic1.id_compras = ic2.id_compras AND ic1.id_produtos <> ic2.id_produtos " +
                "GROUP BY ic1.id_produtos, ic2.id_produtos ) ";
        String sql = subconsulta +
                "SELECT p1.nome AS produto, p2.nome AS comprado_junto, cc.vezes_comprados_juntos " +
                "FROM compra_comum cc " +
                "INNER JOIN produtos p1 ON cc.produto1 = p1.id_produtos " +
                "INNER JOIN produtos p2 ON cc.produto2 = p2.id_produtos " +
                "ORDER BY cc.vezes_compradas_juntas DESC " +
                "LIMIT 10 ";
        return jdbcTemplate.queryForList(sql);
    }
}
