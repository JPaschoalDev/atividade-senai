package controller;

import model.Cargo;
import model.RegistroAcesso;
import model.Usuario;

import java.util.ArrayList;
import java.util.List;

// Funciona como o cérebro do nosso sistema, toda a lógica é aplciada aqui
public class ControleAcessoController {

    // Banco de dados na memória usando Listas (ArrayList)
    private List<Usuario> listaUsuarios;
    private List<RegistroAcesso> historicoAcessos;

    // Construtor: Inicializa as listas e cria alguns usuários de teste
    public ControleAcessoController() {
        this.listaUsuarios = new ArrayList<>();
        this.historicoAcessos = new ArrayList<>();
        inicializarDadosTeste();
    }

    // Método para o Administrador cadastrar usuários
    // Recebe quem está tentando cadastrar (admin) e quem será cadastrado (novoUsuario)
    public void cadastrarUsuario(Usuario admin, Usuario novoUsuario) {
        if (admin.getCargo() == Cargo.ADMINISTRADOR) {
            listaUsuarios.add(novoUsuario);
            System.out.println("Usuário '" + novoUsuario.getNome() + "' cadastrado com sucesso!");
        } else {
            System.out.println("\u001B[31m" + "ERRO: Apenas administradores podem cadastrar usuários." + "\u001B[0m");
        }
    }

    // Método de Login / Autenticação
    // Recebe o iD e a senha digitadas e tenta encontrar um "Usuario" correspondente
    public Usuario fazerLogin(int id, String senhaDigitada) {
        for (Usuario u : listaUsuarios) { // "for-each" percorre a lista de usuários um a um.
            // Se achar o ID e a senha bater
            if (u.getId() == id && u.validarSenha(senhaDigitada)) {
                return u; // Login feito com sucesso, devolve o usuário logado
            }
        }
        return null; // Se não achar ou a senha estiver errada
    }

    // Validar a entrada na Área e Salvar no Histórico
    // Recebe o usuário que está na porta e o nome da área/sala que ele quer entrar.
    public boolean tentarAcesso(Usuario usuario, String areaDesejada) {
        boolean acessoAutorizado = false;

        // Regra de Permissão baseada no Cargo
        if (usuario.getCargo() == Cargo.ADMINISTRADOR) {
            acessoAutorizado = true; // Admin acessa tudo
        } else if (usuario.getCargo() == Cargo.FUNCIONARIO) {
            // Funcionário não entra na Sala de TI/Servidores
            // 'equalsIgnoreCase' compara os textos ignorando maiúsculas/minúsculas
            if (!areaDesejada.equalsIgnoreCase("Sala de Servidores")) {
                acessoAutorizado = true;
            }
        } else if (usuario.getCargo() == Cargo.VISITANTE) {
            // Visitante só entra na Recepção
            if (areaDesejada.equalsIgnoreCase("Recepção")) {
                acessoAutorizado = true;
            }
        }

        // Cria o objeto de registro carimbando a hora
        RegistroAcesso novoLog = new RegistroAcesso(usuario, areaDesejada, acessoAutorizado);

        // Guardamos na nossa lista de histórico
        historicoAcessos.add(novoLog);

        // Devolve "TRUE" acesso permitido / "FALSE" acesso negado
        return acessoAutorizado;
    }

    // Exibe o o histórico de acesso (apenas para o ADMIN)
    public void exibirHistoricoCompleto(Usuario admin) {
        if (admin.getCargo() == Cargo.ADMINISTRADOR) {
            System.out.println("\n========= HISTÓRICO DE ACESSOS =========");
            if (historicoAcessos.isEmpty()) {
                System.out.println("Nenhum acesso registrado ainda.");
            } else {
                for (RegistroAcesso log : historicoAcessos) {
                    System.out.println(log);
                }
            }
            System.out.println("========================================");
        } else {
            System.out.println("\u001B[31m" + "ERRO: Acesso negado ao histórico. Apenas Administradores." + "\u001B[0m");
        }
    }

    // Metodo privado para popular o sistema
    private void inicializarDadosTeste() {
        listaUsuarios.add(new Usuario(1, "Chefão Admin", "admin@empresa.com", "admin123", Cargo.ADMINISTRADOR));
        listaUsuarios.add(new Usuario(2, "Ana Vendas", "ana@empresa.com", "ana123", Cargo.FUNCIONARIO));
        listaUsuarios.add(new Usuario(3, "Lucas Visitante", "lucas@empresa.com", "lucas123", Cargo.VISITANTE));
    }
}