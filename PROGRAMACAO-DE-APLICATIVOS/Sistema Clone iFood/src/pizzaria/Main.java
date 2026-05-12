package pizzaria;

import pizzaria.gerenciador.*;
import pizzaria.modelo.*;
import pizzaria.modelo.Pedido.TipoPedido;
import pizzaria.modelo.Pedido.StatusPedido;
import pizzaria.modelo.Promocao.TipoPromocao;
import pizzaria.util.Console;

import java.time.LocalDate;
import java.util.List;

/**
 * CLASSE: Main
 *
 * Ponto de entrada da aplicação. Contém todos os menus e a lógica de navegação.
 *
 * CONCEITO POO APLICADO:
 *   - Main é o "orquestrador": cria os gerenciadores e os conecta.
 *   - Cada gerenciador é um objeto independente, com responsabilidade única.
 *   - Main não conhece os detalhes internos de cada gerenciador — só chama os métodos públicos.
 *
 * ESTRUTURA DO MAIN:
 *   main()                → menu principal
 *   menuClientes()        → submenu de clientes
 *   menuCardapio()        → submenu do cardápio
 *   menuPedidos()         → submenu de pedidos
 *   menuFinanceiro()      → submenu financeiro
 *   menuPromocoes()       → submenu de promoções
 *   fluxoCriarPedido()    → fluxo completo de criação de um pedido
 */
public class Main {

    // -------------------------
    // GERENCIADORES (objetos principais do sistema)
    // Criados uma vez aqui e passados para quem precisar.
    // -------------------------
    static GerenciadorClientes gerClientes   = new GerenciadorClientes();
    static GerenciadorPedidos  gerPedidos    = new GerenciadorPedidos();
    static Cardapio            cardapio      = new Cardapio();
    static GerenciadorPromocoes gerPromocoes = new GerenciadorPromocoes();
    static GerenciadorFinanceiro gerFinanceiro = new GerenciadorFinanceiro(gerPedidos);

    // -------------------------
    // PONTO DE ENTRADA
    // -------------------------
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   🍕  SISTEMA PIZZARIA v1.0     ║");
        System.out.println("╚══════════════════════════════════╝");

        boolean executando = true;

        while (executando) {
            Console.titulo("MENU PRINCIPAL");
            System.out.println("  1. Clientes");
            System.out.println("  2. Cardápio");
            System.out.println("  3. Pedidos");
            System.out.println("  4. Promoções");
            System.out.println("  5. Financeiro");
            System.out.println("  0. Sair");
            Console.separador();

            int opcao = Console.lerInt("Escolha: ");

            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuCardapio();
                case 3 -> menuPedidos();
                case 4 -> menuPromocoes();
                case 5 -> menuFinanceiro();
                case 0 -> {
                    System.out.println("\nAté logo! 🍕");
                    executando = false;
                }
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    // =========================================================
    // MENU: CLIENTES
    // =========================================================
    static void menuClientes() {
        boolean voltar = false;
        while (!voltar) {
            Console.titulo("CLIENTES");
            System.out.println("  1. Listar todos");
            System.out.println("  2. Cadastrar novo cliente");
            System.out.println("  3. Buscar por nome");
            System.out.println("  4. Editar cliente");
            System.out.println("  5. Remover cliente");
            System.out.println("  0. Voltar");
            Console.separador();

            switch (Console.lerInt("Escolha: ")) {
                case 1 -> {
                    List<Cliente> todos = gerClientes.listarTodos();
                    if (todos.isEmpty()) {
                        System.out.println("  Nenhum cliente cadastrado.");
                    } else {
                        todos.forEach(System.out::println);
                    }
                    Console.pausar();
                }
                case 2 -> {
                    String nome     = Console.lerTexto("Nome: ");
                    String tel      = Console.lerTexto("Telefone: ");
                    String endereco = Console.lerTexto("Endereço: ");
                    Cliente c = gerClientes.cadastrar(nome, tel, endereco);
                    System.out.println("  ✔ Cliente cadastrado: " + c);
                    Console.pausar();
                }
                case 3 -> {
                    String busca = Console.lerTexto("Fragmento do nome: ");
                    List<Cliente> resultado = gerClientes.buscarPorNome(busca);
                    if (resultado.isEmpty()) {
                        System.out.println("  Nenhum cliente encontrado.");
                    } else {
                        resultado.forEach(System.out::println);
                    }
                    Console.pausar();
                }
                case 4 -> {
                    int id = Console.lerInt("ID do cliente: ");
                    Cliente c = gerClientes.buscarPorId(id);
                    if (c == null) {
                        System.out.println("  ⚠ Cliente não encontrado.");
                    } else {
                        System.out.println("  Cliente atual: " + c);
                        String nome     = Console.lerTexto("Novo nome (ENTER para manter): ");
                        String tel      = Console.lerTexto("Novo telefone (ENTER para manter): ");
                        String endereco = Console.lerTexto("Novo endereço (ENTER para manter): ");
                        gerClientes.atualizar(id, nome, tel, endereco);
                        System.out.println("  ✔ Atualizado: " + gerClientes.buscarPorId(id));
                    }
                    Console.pausar();
                }
                case 5 -> {
                    int id = Console.lerInt("ID do cliente a remover: ");
                    boolean ok = gerClientes.remover(id);
                    System.out.println(ok ? "  ✔ Removido." : "  ⚠ ID não encontrado.");
                    Console.pausar();
                }
                case 0 -> voltar = true;
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    // =========================================================
    // MENU: CARDÁPIO
    // =========================================================
    static void menuCardapio() {
        boolean voltar = false;
        while (!voltar) {
            Console.titulo("CARDÁPIO");
            System.out.println("  1. Listar cardápio completo");
            System.out.println("  2. Adicionar pizza");
            System.out.println("  3. Desativar pizza");
            System.out.println("  4. Ativar pizza");
            System.out.println("  0. Voltar");
            Console.separador();

            switch (Console.lerInt("Escolha: ")) {
                case 1 -> {
                    System.out.println();
                    cardapio.listarTodas().forEach(System.out::println);
                    Console.pausar();
                }
                case 2 -> {
                    String nome     = Console.lerTexto("Nome da pizza: ");
                    String desc     = Console.lerTexto("Ingredientes: ");
                    double preco    = Console.lerDouble("Preço (R$): ");
                    String tamanho  = Console.lerTexto("Tamanho (P/M/G): ").toUpperCase();
                    Pizza p = cardapio.adicionar(nome, desc, preco, tamanho);
                    System.out.println("  ✔ Adicionada: " + p);
                    Console.pausar();
                }
                case 3 -> {
                    int id = Console.lerInt("ID da pizza para desativar: ");
                    boolean ok = cardapio.desativar(id);
                    System.out.println(ok ? "  ✔ Pizza desativada." : "  ⚠ ID não encontrado.");
                    Console.pausar();
                }
                case 4 -> {
                    int id = Console.lerInt("ID da pizza para ativar: ");
                    boolean ok = cardapio.ativar(id);
                    System.out.println(ok ? "  ✔ Pizza ativada." : "  ⚠ ID não encontrado.");
                    Console.pausar();
                }
                case 0 -> voltar = true;
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    // =========================================================
    // MENU: PEDIDOS
    // =========================================================
    static void menuPedidos() {
        boolean voltar = false;
        while (!voltar) {
            Console.titulo("PEDIDOS");
            System.out.println("  1. Novo pedido");
            System.out.println("  2. Listar pedidos em aberto");
            System.out.println("  3. Ver comprovante de um pedido");
            System.out.println("  4. Atualizar status de um pedido");
            System.out.println("  5. Ver pedidos de um cliente");
            System.out.println("  0. Voltar");
            Console.separador();

            switch (Console.lerInt("Escolha: ")) {
                case 1 -> fluxoCriarPedido();
                case 2 -> {
                    List<Pedido> abertos = gerPedidos.listarAbertos();
                    if (abertos.isEmpty()) {
                        System.out.println("  Nenhum pedido em aberto.");
                    } else {
                        abertos.forEach(System.out::println);
                    }
                    Console.pausar();
                }
                case 3 -> {
                    int id = Console.lerInt("ID do pedido: ");
                    Pedido p = gerPedidos.buscarPorId(id);
                    if (p == null) {
                        System.out.println("  ⚠ Pedido não encontrado.");
                    } else {
                        System.out.println(p.gerarComprovante());
                    }
                    Console.pausar();
                }
                case 4 -> {
                    int id = Console.lerInt("ID do pedido: ");
                    Pedido p = gerPedidos.buscarPorId(id);
                    if (p == null) {
                        System.out.println("  ⚠ Pedido não encontrado.");
                    } else {
                        System.out.println("  Status atual: " + p.getStatus());
                        System.out.println("  1. ABERTO | 2. EM_PREPARO | 3. PRONTO | 4. ENTREGUE | 5. CANCELADO");
                        int s = Console.lerInt("Novo status (1-5): ");
                        StatusPedido[] statuses = StatusPedido.values();
                        if (s >= 1 && s <= statuses.length) {
                            gerPedidos.atualizarStatus(id, statuses[s - 1]);
                            System.out.println("  ✔ Status atualizado para: " + statuses[s - 1]);
                        } else {
                            System.out.println("  ⚠ Opção inválida.");
                        }
                    }
                    Console.pausar();
                }
                case 5 -> {
                    int cid = Console.lerInt("ID do cliente: ");
                    List<Pedido> historico = gerPedidos.buscarPorCliente(cid);
                    if (historico.isEmpty()) {
                        System.out.println("  Nenhum pedido encontrado para esse cliente.");
                    } else {
                        historico.forEach(System.out::println);
                    }
                    Console.pausar();
                }
                case 0 -> voltar = true;
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    /**
     * FLUXO: Criar Pedido
     *
     * Guia o atendente pelo processo completo de criação de um pedido:
     * 1. Escolher cliente
     * 2. Escolher tipo (DELIVERY/BALCAO)
     * 3. Adicionar pizzas
     * 4. Aplicar promoção (opcional)
     * 5. Confirmar e exibir comprovante
     */
    static void fluxoCriarPedido() {
        Console.titulo("NOVO PEDIDO");

        // PASSO 1: Selecionar cliente
        System.out.println("--- Clientes cadastrados ---");
        gerClientes.listarTodos().forEach(System.out::println);
        int clienteId = Console.lerInt("\nID do cliente: ");
        Cliente cliente = gerClientes.buscarPorId(clienteId);
        if (cliente == null) {
            System.out.println("  ⚠ Cliente não encontrado.");
            Console.pausar();
            return;  // Aborta o fluxo
        }

        // PASSO 2: Tipo do pedido
        System.out.println("\n  1. Balcão (retirada)  |  2. Delivery");
        int tipoOpc = Console.lerInt("Tipo do pedido: ");
        TipoPedido tipo = (tipoOpc == 2) ? TipoPedido.DELIVERY : TipoPedido.BALCAO;

        // Cria o pedido (ainda sem itens)
        Pedido pedido = gerPedidos.criarPedido(cliente, tipo);

        // Se delivery, confirma o endereço
        if (tipo == TipoPedido.DELIVERY) {
            System.out.println("  Endereço cadastrado: " + cliente.getEndereco());
            String novoEnd = Console.lerTexto("Novo endereço (ENTER para usar o cadastrado): ");
            if (!novoEnd.isEmpty()) {
                pedido.setEnderecoEntrega(novoEnd);
            }
        }

        // PASSO 3: Adicionar pizzas
        boolean adicionando = true;
        while (adicionando) {
            Console.separador();
            System.out.println("--- Cardápio disponível ---");
            cardapio.listarDisponiveis().forEach(System.out::println);
            System.out.println("\n  (0 para finalizar os itens)");

            int pizzaId = Console.lerInt("ID da pizza: ");
            if (pizzaId == 0) {
                // Verifica se ao menos um item foi adicionado
                if (pedido.getItens().isEmpty()) {
                    System.out.println("  ⚠ Adicione pelo menos uma pizza!");
                } else {
                    adicionando = false;
                }
                continue;
            }

            Pizza pizza = cardapio.buscarPorId(pizzaId);
            if (pizza == null || !pizza.isDisponivel()) {
                System.out.println("  ⚠ Pizza inválida ou indisponível.");
                continue;
            }

            int qtd = Console.lerInt("Quantidade: ");
            String obs = Console.lerTexto("Observação (ENTER para nenhuma): ");

            // Cria o ItemPedido (composição: item "tem" uma pizza)
            ItemPedido item = new ItemPedido(pizza, qtd, obs);
            pedido.adicionarItem(item);
            System.out.printf("  ✔ Adicionado: %dx %s — R$ %.2f%n",
                    qtd, pizza.getNome(), item.getSubtotal());
        }

        // PASSO 4: Aplicar promoção (opcional)
        List<Promocao> ativas = gerPromocoes.listarAtivas();
        if (!ativas.isEmpty()) {
            Console.separador();
            System.out.println("--- Promoções disponíveis ---");
            ativas.forEach(System.out::println);
            System.out.println("  (0 para não aplicar promoção)");
            int promId = Console.lerInt("ID da promoção: ");
            if (promId != 0) {
                Promocao prom = gerPromocoes.buscarPorId(promId);
                if (prom != null && prom.isAtiva()) {
                    // ATENÇÃO: o combo exige pelo menos 2 pizzas diferentes
                    if (prom.getTipo() == TipoPromocao.COMBO && pedido.getItens().size() < 2) {
                        System.out.println("  ⚠ Combo exige pelo menos 2 pizzas no pedido.");
                    } else {
                        gerPedidos.aplicarPromocao(pedido.getId(), prom);
                        System.out.println("  ✔ Promoção aplicada: " + prom.getNome());
                    }
                } else {
                    System.out.println("  ⚠ Promoção inválida.");
                }
            }
        }

        // PASSO 5: Exibir comprovante
        pedido.setStatus(StatusPedido.EM_PREPARO);
        System.out.println("\n" + pedido.gerarComprovante());
        Console.pausar();
    }

    // =========================================================
    // MENU: PROMOÇÕES
    // =========================================================
    static void menuPromocoes() {
        boolean voltar = false;
        while (!voltar) {
            Console.titulo("PROMOÇÕES");
            System.out.println("  1. Listar todas as promoções");
            System.out.println("  2. Cadastrar nova promoção");
            System.out.println("  3. Ativar promoção");
            System.out.println("  4. Desativar promoção");
            System.out.println("  0. Voltar");
            Console.separador();

            switch (Console.lerInt("Escolha: ")) {
                case 1 -> {
                    gerPromocoes.listarTodas().forEach(System.out::println);
                    Console.pausar();
                }
                case 2 -> {
                    String nome = Console.lerTexto("Nome da promoção: ");
                    System.out.println("  1. Desconto percentual  |  2. Combo (2x1)");
                    int tipoOpc = Console.lerInt("Tipo: ");
                    TipoPromocao tipo = (tipoOpc == 2) ? TipoPromocao.COMBO : TipoPromocao.PERCENTUAL;

                    double valor = 0;
                    if (tipo == TipoPromocao.PERCENTUAL) {
                        valor = Console.lerDouble("Percentual de desconto (ex: 10 para 10%): ");
                    }

                    Promocao p = gerPromocoes.cadastrar(nome, tipo, valor);
                    System.out.println("  ✔ Promoção cadastrada: " + p);
                    Console.pausar();
                }
                case 3 -> {
                    int id = Console.lerInt("ID da promoção: ");
                    System.out.println(gerPromocoes.ativar(id) ? "  ✔ Ativada." : "  ⚠ Não encontrada.");
                    Console.pausar();
                }
                case 4 -> {
                    int id = Console.lerInt("ID da promoção: ");
                    System.out.println(gerPromocoes.desativar(id) ? "  ✔ Desativada." : "  ⚠ Não encontrada.");
                    Console.pausar();
                }
                case 0 -> voltar = true;
                default -> System.out.println("  ⚠ Opção inválida.");
            }
        }
    }

    // =========================================================
    // MENU: FINANCEIRO
    // =========================================================
    static void menuFinanceiro() {
        Console.titulo("RELATÓRIO FINANCEIRO");

        // Usa a data de hoje como referência padrão
        LocalDate hoje = LocalDate.now();
        System.out.println(gerFinanceiro.gerarRelatorio(hoje));

        System.out.println("  Dica: para ver o relatório de outra data,");
        System.out.println("  altere 'LocalDate.now()' no código (exercício!).\n");

        // Exibe todos os pedidos do sistema para referência
        Console.separador();
        System.out.println("Todos os pedidos registrados:");
        List<Pedido> todos = gerPedidos.listarTodos();
        if (todos.isEmpty()) {
            System.out.println("  Nenhum pedido ainda.");
        } else {
            todos.forEach(System.out::println);
        }

        Console.pausar();
    }
}
