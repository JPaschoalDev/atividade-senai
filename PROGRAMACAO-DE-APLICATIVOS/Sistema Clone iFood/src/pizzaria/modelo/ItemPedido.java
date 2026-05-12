package pizzaria.modelo;

/**
 * CLASSE: ItemPedido
 *
 * Representa uma linha do pedido: uma Pizza + a quantidade solicitada.
 *
 * CONCEITO POO APLICADO:
 *   - Composição: ItemPedido "tem" uma Pizza (relação HAS-A).
 *     Em vez de copiar os dados da pizza, guardamos a referência ao objeto Pizza.
 *   - Método de cálculo encapsulado: getSubtotal() fica aqui, não espalhado pelo sistema.
 */
public class ItemPedido {

    // -------------------------
    // ATRIBUTOS
    // -------------------------
    private Pizza pizza;        // Referência ao objeto Pizza (composição)
    private int quantidade;     // Quantas unidades dessa pizza foram pedidas
    private String observacao;  // Ex: "sem cebola", "borda recheada"

    // -------------------------
    // CONSTRUTOR
    // -------------------------
    public ItemPedido(Pizza pizza, int quantidade, String observacao) {
        this.pizza = pizza;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    // Construtor sem observação (sobrecarga de construtor)
    public ItemPedido(Pizza pizza, int quantidade) {
        this(pizza, quantidade, "");  // Chama o construtor principal com obs vazia
    }

    // -------------------------
    // GETTERS
    // -------------------------
    public Pizza getPizza()       { return pizza; }
    public int getQuantidade()    { return quantidade; }
    public String getObservacao() { return observacao; }

    /**
     * MÉTODO DE NEGÓCIO: getSubtotal
     * Calcula o valor total desse item: preço unitário * quantidade.
     */
    public double getSubtotal() {
        return pizza.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        String obs = observacao.isEmpty() ? "" : " | Obs: " + observacao;
        return String.format("  • %dx %-25s R$ %.2f cada = R$ %.2f%s",
                quantidade, pizza.getNome(), pizza.getPreco(), getSubtotal(), obs);
    }
}
