package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.loja.hardware.model.Subcategoria;

@Repository
public class SubcategoriaRepository implements CrudRepository<Subcategoria> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Subcategoria subcategoria) {
        String sql = "INSERT INTO subcategoria (nome, id_categoria) VALUES (?, ?)";
        return jdbcTemplate.update(sql, subcategoria.getNome(), subcategoria.getIdCategoria());
    }

    @Override
    public Subcategoria findById(int id) {
        String sql = "SELECT id_subcategoria, nome, id_categoria FROM subcategoria WHERE id_subcategoria = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.setIdSubcategoria(rs.getInt("id_subcategoria"));
            subcategoria.setNome(rs.getString("nome"));
            subcategoria.setIdCategoria(rs.getInt("id_categoria"));
            return subcategoria;
        }, id);
    }

    @Override
    public List<Subcategoria> findAll() {
        String sql = "SELECT id_subcategoria, nome, id_categoria FROM subcategoria";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.setIdSubcategoria(rs.getInt("id_subcategoria"));
            subcategoria.setNome(rs.getString("nome"));
            subcategoria.setIdCategoria(rs.getInt("id_categoria"));
            return subcategoria;
        });
    }

    @Override
    public int update(int id, Subcategoria subcategoria) {
        String sql = "UPDATE subcategoria SET nome = ?, id_categoria = ? WHERE id_subcategoria = ?";
        return jdbcTemplate.update(sql, subcategoria.getNome(), subcategoria.getIdCategoria(), id);
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM subcategoria WHERE id_subcategoria = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Subcategoria> findByIdCategoria(int idCategoria) {
        String sql = "SELECT id_subcategoria, nome, id_categoria FROM subcategoria WHERE id_categoria = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.setIdSubcategoria(rs.getInt("id_subcategoria"));
            subcategoria.setNome(rs.getString("nome"));
            subcategoria.setIdCategoria(rs.getInt("id_categoria"));
            return subcategoria;
        }, idCategoria);
    }
}
