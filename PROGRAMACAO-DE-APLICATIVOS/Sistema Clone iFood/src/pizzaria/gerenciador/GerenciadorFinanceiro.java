package pizzaria.gerenciador;

import pizzaria.modelo.Pedido;
import pizzaria.modelo.Pedido.StatusPedido;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CLASSE: GerenciadorFinanceiro
 *
 * Analisa os pedidos e gera relatórios financeiros.
 *
 * CONCEITO POO APLICADO:
 *   - Esta classe não armazena pedidos — ela RECEBE a lista e faz cálculos.
 *   - Separação de responsabilidades: pedidos são gerenciados por GerenciadorPedidos;
 *     análise financeira fica aqui.
 *   - Métodos utilitários que filtram e somam listas de objetos.
 *
 * REGRA DE NEGÓCIO:
 *   - Apenas pedidos com status ENTREGUE contam para o faturamento.
 */
public class GerenciadorFinanceiro {

    // Referência ao gerenciador de pedidos (não duplicamos a lista)
    private GerenciadorPedidos gerenciadorPedidos;

    public GerenciadorFinanceiro(GerenciadorPedidos gerenciadorPedidos) {
        this.gerenciadorPedidos = gerenciadorPedidos;
    }

    // -------------------------
    // RELATÓRIOS
    // -------------------------

    /**
     * Calcula o faturamento de um dia específico.
     *
     * @param data Data alvo (ex: LocalDate.now() para hoje)
     * @return Soma dos totais de todos os pedidos entregues nessa data
     */
    public double faturamentoDiario(LocalDate data) {
        double total = 0;
        for (Pedido p : gerenciadorPedidos.listarTodos()) {
            if (pedidoFoiEntregueEm(p, data)) {
                total += p.calcularTotal();
            }
        }
        return total;
    }

    /**
     * Calcula o faturamento dos últimos 7 dias a partir de uma data.
     */
    public double faturamentoSemanal(LocalDate dataFim) {
        double total = 0;
        // Itera sobre os 7 dias anteriores (incluindo o dia final)
        for (int i = 6; i >= 0; i--) {
            LocalDate dia = dataFim.minusDays(i);
            total += faturamentoDiario(dia);
        }
        return total;
    }

    /**
     * Calcula o faturamento de um ano inteiro (mês a mês).
     *
     * @param ano Ano alvo (ex: 2024)
     * @return Soma total do ano
     */
    public double faturamentoAnual(int ano) {
        double total = 0;
        for (Pedido p : gerenciadorPedidos.listarTodos()) {
            if (p.getStatus() == StatusPedido.ENTREGUE &&
                p.getDataHora().getYear() == ano) {
                total += p.calcularTotal();
            }
        }
        return total;
    }

    /**
     * Conta quantos pedidos foram entregues em um determinado dia.
     */
    public int totalPedidosDia(LocalDate data) {
        int count = 0;
        for (Pedido p : gerenciadorPedidos.listarTodos()) {
            if (pedidoFoiEntregueEm(p, data)) count++;
        }
        return count;
    }

    /**
     * Calcula o ticket médio dos pedidos entregues no dia.
     * Ticket médio = faturamento / número de pedidos.
     */
    public double ticketMedioDia(LocalDate data) {
        int total = totalPedidosDia(data);
        if (total == 0) return 0;
        return faturamentoDiario(data) / total;
    }

    /**
     * Gera um relatório completo em texto para exibição no console.
     */
    public String gerarRelatorio(LocalDate data) {
        int ano = data.getYear();
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔══════════════════════════════════════╗\n");
        sb.append("║       RELATÓRIO FINANCEIRO           ║\n");
        sb.append("╠══════════════════════════════════════╣\n");
        sb.append(String.format("║  Data de referência: %-17s║\n", data.toString()));
        sb.append("╠══════════════════════════════════════╣\n");
        sb.append(String.format("║  Faturamento HOJE:    R$ %10.2f ║\n", faturamentoDiario(data)));
        sb.append(String.format("║  Pedidos hoje:        %13d ║\n", totalPedidosDia(data)));
        sb.append(String.format("║  Ticket médio hoje:   R$ %10.2f ║\n", ticketMedioDia(data)));
        sb.append("╠══════════════════════════════════════╣\n");
        sb.append(String.format("║  Faturamento SEMANAL: R$ %10.2f ║\n", faturamentoSemanal(data)));
        sb.append("╠══════════════════════════════════════╣\n");
        sb.append(String.format("║  Faturamento ANUAL %d: R$ %10.2f ║\n", ano, faturamentoAnual(ano)));
        sb.append("╚══════════════════════════════════════╝\n");
        return sb.toString();
    }

    // -------------------------
    // MÉTODO AUXILIAR (PRIVATE)
    // -------------------------

    /**
     * Verifica se um pedido foi entregue em uma data específica.
     * É 'private' pois é um detalhe de implementação interno desta classe.
     */
    private boolean pedidoFoiEntregueEm(Pedido p, LocalDate data) {
        if (p.getStatus() != StatusPedido.ENTREGUE) return false;
        LocalDate dataPedido = p.getDataHora().toLocalDate();
        return dataPedido.equals(data);
    }
}
