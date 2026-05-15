package repositories;

import database.DatabaseConnection;
import models.Consulta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de acesso a dados para a entidade Consulta.
 */
public class ConsultaRepository {

    public void salvar(Consulta consulta) {
        String sql = """
            INSERT INTO consultas (animal_id, veterinario_id, data, motivo, diagnostico)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, consulta.getAnimalId());
            stmt.setInt(2, consulta.getVeterinarioId());
            stmt.setString(3, consulta.getData());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getDiagnostico());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) consulta.setId(chaves.getInt(1));
            }
            System.out.println("Consulta registrada com ID: " + consulta.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao salvar consulta: " + e.getMessage());
        }
    }

    public List<Consulta> buscarTodas() {
        String sql = """
            SELECT c.*,
                   a.nome AS nome_animal,
                   v.nome AS nome_veterinario
              FROM consultas c
              JOIN animais     a ON c.animal_id      = a.id
              JOIN veterinarios v ON c.veterinario_id = v.id
             ORDER BY c.data DESC
            """;
        List<Consulta> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.obterConexao();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearConsulta(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao buscar consultas: " + e.getMessage());
        }
        return lista;
    }

    public Optional<Consulta> buscarPorId(int id) {
        String sql = """
            SELECT c.*,
                   a.nome AS nome_animal,
                   v.nome AS nome_veterinario
              FROM consultas c
              JOIN animais     a ON c.animal_id      = a.id
              JOIN veterinarios v ON c.veterinario_id = v.id
             WHERE c.id = ?
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearConsulta(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar consulta por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Consulta> buscarPorAnimal(int animalId) {
        String sql = """
            SELECT c.*,
                   a.nome AS nome_animal,
                   v.nome AS nome_veterinario
              FROM consultas c
              JOIN animais     a ON c.animal_id      = a.id
              JOIN veterinarios v ON c.veterinario_id = v.id
             WHERE c.animal_id = ?
             ORDER BY c.data DESC
            """;
        List<Consulta> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, animalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapearConsulta(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar consultas por animal: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(Consulta consulta) {
        String sql = """
            UPDATE consultas
               SET animal_id = ?, veterinario_id = ?, data = ?,
                   motivo = ?, diagnostico = ?
             WHERE id = ?
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getAnimalId());
            stmt.setInt(2, consulta.getVeterinarioId());
            stmt.setString(3, consulta.getData());
            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getDiagnostico());
            stmt.setInt(6, consulta.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar consulta: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM consultas WHERE id = ?";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar consulta: " + e.getMessage());
            return false;
        }
    }

    private Consulta mapearConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta(
            rs.getInt("animal_id"),
            rs.getInt("veterinario_id"),
            rs.getString("data"),
            rs.getString("motivo"),
            rs.getString("diagnostico")
        );
        c.setId(rs.getInt("id"));
        c.setNomeAnimal(rs.getString("nome_animal"));
        c.setNomeVeterinario(rs.getString("nome_veterinario"));
        return c;
    }
}
