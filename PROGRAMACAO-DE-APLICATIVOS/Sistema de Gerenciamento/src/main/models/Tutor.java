package main.models;

/**
 * Representa o tutor (dono) de um animal.
 * Herda de Pessoa e adiciona o atributo endereço.
 */
public class Tutor extends Pessoa {

    private String endereco;

    public Tutor(String nome, String telefone, String email, String endereco) {
        super(nome, telefone, email);
        this.endereco = endereco;
    }

    // ── Getter e Setter específicos ────────────────────────────────

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    // ── Polimorfismo: sobrescreve apresentar() ─────────────────────

    @Override
    public String apresentar() {
        return String.format(
            "[TUTOR] Nome: %-25s | Tel: %-15s | Email: %-25s | End: %s",
            getNome(), getTelefone(), getEmail(), endereco
        );
    }
}
