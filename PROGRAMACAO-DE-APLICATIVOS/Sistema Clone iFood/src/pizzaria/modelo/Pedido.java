package pizzaria.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASSE: Pedido
 *
 * Representa um pedido completo feito por um cliente.
 * Suporta dois tipos: DELIVERY (com endereço) e BALCAO (retirada).
 *
 * CONCEITO POO APLICADO:
 *   - Composição: Pedido "tem" um Cliente, vários ItemPedido e uma Promocao opcional.
 *   - Enum interno: TipoPedido limita os valores válidos.
 *   - ArrayList<ItemPedido>: coleção de objetos — fundamento de POO com coleções.
 *   - Método de negócio: calcularTotal() centraliza a lógica de precificação.
 */
public class Pedido {

    /**
     * Enum para tipo do pedido.
     * Evita usar Strings soltas como "delivery" ou "balcao" (propenso a erros de digitação).
     */
    public enum TipoPedido {
        DELIVERY,
        BALCAO
    }

    /**
     * Enum para status do pedido.
     */
    public enum StatusPedido {
        ABERTO,
        EM_PREPARO,
        PRONTO,
        ENTREGUE,
        CANCELADO
    }

    // -------------------------
    // ATRIBUTOS
    // -------------------------
    private int id;
    private Cliente cliente;                   // Composição com Cliente
    private List<ItemPedido> itens;            // Lista de itens do pedido
    private TipoPedido tipo;
    private StatusPedido status;
    private Promocao promocaoAplicada;         // Pode ser null se não houver promoção
    private LocalDateTime dataHora;            // Momento em que o pedido foi criado
    private String enderecoEntrega;            // Preenchido só se for DELIVERY

    // -------------------------
    // CONSTRUTOR
    // -------------------------
    public Pedido(int id, Cliente cliente, TipoPedido tipo) {
        this.id = id;
        this.cliente = cliente;
        this.tipo = tipo;
        this.status = StatusPedido.ABERTO;     // Todo pedido começa como ABERTO
        this.itens = new ArrayList<>();         // Inicia a lista vazia
        this.dataHora = LocalDateTime.now();   // Registra o momento atual
        this.promocaoAplicada = null;
        this.enderecoEntrega = tipo == TipoPedido.DELIVERY ? cliente.getEndereco() : "";
    }

    // -------------------------
    // MÉTODOS DE NEGÓCIO
    // -------------------------

    /**
     * Adiciona um item à lista de itens do pedido.
     * A lista é gerenciada internamente — o código externo não acessa 'itens' diretamente.
     */
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    /**
     * Calcula o menor preço entre todas as pizzas no pedido.
     * Usado para a promoção COMBO (a mais barata sai de graça).
     */
    private double getMenorPreco() {
        return itens.stream()
                .mapToDouble(i -> i.getPizza().getPreco())
                .min()
                .orElse(0.0);
    }

    /**
     * Calcula o subtotal (soma de todos os itens, sem desconto).
     */
    public double calcularSubtotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Calcula o total final com desconto da promoção (se houver).
     */
    public double calcularTotal() {
        double subtotal = calcularSubtotal();
        if (promocaoAplicada == null) return subtotal;

        double desconto = promocaoAplicada.calcularDesconto(subtotal, getMenorPreco());
        return subtotal - desconto;
    }

    /**
     * Calcula o valor do desconto aplicado (para exibição).
     */
    public double calcularDesconto() {
        if (promocaoAplicada == null) return 0.0;
        return promocaoAplicada.calcularDesconto(calcularSubtotal(), getMenorPreco());
    }

    // -------------------------
    // GETTERS
    // -------------------------
    public int getId()                      { return id; }
    public Cliente getCliente()             { return cliente; }
    public List<ItemPedido> getItens()      { return itens; }
    public TipoPedido getTipo()             { return tipo; }
    public StatusPedido getStatus()         { return status; }
    public Promocao getPromocaoAplicada()   { return promocaoAplicada; }
    public LocalDateTime getDataHora()      { return dataHora; }
    public String getEnderecoEntrega()      { return enderecoEntrega; }

    // -------------------------
    // SETTERS
    // -------------------------
    public void setStatus(StatusPedido status)           { this.status = status; }
    public void setPromocaoAplicada(Promocao promocao)   { this.promocaoAplicada = promocao; }
    public void setEnderecoEntrega(String endereco)      { this.enderecoEntrega = endereco; }

    /**
     * Exibe o pedido como um "comprovante" formatado no console.
     */
    public String gerarComprovante() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append(String.format("  PEDIDO #%d — %s\n", id, tipo));
        sb.append(String.format("  Data: %s\n", dataHora.format(fmt)));
        sb.append(String.format("  Cliente: %s\n", cliente.getNome()));
        if (tipo == TipoPedido.DELIVERY) {
            sb.append(String.format("  Entrega: %s\n", enderecoEntrega));
        }
        sb.append("----------------------------------------\n");
        for (ItemPedido item : itens) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("----------------------------------------\n");
        sb.append(String.format("  Subtotal:  R$ %.2f\n", calcularSubtotal()));
        if (promocaoAplicada != null) {
            sb.append(String.format("  Promoção:  %s (-R$ %.2f)\n",
                    promocaoAplicada.getNome(), calcularDesconto()));
        }
        sb.append(String.format("  TOTAL:     R$ %.2f\n", calcularTotal()));
        sb.append(String.format("  Status:    %s\n", status));
        sb.append("========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("[#%d] %s | %s | %s | R$ %.2f | %s",
                id, cliente.getNome(), tipo, dataHora.format(fmt), calcularTotal(), status);
    }
}
