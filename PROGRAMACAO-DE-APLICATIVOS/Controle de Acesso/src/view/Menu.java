package view;

import controller.ControleAcessoController;
import model.Cargo;
import model.Usuario;

import java.util.InputMismatchException;
import java.util.Scanner;

// Declaração de classe, atributos privados para o view funcionar
public class Menu {
    private ControleAcessoController controller; // Conexão direta com o cérebro do sistema
    private Scanner scanner; // Leitor de teclado
    private Usuario usuarioLogado; // Guarda o usuário que passou pelo login com sucesso

    // Construtor: Recebe o "cérebro" (controller) criado no Main
    public Menu(ControleAcessoController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.usuarioLogado = null; // Ninguém começa logado
    }

    // Método principal que roda o loop do menu principal
    public void exibirMenuPrincipal() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===== SISTEMA DE CONTROLE DE ACESSO =====");
            if (usuarioLogado != null) {
                System.out.println("Sessão ativa: " + usuarioLogado.getNome() + " [" + usuarioLogado.getCargo() + "]");
            } else {
                System.out.println("Nenhum usuário logado. (Acesso Restrito)");
            }
            System.out.println("-----------------------------------------");
            System.out.println("1 - Fazer Login");
            System.out.println("2 - Tentar Acessar uma Área");
            System.out.println("3 - Cadastrar Novo Usuário (Apenas Admin)");
            System.out.println("4 - Consultar Histórico Completo (Apenas Admin)");
            System.out.println("5 - Fazer Logout");
            System.out.println("0 - Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            // TRATAMENTO DE ERROS (Evita que o programa feche se digitarem letras)
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado
                processarOpcao(opcao);
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31m" + "ERRO: Por favor, digite apenas números válidos." + "\u001B[0m");
                scanner.nextLine(); // Limpa o erro do teclado para não travar o loop
            }
        }
    }

    // Direciona a opção escolhida para o método correto.
    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1: menuLogin(); break;
            case 2: menuTentarAcesso(); break;
            case 3: menuCadastrarUsuario(); break;
            case 4: controller.exibirHistoricoCompleto(usuarioLogado); break;
            case 5:
                usuarioLogado = null;
                System.out.println("Logout efetuado com sucesso.");
                break;
            case 0: System.out.println("Desligando o sistema... Até logo!"); break;
            default: System.out.println("\u001B[31m" + "Opção inválida! Tente novamente." + "\u001B[0m");
        }
    }

    // Submenu para coletar dados de Login
    private void menuLogin() {
        System.out.println("\n--- TELA DE LOGIN ---");
        try {
            System.out.print("Digite o ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpa buffer

            System.out.print("Digite a Senha: ");
            String senha = scanner.nextLine();

            // Pede para o controller validar "iD" e "senha"
            Usuario u = controller.fazerLogin(id, senha);

            if (u != null) {
                usuarioLogado = u;
                System.out.println("Login realizado com sucesso! Bem-vindo, " + u.getNome());
            } else {
                System.out.println("\u001B[31m" + "Falha na autenticação: ID ou Senha incorretos." + "\u001B[0m");
            }
        } catch (InputMismatchException e) {
            System.out.println("\u001B[31m" + "ERRO: ID precisa ser um número." + "\u001B[0m");
            scanner.nextLine();
        }
    }

    // Submenu para simular a passagem em uma porta/roleta
    private void menuTentarAcesso() {
        if (usuarioLogado == null) {
            System.out.println("\u001B[31m" + "ERRO: Você precisa fazer login antes de tentar acessar uma área." + "\u001B[0m");
            return;
        }

        System.out.println("\n--- TESTAR ENTRADA EM ÁREA ---");
        System.out.println("Áreas sugeridas: Recepção, Almoxarifado, Sala de Servidores");
        System.out.print("Digite o nome da área que deseja entrar: ");
        String area = scanner.nextLine();

        // Aciona o Controller para decidir e registrar
        boolean autorizado = controller.tentarAcesso(usuarioLogado, area);

        if (autorizado) {
            System.out.println("🟢 [ACESSO LIBERADO] Porta aberta para a área: " + area);
        } else {
            System.out.println("\u001B[31m" + "[ACESSO NEGADO] Você não tem permissão para entrar na área: " + area + "\u001B[0m");
        }
    }

    // Submenu para o Admin cadastrar novas pessoas
    private void menuCadastrarUsuario() {
        if (usuarioLogado == null) {
            System.out.println("\u001B[31m" + "ERRO: Faça login como Administrador primeiro." + "\u001B[0m");
            return;
        }

        System.out.println("\n--- CADASTRAR NOVO USUÁRIO ---");
        try {
            System.out.print("Digite o novo ID (número): ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nome do funcionário: ");
            String nome = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Senha de acesso: ");
            String senha = scanner.nextLine();

            System.out.println("Escolha o nível de acesso:");
            System.out.println("1 - ADMINISTRADOR | 2 - FUNCIONARIO | 3 - VISITANTE");
            System.out.print("Opção: ");
            int escolhaCargo = scanner.nextInt();
            scanner.nextLine();

            Cargo cargoEscolhido = Cargo.VISITANTE; // Padrão mais seguro se errar
            if (escolhaCargo == 1) cargoEscolhido = Cargo.ADMINISTRADOR;
            if (escolhaCargo == 2) cargoEscolhido = Cargo.FUNCIONARIO;

            // Cria o objeto modelo provisório
            Usuario novo = new Usuario(id, nome, email, senha, cargoEscolhido);

            // Envia para o controller aplicar a regra e salvar se puder
            controller.cadastrarUsuario(usuarioLogado, novo);

        } catch (InputMismatchException e) {
            System.out.println("\u001B[31m" + "ERRO: Dados inválidos digitados. Cadastro cancelado." + "\u001B[0m");

            scanner.nextLine();
        }
    }
}
