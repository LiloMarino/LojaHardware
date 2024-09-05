package project.loja.hardware.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import project.loja.hardware.model.Cliente;

@Repository
public class ClienteRepository implements CrudRepository<Cliente> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, nome, email, senha) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, cliente.getCpf(), cliente.getNome(), cliente.getEmail(), cliente.getSenha());
    }

    @Override
    public Cliente findById(int id) {
        String sql = "SELECT id_cliente, cpf, nome, email, senha FROM cliente WHERE id_cliente = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(rs.getInt("id_cliente"));
            cliente.setCpf(rs.getString("cpf"));
            cliente.setNome(rs.getString("nome"));
            cliente.setEmail(rs.getString("email"));
            cliente.setSenha(rs.getString("senha"));
            return cliente;
        }, id);
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT id_cliente, cpf, nome, email, senha FROM cliente";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(rs.getInt("id_cliente"));
            cliente.setCpf(rs.getString("cpf"));
            cliente.setNome(rs.getString("nome"));
            cliente.setEmail(rs.getString("email"));
            cliente.setSenha(rs.getString("senha"));
            return cliente;
        });
    }

    @Override
    public int update(int id, Cliente clienteAtualizado) {
        String sql = "UPDATE cliente SET cpf = ?, nome = ?, email = ?, senha = ? WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, clienteAtualizado.getCpf(), clienteAtualizado.getNome(), 
                                    clienteAtualizado.getEmail(), clienteAtualizado.getSenha(), id);
    }

    @Override
    public int deleteById(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, id);
    }
}
