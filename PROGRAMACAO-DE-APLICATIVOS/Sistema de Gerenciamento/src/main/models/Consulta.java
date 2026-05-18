package main.models;

/**
 * Representa uma consulta veterinária agendada ou realizada.
 * Associa um Animal a um Veterinário em uma data.
 */
public class Consulta {

    private int id;
    private int animalId;
    private int veterinarioId;
    private String data;      // formato: YYYY-MM-DD HH:MM
    private String motivo;
    private String diagnostico;

    // campos auxiliares para exibição
    private String nomeAnimal;
    private String nomeVeterinario;

    public Consulta(int animalId, int veterinarioId,
                    String data, String motivo, String diagnostico) {
        this.animalId = animalId;
        this.veterinarioId = veterinarioId;
        this.setData(data);
        this.motivo = motivo;
        this.diagnostico = diagnostico;
    }

    // ── Getters e Setters ──────────────────────────────────────────

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(int veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        if (data == null || data.isBlank())
            throw new IllegalArgumentException("Data da consulta não pode ser vazia.");
        this.data = data.trim();
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getNomeVeterinario() {
        return nomeVeterinario;
    }

    public void setNomeVeterinario(String nomeVeterinario) {
        this.nomeVeterinario = nomeVeterinario;
    }

    @Override
    public String toString() {
        return String.format(
            "[CONSULTA #%d] Data: %-16s | Animal: %-12s | Vet: %-15s | Motivo: %s",
            id, data,
            (nomeAnimal != null ? nomeAnimal : "ID " + animalId),
            (nomeVeterinario != null ? nomeVeterinario : "ID " + veterinarioId),
            motivo
        );
    }
}
