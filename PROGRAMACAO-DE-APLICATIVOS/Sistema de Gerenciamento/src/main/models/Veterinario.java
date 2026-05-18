package main.models;

/**
 * Representa um veterinário da clínica.
 * Herda de Pessoa e adiciona CRMV e especialidade.
 */
public class Veterinario extends Pessoa {

    private String crmv;
    private String especialidade;

    public Veterinario(String nome, String telefone, String email,
                       String crmv, String especialidade) {
        super(nome, telefone, email);
        this.setCrmv(crmv);
        this.especialidade = especialidade;
    }

    // ── Getters e Setters específicos ──────────────────────────────

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        if (crmv == null || crmv.isBlank())
            throw new IllegalArgumentException("CRMV não pode ser vazio.");
        this.crmv = crmv.trim();
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    // ── Polimorfismo: sobrescreve apresentar() ─────────────────────

    @Override
    public String apresentar() {
        return String.format(
            "[VETERINÁRIO] Nome: %-20s | CRMV: %-10s | Esp: %-20s | Tel: %s",
            getNome(), crmv, especialidade, getTelefone()
        );
    }
}
