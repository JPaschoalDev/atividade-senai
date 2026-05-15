package models;

/**
 * Classe base que representa qualquer pessoa no sistema.
 * Aplica encapsulamento com atributos privados e getters/setters.
 */
public abstract class Pessoa {

    private int id;
    private String nome;
    private String telefone;
    private String email;

    public Pessoa(String nome, String telefone, String email) {
        this.setNome(nome);
        this.setTelefone(telefone);
        this.setEmail(email);
    }

    // ── Getters e Setters ──────────────────────────────────────────

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID deve ser positivo.");
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        this.nome = nome.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ── Método polimórfico ─────────────────────────────────────────

    /**
     * Cada subclasse sobrescreve este método para se apresentar
     * de forma específica ao seu papel no sistema (polimorfismo).
     */
    public abstract String apresentar();

    @Override
    public String toString() {
        return apresentar();
    }
}
