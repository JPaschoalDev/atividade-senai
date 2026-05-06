package view;

import model.Item;
import service.EstoqueService;
import java.util.Scanner;

/**
 * Interface de usuário do sistema de gestão de estoque via console.
 *
 * <p>Responsável exclusivamente por interagir com o usuário: exibir menus,
 * ler entradas e apresentar resultados. Não contém nenhuma regra de negócio
 * — toda lógica é delegada ao {@link EstoqueService}.</p>
 *
 * <p>Essa separação garante que a interface possa ser substituída
 * (por exemplo, por uma UI gráfica) sem impactar as demais camadas.</p>
 *
 * @author João Victor Paschoal
 * @version 2.0
 */
public class Menu {

    /** Canal de leitura da entrada do usuário via console. */
    private final Scanner scanner = new Scanner(System.in);

    /** Serviço que contém todas as regras de negócio do estoque. */
    private final EstoqueService service;

    /**
     * Constrói o menu injetando o serviço que será utilizado.
     *
     * @param service serviço de estoque com as regras de negócio
     */
    public Menu(EstoqueService service) {
        this.service = service;
    }

    /**
     * Inicia o loop principal do menu, exibindo as opções disponíveis
     * e redirecionando para o método correspondente à escolha do usuário.
     *
     * <p>O loop continua até que o usuário selecione a opção 0 (Sair).</p>
     */
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n--- SISTEMA DE GESTÃO DE ESTOQUE ---");
            System.out.println("[1] CADASTRAR ITENS");
            System.out.println("[2] BUSCAR ITEM POR ID");
            System.out.println("[3] MOVIMENTAÇÃO (ADICIONAR/REMOVER)");
            System.out.println("[4] LISTAR TODOS OS ITENS");
            System.out.println("[5] DASHBOARD FINANCEIRO");
            System.out.println("[0] SAIR");
            System.out.print("OPÇÃO: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> cadastrarItem();
                case 2 -> buscarItem();
                case 3 -> movimentarEstoque();
                case 4 -> listarItens();
                case 5 -> exibirFinanceiro();
                case 0 -> System.out.println("ENCERRANDO SISTEMA...");
                default -> System.out.println("OPÇÃO INVÁLIDA");
            }
        } while (opcao != 0);
    }

    /**
     * Coleta os dados do novo item com o usuário e delega o cadastro ao serviço.
     *
     * <p>Exibe o resultado retornado pelo serviço — sucesso ou descrição do erro.</p>
     */
    private void cadastrarItem() {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer após nextInt()

        System.out.print("NOME: ");
        String nome = scanner.nextLine();

        System.out.print("QUANTIDADE INICIAL: ");
        int qtd = scanner.nextInt();

        System.out.print("PREÇO: ");
        double preco = scanner.nextDouble();

        // Delega toda a validação e lógica ao serviço
        String resultado = service.cadastrarItem(id, nome, qtd, preco);
        System.out.println(resultado);

        aguardarEnter();
    }

    /**
     * Solicita um ID ao usuário e exibe o item correspondente, se encontrado.
     */
    private void buscarItem() {
        System.out.print("DIGITE O ID DO ITEM: ");
        int id = scanner.nextInt();

        Item item = service.buscarPorId(id);

        if (item != null) {
            System.out.println("ITEM ENCONTRADO: " + item);
            aguardarEnter();
        } else {
            System.out.println("ITEM NÃO LOCALIZADO.");
        }
    }

    /**
     * Conduz o fluxo de movimentação de estoque (entrada ou saída).
     *
     * <p>Localiza o item pelo ID, exibe as opções de operação e
     * delega o registro ao serviço, exibindo o resultado ao usuário.</p>
     */
    private void movimentarEstoque() {
        System.out.println("\n--- MOVIMENTAÇÃO DE ESTOQUE ---");
        System.out.print("DIGITE O ID DO ITEM: ");
        int id = scanner.nextInt();

        Item item = service.buscarPorId(id);

        if (item == null) {
            System.out.println("ITEM COM ID " + id + " NÃO ENCONTRADO.");
            aguardarEnter();
            return;
        }

        System.out.println("ITEM SELECIONADO: " + item.getNome());
        System.out.println("QUANTIDADE ATUAL: " + item.getQuantidade());
        System.out.println("\n[1] ENTRADA (SOMA +)");
        System.out.println("[2] SAÍDA (SUBTRAÇÃO -)");
        System.out.print("ESCOLHA A OPERAÇÃO: ");
        int operacao = scanner.nextInt();

        System.out.print("DIGITE A QUANTIDADE A SER MOVIMENTADA: ");
        int qtd = scanner.nextInt();

        // Delega ao serviço e exibe o resultado retornado
        String resultado = switch (operacao) {
            case 1 -> service.registrarEntrada(item, qtd);
            case 2 -> service.registrarSaida(item, qtd);
            default -> "OPÇÃO DE OPERAÇÃO INVÁLIDA.";
        };

        System.out.println(resultado);
        aguardarEnter();
    }

    /**
     * Exibe todos os itens cadastrados no estoque em formato de relatório.
     */
    private void listarItens() {
        System.out.println("\n--- RELATÓRIO DE ESTOQUE ---");

        if (service.estoqueVazio()) {
            System.out.println("ESTOQUE VAZIO.");
            return;
        }

        for (Item item : service.listarTodos()) {
            System.out.println(item);
        }

        aguardarEnter();
    }

    /**
     * Exibe o dashboard financeiro consolidado da sessão atual.
     *
     * <p>Apresenta o valor em estoque por item e os indicadores globais:
     * entradas, saídas, valor patrimonial e balanço da sessão.</p>
     */
    private void exibirFinanceiro() {
        System.out.println("\n========= DETALHAMENTO CONTÁBIL =========");
        System.out.printf("%-15s | %-10s | %-15s%n", "ITEM", "QTD", "VALOR EM ESTOQUE");
        System.out.println("------------------------------------------");

        for (Item item : service.listarTodos()) {
            double valorNoEstoque = item.getQuantidade() * item.getPreco();
            System.out.printf("%-15s | %-10d | R$ %.2f%n",
                    item.getNome(), item.getQuantidade(), valorNoEstoque);
        }

        double balanco = service.getSaldoSaidas() - service.getSaldoEntradas();

        System.out.println("------------------------------------------");
        System.out.printf("📥 TOTAL ACUMULADO DE ENTRADAS: R$ %.2f\n", service.getSaldoEntradas());
        System.out.printf("📤 TOTAL ACUMULADO DE SAÍDAS : R$ %.2f\n", service.getSaldoSaidas());
        System.out.printf("💰 VALOR PATRIMONIAL ATUAL   : R$ %.2f\n", service.calcularValorPatrimonial());
        System.out.printf("📊 BALANÇO (SAÍDAS - ENTRADAS): R$ %.2f\n", balanco);
        System.out.println("==========================================");

        aguardarEnter();
    }

    /**
     * Pausa a execução e aguarda o usuário pressionar Enter para continuar.
     *
     * <p>Método utilitário usado ao final de cada operação para evitar que
     * a tela seja sobrescrita antes que o usuário leia o resultado.</p>
     */
    private void aguardarEnter() {
        System.out.print("\nPRESSIONE ENTER PARA CONTINUAR...");
        scanner.nextLine();
        scanner.nextLine();
    }
}
