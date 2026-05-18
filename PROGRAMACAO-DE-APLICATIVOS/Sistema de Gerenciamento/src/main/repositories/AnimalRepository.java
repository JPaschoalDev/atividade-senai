package main.repositories;

import main.database.DatabaseConnection;
import main.models.Animal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de acesso a dados para a entidade Animal.
 * Faz JOIN com tutores para retornar o nome do tutor junto.
 */
public class AnimalRepository {

    public void salvar(Animal animal) {
        String sql = """
            INSERT INTO animais (nome, especie, raca, idade, tutor_id)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, animal.getNome());
            stmt.setString(2, animal.getEspecie());
            stmt.setString(3, animal.getRaca());
            stmt.setInt(4, animal.getIdadeAnos());
            stmt.setInt(5, animal.getTutorId());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) animal.setId(chaves.getInt(1));
            }
            System.out.println("Animal salvo com ID: " + animal.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao salvar animal: " + e.getMessage());
        }
    }

    public List<Animal> buscarTodos() {
        String sql = """
            SELECT a.*, t.nome AS nome_tutor
              FROM animais a
              JOIN tutores t ON a.tutor_id = t.id
             ORDER BY a.nome
            """;
        List<Animal> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.obterConexao();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearAnimal(rs));

        } catch (SQLException e) {
            System.err.println("Erro ao buscar animais: " + e.getMessage());
        }
        return lista;
    }

    public Optional<Animal> buscarPorId(int id) {
        String sql = """
            SELECT a.*, t.nome AS nome_tutor
              FROM animais a
              JOIN tutores t ON a.tutor_id = t.id
             WHERE a.id = ?
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearAnimal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar animal por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Animal> buscarPorTutor(int tutorId) {
        String sql = """
            SELECT a.*, t.nome AS nome_tutor
              FROM animais a
              JOIN tutores t ON a.tutor_id = t.id
             WHERE a.tutor_id = ?
             ORDER BY a.nome
            """;
        List<Animal> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, tutorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapearAnimal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar animais por tutor: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(Animal animal) {
        String sql = """
            UPDATE animais
               SET nome = ?, especie = ?, raca = ?, idade = ?, tutor_id = ?
             WHERE id = ?
            """;

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, animal.getNome());
            stmt.setString(2, animal.getEspecie());
            stmt.setString(3, animal.getRaca());
            stmt.setInt(4, animal.getIdadeAnos());
            stmt.setInt(5, animal.getTutorId());
            stmt.setInt(6, animal.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar animal: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM animais WHERE id = ?";

        try (Connection con = DatabaseConnection.obterConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar animal: " + e.getMessage());
            return false;
        }
    }

    private Animal mapearAnimal(ResultSet rs) throws SQLException {
        Animal animal = new Animal(
            rs.getString("nome"),
            rs.getString("especie"),
            rs.getString("raca"),
            rs.getInt("idade"),
            rs.getInt("tutor_id")
        );
        animal.setId(rs.getInt("id"));
        animal.setNomeTutor(rs.getString("nome_tutor"));
        return animal;
    }
}
