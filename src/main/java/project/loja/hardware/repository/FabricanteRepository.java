package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.loja.hardware.model.Fabricante;

@Repository
public class FabricanteRepository implements CrudRepository<Fabricante> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Fabricante fabricante) {
        String sql = "INSERT INTO fabricante (nome) VALUES (?)";
        return jdbcTemplate.update(sql, fabricante.getNome());
    }

    @Override
    public Fabricante findById(int id) {
        String sql = "SELECT id_fabricante, nome FROM fabricante WHERE id_fabricante = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Fabricante fabricante = new Fabricante();
            fabricante.setIdFabricante(rs.getInt("id_fabricante"));
            fabricante.setNome(rs.getString("nome"));
            return fabricante;
        }, id);
    }

    @Override
    public List<Fabricante> findAll() {
        String sql = "SELECT id_fabricante, nome FROM fabricante";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Fabricante fabricante = new Fabricante();
            fabricante.setIdFabricante(rs.getInt("id_fabricante"));
            fabricante.setNome(rs.getString("nome"));
            return fabricante;
        });
    }

    @Override
    public int update(int id, Fabricante fabricante) {
        String sql = "UPDATE fabricante SET nome = ? WHERE id_fabricante = ?";
        return jdbcTemplate.update(sql, fabricante.getNome(), id);
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM fabricante WHERE id_fabricante = ?";
        return jdbcTemplate.update(sql, id);
    }

}
