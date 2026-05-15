package repositories;

import database.DatabaseConnection;
import models.Veterinario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de acesso a dados para a entidade Veterinario.
 */
public class VeterinarioRepository {

    public void salvar(Veterinario vet) {
        String sql = """
            INSERT INTO veterinarios (nome, telefone, email, crmv, especialidade)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vet.getNome());
            stmt.setString(2, vet.getTelefone());
            stmt.setString(3, vet.getEmail());
            stmt.setString(4, vet.getCrmv());
            stmt.setString(5, vet.getEspecialidade());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) vet.setId(chaves.getInt(1));
            }
            System.out.println("Veterinário salvo com ID: " + vet.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao salvar veterinário: " + e.getMessage());
        }
    }

    public List<Veterinario> buscarTodos() {
        String sql = "SELECT * FROM veterinarios ORDER BY nome";
        List<Veterinario> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.obterConexao();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearVeterinario(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao buscar veterinários: " + e.getMessage());
        }
        return lista;
    }

    public Optional<Veterinario> buscarPorId(int id) {
        String sql = "SELECT * FROM veterinarios WHERE id = ?";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearVeterinario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar veterinário por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean atualizar(Veterinario vet) {
        String sql = """
            UPDATE veterinarios
               SET nome = ?, telefone = ?, email = ?, crmv = ?, especialidade = ?
             WHERE id = ?
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, vet.getNome());
            stmt.setString(2, vet.getTelefone());
            stmt.setString(3, vet.getEmail());
            stmt.setString(4, vet.getCrmv());
            stmt.setString(5, vet.getEspecialidade());
            stmt.setInt(6, vet.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar veterinário: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM veterinarios WHERE id = ?";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar veterinário: " + e.getMessage());
            return false;
        }
    }

    private Veterinario mapearVeterinario(ResultSet rs) throws SQLException {
        Veterinario vet = new Veterinario(
            rs.getString("nome"),
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getString("crmv"),
            rs.getString("especialidade")
        );
        vet.setId(rs.getInt("id"));
        return vet;
    }
}
