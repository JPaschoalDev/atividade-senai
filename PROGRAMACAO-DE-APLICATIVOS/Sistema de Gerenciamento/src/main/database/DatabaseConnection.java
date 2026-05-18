package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsável por criar e fornecer conexões com o banco SQLite.
 * Utiliza Singleton para garantir apenas uma fonte de conexão.
 */
public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:clinica_veterinaria.db";

    private DatabaseConnection() {}

    /**
     * Retorna uma nova conexão com o banco de dados.
     * O chamador é responsável por fechar a conexão após o uso.
     */
    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Cria todas as tabelas do banco caso ainda não existam.
     * Deve ser chamado uma única vez na inicialização do sistema.
     */
    public static void inicializarBanco() {
        String sqlTutores = """
            CREATE TABLE IF NOT EXISTS tutores (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                nome      TEXT    NOT NULL,
                telefone  TEXT,
                email     TEXT,
                endereco  TEXT
            );
            """;

        String sqlVeterinarios = """
            CREATE TABLE IF NOT EXISTS veterinarios (
                id            INTEGER PRIMARY KEY AUTOINCREMENT,
                nome          TEXT    NOT NULL,
                telefone      TEXT,
                email         TEXT,
                crmv          TEXT    NOT NULL UNIQUE,
                especialidade TEXT
            );
            """;

        String sqlAnimais = """
            CREATE TABLE IF NOT EXISTS animais (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                nome      TEXT    NOT NULL,
                especie   TEXT    NOT NULL,
                raca      TEXT,
                idade     INTEGER,
                tutor_id  INTEGER NOT NULL,
                FOREIGN KEY (tutor_id) REFERENCES tutores(id)
            );
            """;

        String sqlConsultas = """
            CREATE TABLE IF NOT EXISTS consultas (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id       INTEGER NOT NULL,
                veterinario_id  INTEGER NOT NULL,
                data            TEXT    NOT NULL,
                motivo          TEXT,
                diagnostico     TEXT,
                FOREIGN KEY (animal_id)      REFERENCES animais(id),
                FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id)
            );
            """;

        try (Connection con = obterConexao();
             Statement stmt = con.createStatement()) {

            stmt.execute(sqlTutores);
            stmt.execute(sqlVeterinarios);
            stmt.execute(sqlAnimais);
            stmt.execute(sqlConsultas);
            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco: " + e.getMessage());
        }
    }
}
