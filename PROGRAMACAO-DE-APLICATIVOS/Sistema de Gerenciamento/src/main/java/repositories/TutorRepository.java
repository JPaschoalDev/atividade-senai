package repositories;

import database.DatabaseConnection;
import models.Tutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de acesso a dados para a entidade Tutor.
 * Toda lógica SQL fica aqui — as classes de domínio não conhecem SQL.
 */
public class TutorRepository {

    public void salvar(Tutor tutor) {
        String sql = "INSERT INTO tutores (nome, telefone, email, endereco) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getTelefone());
            stmt.setString(3, tutor.getEmail());
            stmt.setString(4, tutor.getEndereco());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    tutor.setId(chaves.getInt(1));
                }
            }
            System.out.println("Tutor salvo com ID: " + tutor.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao salvar tutor: " + e.getMessage());
        }
    }

    public List<Tutor> buscarTodos() {
        String sql = "SELECT * FROM tutores ORDER BY nome";
        List<Tutor> tutores = new ArrayList<>();

        try (Connection con = DatabaseConnection.obterConexao();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tutores.add(mapearTutor(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tutores: " + e.getMessage());
        }
        return tutores;
    }

    public Optional<Tutor> buscarPorId(int id) {
        String sql = "SELECT * FROM tutores WHERE id = ?";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearTutor(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tutor por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean atualizar(Tutor tutor) {
        String sql = """
            UPDATE tutores
               SET nome = ?, telefone = ?, email = ?, endereco = ?
             WHERE id = ?
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getTelefone());
            stmt.setString(3, tutor.getEmail());
            stmt.setString(4, tutor.getEndereco());
            stmt.setInt(5, tutor.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tutor: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tutores WHERE id = ?";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar tutor: " + e.getMessage());
            return false;
        }
    }

    private Tutor mapearTutor(ResultSet rs) throws SQLException {
        Tutor tutor = new Tutor(
            rs.getString("nome"),
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getString("endereco")
        );
        tutor.setId(rs.getInt("id"));
        return tutor;
    }
}
