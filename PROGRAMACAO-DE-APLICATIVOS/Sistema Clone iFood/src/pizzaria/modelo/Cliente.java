package pizzaria.modelo;

/**
 * CLASSE: Cliente
 *
 * Representa um cliente cadastrado na pizzaria.
 *
 * CONCEITO POO APLICADO:
 *   - Encapsulamento: todos os atributos são 'private'.
 *     Só podem ser acessados de fora via métodos get/set.
 *   - Isso protege os dados e evita alterações acidentais diretas.
 */
public class Cliente {

    // -------------------------
    // ATRIBUTOS (private = encapsulados)
    // -------------------------
    private int id;              // Identificador único do cliente
    private String nome;         // Nome completo
    private String telefone;     // Telefone para contato
    private String endereco;     // Endereço (usado em pedidos delivery)

    // -------------------------
    // CONSTRUTOR
    // Chamado com 'new Cliente(...)' para criar um objeto Cliente.
    // -------------------------
    public Cliente(int id, String nome, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    // -------------------------
    // GETTERS — permitem LER os atributos privados
    // -------------------------
    public int getId()          { return id; }
    public String getNome()     { return nome; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }

    // -------------------------
    // SETTERS — permitem ALTERAR os atributos privados com validação futura
    // -------------------------
    public void setNome(String nome)         { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    /**
     * toString() — sobrescrita do método da classe Object.
     * Quando você usa 'System.out.println(cliente)', esse método é chamado.
     */
    @Override
    public String toString() {
        return String.format("[#%d] %s | Tel: %s | End: %s",
                id, nome, telefone, endereco);
    }
}
