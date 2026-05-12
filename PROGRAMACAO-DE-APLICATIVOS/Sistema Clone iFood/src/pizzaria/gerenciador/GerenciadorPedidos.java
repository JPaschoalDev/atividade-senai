package pizzaria.gerenciador;

import pizzaria.modelo.*;
import pizzaria.modelo.Pedido.TipoPedido;
import pizzaria.modelo.Pedido.StatusPedido;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASSE: GerenciadorPedidos
 *
 * Responsável por criar, listar e atualizar pedidos.
 *
 * CONCEITO POO APLICADO:
 *   - Depende de outros objetos (Cliente, Pizza, Promocao) sem herdar deles.
 *   - Método fábrica (criarPedido): centraliza a criação de pedidos complexos.
 *   - Filtragem de listas com critérios de negócio (buscarPorCliente, buscarAbertos).
 */
public class GerenciadorPedidos {

    private List<Pedido> pedidos;
    private int proximoId;

    public GerenciadorPedidos() {
        this.pedidos = new ArrayList<>();
        this.proximoId = 1;
    }

    // -------------------------
    // CRIAÇÃO DE PEDIDO
    // -------------------------

    /**
     * Cria um novo pedido vazio para um cliente.
     * Os itens são adicionados depois com adicionarItem().
     *
     * @param cliente Cliente que está fazendo o pedido
     * @param tipo    DELIVERY ou BALCAO
     * @return        O pedido criado (para adicionar itens em seguida)
     */
    public Pedido criarPedido(Cliente cliente, TipoPedido tipo) {
        Pedido novo = new Pedido(proximoId++, cliente, tipo);
        pedidos.add(novo);
        return novo;
    }

    /**
     * Adiciona um item (pizza + quantidade) a um pedido existente.
     *
     * @param pedidoId  ID do pedido
     * @param item      ItemPedido a ser adicionado
     * @return true se o pedido foi encontrado e o item adicionado
     */
    public boolean adicionarItem(int pedidoId, ItemPedido item) {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null) return false;
        pedido.adicionarItem(item);
        return true;
    }

    /**
     * Aplica uma promoção a um pedido.
     * Um pedido só pode ter uma promoção por vez.
     */
    public boolean aplicarPromocao(int pedidoId, Promocao promocao) {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null || !promocao.isAtiva()) return false;
        pedido.setPromocaoAplicada(promocao);
        return true;
    }

    /**
     * Atualiza o status de um pedido (ex: ABERTO → EM_PREPARO → PRONTO → ENTREGUE).
     */
    public boolean atualizarStatus(int pedidoId, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(pedidoId);
        if (pedido == null) return false;
        pedido.setStatus(novoStatus);
        return true;
    }

    // -------------------------
    // BUSCAS E FILTROS
    // -------------------------

    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Lista todos os pedidos de um cliente específico.
     */
    public List<Pedido> buscarPorCliente(int clienteId) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getCliente().getId() == clienteId) resultado.add(p);
        }
        return resultado;
    }

    /**
     * Lista pedidos que ainda não foram entregues/cancelados.
     */
    public List<Pedido> listarAbertos() {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getStatus() != StatusPedido.ENTREGUE &&
                p.getStatus() != StatusPedido.CANCELADO) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }

    public int getTotalPedidos() {
        return pedidos.size();
    }
}
