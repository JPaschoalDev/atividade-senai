package pizzaria.modelo;

/**
 * CLASSE: Promocao
 *
 * Representa uma promoção aplicável a um pedido.
 *
 * CONCEITO POO APLICADO:
 *   - Enum interno (TipoPromocao) para representar categorias fixas.
 *   - Método de negócio dentro da classe (calcularDesconto).
 *   - Separação de responsabilidade: a Promoção sabe calcular seu próprio desconto.
 */
public class Promocao {

    /**
     * ENUM: TipoPromocao
     * Um enum é uma forma de limitar os valores possíveis de um campo.
     * Aqui, uma promoção só pode ser PERCENTUAL ou COMBO.
     */
    public enum TipoPromocao {
        PERCENTUAL,  // Ex: 10% de desconto no total
        COMBO        // Leve 2 pizzas, pague 1 (a mais barata é gratuita)
    }

    // -------------------------
    // ATRIBUTOS
    // -------------------------
    private int id;
    private String nome;               // Ex: "Promoção de Sexta"
    private TipoPromocao tipo;
    private double valorDesconto;      // Usado apenas no tipo PERCENTUAL (0 a 100)
    private boolean ativa;

    // -------------------------
    // CONSTRUTOR
    // -------------------------
    public Promocao(int id, String nome, TipoPromocao tipo, double valorDesconto) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.valorDesconto = valorDesconto;
        this.ativa = true;
    }

    // -------------------------
    // GETTERS
    // -------------------------
    public int getId()             { return id; }
    public String getNome()        { return nome; }
    public TipoPromocao getTipo()  { return tipo; }
    public double getValorDesconto(){ return valorDesconto; }
    public boolean isAtiva()       { return ativa; }

    // -------------------------
    // SETTERS
    // -------------------------
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    /**
     * MÉTODO DE NEGÓCIO: calcularDesconto
     *
     * Recebe o subtotal do pedido e a lista de pizzas (para o combo),
     * e retorna o valor a ser descontado.
     *
     * @param subtotal   Valor total dos itens antes do desconto
     * @param menorPreco Preço da pizza mais barata no pedido (usado no combo)
     * @return           Valor do desconto em reais
     */
    public double calcularDesconto(double subtotal, double menorPreco) {
        if (!ativa) return 0.0;

        switch (tipo) {
            case PERCENTUAL:
                // Desconto = subtotal * (percentual / 100)
                return subtotal * (valorDesconto / 100.0);

            case COMBO:
                // No combo 2x1, a pizza mais barata sai de graça
                return menorPreco;

            default:
                return 0.0;
        }
    }

    @Override
    public String toString() {
        String descTipo;
        if (tipo == TipoPromocao.PERCENTUAL) {
            descTipo = "Desconto de " + valorDesconto + "%";
        } else {
            descTipo = "Combo: leve 2, pague 1";
        }
        String status = ativa ? "✔ Ativa" : "✘ Inativa";
        return String.format("[#%d] %-20s | %s | %s", id, nome, descTipo, status);
    }
}
