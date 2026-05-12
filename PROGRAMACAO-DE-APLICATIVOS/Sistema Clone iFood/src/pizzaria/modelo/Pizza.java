package pizzaria.modelo;

/**
 * CLASSE: Pizza
 *
 * Representa um item do cardápio da pizzaria.
 *
 * CONCEITO POO APLICADO:
 *   - Encapsulamento dos atributos.
 *   - Objeto de domínio: representa uma "coisa real" do negócio.
 */
public class Pizza {

    // -------------------------
    // ATRIBUTOS
    // -------------------------
    private int id;
    private String nome;          // Ex: "Calabresa", "Frango com Catupiry"
    private String descricao;     // Ingredientes ou descrição resumida
    private double preco;         // Preço base (tamanho médio, por padrão)
    private String tamanho;       // "P", "M" ou "G"
    private boolean disponivel;   // Se está ativa no cardápio

    // -------------------------
    // CONSTRUTOR
    // -------------------------
    public Pizza(int id, String nome, String descricao, double preco, String tamanho) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tamanho = tamanho;
        this.disponivel = true;  // Por padrão, toda pizza nova está disponível
    }

    // -------------------------
    // GETTERS
    // -------------------------
    public int getId()           { return id; }
    public String getNome()      { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco()     { return preco; }
    public String getTamanho()   { return tamanho; }
    public boolean isDisponivel(){ return disponivel; }

    // -------------------------
    // SETTERS
    // -------------------------
    public void setPreco(double preco)          { this.preco = preco; }
    public void setDisponivel(boolean disponivel){ this.disponivel = disponivel; }
    public void setDescricao(String descricao)  { this.descricao = descricao; }

    @Override
    public String toString() {
        String status = disponivel ? "✔ Disponível" : "✘ Indisponível";
        return String.format("[#%d] %-25s | Tam: %-2s | R$ %.2f | %s | %s",
                id, nome, tamanho, preco, status, descricao);
    }
}
