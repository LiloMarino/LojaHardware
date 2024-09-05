package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.loja.hardware.model.Categoria;

@Repository
public class CategoriaRepository implements CrudRepository<Categoria> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        return jdbcTemplate.update(sql, categoria.getNome());
    }

    @Override
    public Categoria findById(int id) {
        String sql = "SELECT id_categoria, nome FROM categoria WHERE id_categoria = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(rs.getInt("id_categoria"));
            categoria.setNome(rs.getString("nome"));
            return categoria;
        }, id);
    }

    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT id_categoria, nome FROM categoria";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(rs.getInt("id_categoria"));
            categoria.setNome(rs.getString("nome"));
            return categoria;
        });
    }

    @Override
    public int update(int id, Categoria categoria) {
        String sql = "UPDATE categoria SET nome = ? WHERE id_categoria = ?";
        return jdbcTemplate.update(sql, categoria.getNome(), id);
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        return jdbcTemplate.update(sql, id);
    }

}

