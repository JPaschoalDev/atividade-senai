package models;

/**
 * Representa um animal cadastrado na clínica.
 * Associado a um Tutor (dono).
 */
public class Animal {

    private int id;
    private String nome;
    private String especie;
    private String raca;
    private int idadeAnos;
    private int tutorId;
    private String nomeTutor; // campo auxiliar para exibição

    public Animal(String nome, String especie, String raca, int idadeAnos, int tutorId) {
        this.setNome(nome);
        this.setEspecie(especie);
        this.raca = raca;
        this.setIdadeAnos(idadeAnos);
        this.tutorId = tutorId;
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
            throw new IllegalArgumentException("Nome do animal não pode ser vazio.");
        this.nome = nome.trim();
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        if (especie == null || especie.isBlank())
            throw new IllegalArgumentException("Espécie não pode ser vazia.");
        this.especie = especie.trim();
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdadeAnos() {
        return idadeAnos;
    }

    public void setIdadeAnos(int idadeAnos) {
        if (idadeAnos < 0) throw new IllegalArgumentException("Idade não pode ser negativa.");
        this.idadeAnos = idadeAnos;
    }

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public String getNomeTutor() {
        return nomeTutor;
    }

    public void setNomeTutor(String nomeTutor) {
        this.nomeTutor = nomeTutor;
    }

    @Override
    public String toString() {
        return String.format(
            "[ANIMAL] Nome: %-15s | Esp: %-10s | Raça: %-15s | Idade: %d ano(s) | Tutor: %s",
            nome, especie, raca, idadeAnos,
            (nomeTutor != null ? nomeTutor : "ID " + tutorId)
        );
    }
}
