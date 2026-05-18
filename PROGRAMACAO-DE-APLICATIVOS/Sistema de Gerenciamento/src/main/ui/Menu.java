package main.ui;

import main.database.DatabaseConnection;
import main.models.*;
import main.repositories.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Ponto de entrada do sistema.
 * Exibe menus de texto e delega operações aos repositórios.
 */
public class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    private static final TutorRepository      tutorRepo      = new TutorRepository();
    private static final VeterinarioRepository vetRepo        = new VeterinarioRepository();
    private static final AnimalRepository      animalRepo     = new AnimalRepository();
    private static final ConsultaRepository    consultaRepo   = new ConsultaRepository();

    public static void main(String[] args) {
        DatabaseConnection.inicializarBanco();
        exibirMenuPrincipal();
    }

    // ── Menu Principal ─────────────────────────────────────────────

    private static void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║     🐾  CLÍNICA VETERINÁRIA  🐾     ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  1. Gerenciar Tutores              ║");
            System.out.println("║  2. Gerenciar Veterinários         ║");
            System.out.println("║  3. Gerenciar Animais              ║");
            System.out.println("║  4. Gerenciar Consultas            ║");
            System.out.println("║  0. Sair                           ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> menuTutores();
                case 2 -> menuVeterinarios();
                case 3 -> menuAnimais();
                case 4 -> menuConsultas();
                case 0 -> { System.out.println("Até logo!"); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // ── Menu Tutores ───────────────────────────────────────────────

    private static void menuTutores() {
        while (true) {
            System.out.println("\n── TUTORES ──");
            System.out.println("1. Cadastrar  2. Listar  3. Buscar por ID  4. Atualizar  5. Excluir  0. Voltar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> cadastrarTutor();
                case 2 -> listarTutores();
                case 3 -> buscarTutorPorId();
                case 4 -> atualizarTutor();
                case 5 -> deletarTutor();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarTutor() {
        System.out.println("\n-- Cadastrar Tutor --");
        System.out.print("Nome: ");      String nome     = lerTexto();
        System.out.print("Telefone: ");  String tel      = lerTexto();
        System.out.print("Email: ");     String email    = lerTexto();
        System.out.print("Endereço: ");  String endereco = lerTexto();

        tutorRepo.salvar(new Tutor(nome, tel, email, endereco));
    }

    private static void listarTutores() {
        List<Tutor> tutores = tutorRepo.buscarTodos();
        if (tutores.isEmpty()) {
            System.out.println("Nenhum tutor cadastrado.");
            return;
        }
        System.out.println("\n-- Lista de Tutores --");
        tutores.forEach(t -> System.out.println("[ID " + t.getId() + "] " + t.apresentar()));
    }

    private static void buscarTutorPorId() {
        System.out.print("ID do tutor: ");
        int id = lerInteiro();
        tutorRepo.buscarPorId(id).ifPresentOrElse(
            t -> System.out.println("[ID " + t.getId() + "] " + t.apresentar()),
            () -> System.out.println("Tutor não encontrado.")
        );
    }

    private static void atualizarTutor() {
        System.out.print("ID do tutor a atualizar: ");
        int id = lerInteiro();

        Optional<Tutor> opt = tutorRepo.buscarPorId(id);
        if (opt.isEmpty()) { System.out.println("Tutor não encontrado."); return; }

        Tutor tutor = opt.get();
        System.out.println("Atual: " + tutor.apresentar());

        System.out.print("Novo nome (Enter para manter): ");
        String nome = lerTexto();
        if (!nome.isBlank()) tutor.setNome(nome);

        System.out.print("Novo telefone (Enter para manter): ");
        String tel = lerTexto();
        if (!tel.isBlank()) tutor.setTelefone(tel);

        System.out.print("Novo email (Enter para manter): ");
        String email = lerTexto();
        if (!email.isBlank()) tutor.setEmail(email);

        System.out.print("Novo endereço (Enter para manter): ");
        String end = lerTexto();
        if (!end.isBlank()) tutor.setEndereco(end);

        boolean ok = tutorRepo.atualizar(tutor);
        System.out.println(ok ? "Tutor atualizado!" : "Falha na atualização.");
    }

    private static void deletarTutor() {
        System.out.print("ID do tutor a excluir: ");
        int id = lerInteiro();
        boolean ok = tutorRepo.deletar(id);
        System.out.println(ok ? "Tutor excluído!" : "Tutor não encontrado.");
    }

    // ── Menu Veterinários ──────────────────────────────────────────

    private static void menuVeterinarios() {
        while (true) {
            System.out.println("\n── VETERINÁRIOS ──");
            System.out.println("1. Cadastrar  2. Listar  3. Buscar por ID  4. Atualizar  5. Excluir  0. Voltar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> cadastrarVeterinario();
                case 2 -> listarVeterinarios();
                case 3 -> buscarVeterinarioPorId();
                case 4 -> atualizarVeterinario();
                case 5 -> deletarVeterinario();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarVeterinario() {
        System.out.println("\n-- Cadastrar Veterinário --");
        System.out.print("Nome: ");          String nome  = lerTexto();
        System.out.print("Telefone: ");      String tel   = lerTexto();
        System.out.print("Email: ");         String email = lerTexto();
        System.out.print("CRMV: ");          String crmv  = lerTexto();
        System.out.print("Especialidade: "); String esp   = lerTexto();

        vetRepo.salvar(new Veterinario(nome, tel, email, crmv, esp));
    }

    private static void listarVeterinarios() {
        List<Veterinario> vets = vetRepo.buscarTodos();
        if (vets.isEmpty()) { System.out.println("Nenhum veterinário cadastrado."); return; }
        System.out.println("\n-- Lista de Veterinários --");
        vets.forEach(v -> System.out.println("[ID " + v.getId() + "] " + v.apresentar()));
    }

    private static void buscarVeterinarioPorId() {
        System.out.print("ID do veterinário: ");
        int id = lerInteiro();
        vetRepo.buscarPorId(id).ifPresentOrElse(
            v -> System.out.println("[ID " + v.getId() + "] " + v.apresentar()),
            () -> System.out.println("Veterinário não encontrado.")
        );
    }

    private static void atualizarVeterinario() {
        System.out.print("ID do veterinário a atualizar: ");
        int id = lerInteiro();
        Optional<Veterinario> opt = vetRepo.buscarPorId(id);
        if (opt.isEmpty()) { System.out.println("Não encontrado."); return; }

        Veterinario vet = opt.get();
        System.out.println("Atual: " + vet.apresentar());

        System.out.print("Novo nome (Enter para manter): ");
        String nome = lerTexto(); if (!nome.isBlank()) vet.setNome(nome);
        System.out.print("Novo telefone (Enter para manter): ");
        String tel = lerTexto(); if (!tel.isBlank()) vet.setTelefone(tel);
        System.out.print("Novo email (Enter para manter): ");
        String email = lerTexto(); if (!email.isBlank()) vet.setEmail(email);
        System.out.print("Nova especialidade (Enter para manter): ");
        String esp = lerTexto(); if (!esp.isBlank()) vet.setEspecialidade(esp);

        System.out.println(vetRepo.atualizar(vet) ? "Veterinário atualizado!" : "Falha.");
    }

    private static void deletarVeterinario() {
        System.out.print("ID do veterinário a excluir: ");
        int id = lerInteiro();
        System.out.println(vetRepo.deletar(id) ? "Veterinário excluído!" : "Não encontrado.");
    }

    // ── Menu Animais ───────────────────────────────────────────────

    private static void menuAnimais() {
        while (true) {
            System.out.println("\n── ANIMAIS ──");
            System.out.println("1. Cadastrar  2. Listar  3. Buscar por ID  4. Atualizar  5. Excluir  0. Voltar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> cadastrarAnimal();
                case 2 -> listarAnimais();
                case 3 -> buscarAnimalPorId();
                case 4 -> atualizarAnimal();
                case 5 -> deletarAnimal();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarAnimal() {
        System.out.println("\n-- Cadastrar Animal --");
        System.out.print("Nome do animal: "); String nome    = lerTexto();
        System.out.print("Espécie: ");        String especie = lerTexto();
        System.out.print("Raça: ");           String raca    = lerTexto();
        System.out.print("Idade (anos): ");   int idade      = lerInteiro();
        listarTutores();
        System.out.print("ID do tutor: ");    int tutorId    = lerInteiro();

        animalRepo.salvar(new Animal(nome, especie, raca, idade, tutorId));
    }

    private static void listarAnimais() {
        List<Animal> animais = animalRepo.buscarTodos();
        if (animais.isEmpty()) { System.out.println("Nenhum animal cadastrado."); return; }
        System.out.println("\n-- Lista de Animais --");
        animais.forEach(a -> System.out.println("[ID " + a.getId() + "] " + a));
    }

    private static void buscarAnimalPorId() {
        System.out.print("ID do animal: ");
        int id = lerInteiro();
        animalRepo.buscarPorId(id).ifPresentOrElse(
            a -> System.out.println("[ID " + a.getId() + "] " + a),
            () -> System.out.println("Animal não encontrado.")
        );
    }

    private static void atualizarAnimal() {
        System.out.print("ID do animal a atualizar: ");
        int id = lerInteiro();
        Optional<Animal> opt = animalRepo.buscarPorId(id);
        if (opt.isEmpty()) { System.out.println("Não encontrado."); return; }

        Animal animal = opt.get();
        System.out.println("Atual: " + animal);

        System.out.print("Novo nome (Enter para manter): ");
        String nome = lerTexto(); if (!nome.isBlank()) animal.setNome(nome);
        System.out.print("Nova espécie (Enter para manter): ");
        String esp = lerTexto(); if (!esp.isBlank()) animal.setEspecie(esp);
        System.out.print("Nova raça (Enter para manter): ");
        String raca = lerTexto(); if (!raca.isBlank()) animal.setRaca(raca);
        System.out.print("Nova idade (0 para manter): ");
        int idade = lerInteiro(); if (idade > 0) animal.setIdadeAnos(idade);

        System.out.println(animalRepo.atualizar(animal) ? "Animal atualizado!" : "Falha.");
    }

    private static void deletarAnimal() {
        System.out.print("ID do animal a excluir: ");
        int id = lerInteiro();
        System.out.println(animalRepo.deletar(id) ? "Animal excluído!" : "Não encontrado.");
    }

    // ── Menu Consultas ─────────────────────────────────────────────

    private static void menuConsultas() {
        while (true) {
            System.out.println("\n── CONSULTAS ──");
            System.out.println("1. Registrar  2. Listar todas  3. Buscar por ID  4. Atualizar  5. Excluir  0. Voltar");
            System.out.print("Escolha: ");

            switch (lerInteiro()) {
                case 1 -> registrarConsulta();
                case 2 -> listarConsultas();
                case 3 -> buscarConsultaPorId();
                case 4 -> atualizarConsulta();
                case 5 -> deletarConsulta();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void registrarConsulta() {
        System.out.println("\n-- Registrar Consulta --");
        listarAnimais();
        System.out.print("ID do animal: ");       int animalId  = lerInteiro();
        listarVeterinarios();
        System.out.print("ID do veterinário: ");  int vetId     = lerInteiro();
        System.out.print("Data (YYYY-MM-DD HH:MM): "); String data = lerTexto();
        System.out.print("Motivo: ");             String motivo = lerTexto();
        System.out.print("Diagnóstico: ");        String diag   = lerTexto();

        consultaRepo.salvar(new Consulta(animalId, vetId, data, motivo, diag));
    }

    private static void listarConsultas() {
        List<Consulta> consultas = consultaRepo.buscarTodas();
        if (consultas.isEmpty()) { System.out.println("Nenhuma consulta registrada."); return; }
        System.out.println("\n-- Lista de Consultas --");
        consultas.forEach(c -> System.out.println(c));
    }

    private static void buscarConsultaPorId() {
        System.out.print("ID da consulta: ");
        int id = lerInteiro();
        consultaRepo.buscarPorId(id).ifPresentOrElse(
            c -> {
                System.out.println(c);
                System.out.println("  Diagnóstico: " + c.getDiagnostico());
            },
            () -> System.out.println("Consulta não encontrada.")
        );
    }

    private static void atualizarConsulta() {
        System.out.print("ID da consulta a atualizar: ");
        int id = lerInteiro();
        Optional<Consulta> opt = consultaRepo.buscarPorId(id);
        if (opt.isEmpty()) { System.out.println("Não encontrada."); return; }

        Consulta consulta = opt.get();
        System.out.println("Atual: " + consulta);

        System.out.print("Nova data (Enter para manter): ");
        String data = lerTexto(); if (!data.isBlank()) consulta.setData(data);
        System.out.print("Novo motivo (Enter para manter): ");
        String motivo = lerTexto(); if (!motivo.isBlank()) consulta.setMotivo(motivo);
        System.out.print("Novo diagnóstico (Enter para manter): ");
        String diag = lerTexto(); if (!diag.isBlank()) consulta.setDiagnostico(diag);

        System.out.println(consultaRepo.atualizar(consulta) ? "Consulta atualizada!" : "Falha.");
    }

    private static void deletarConsulta() {
        System.out.print("ID da consulta a excluir: ");
        int id = lerInteiro();
        System.out.println(consultaRepo.deletar(id) ? "Consulta excluída!" : "Não encontrada.");
    }

    // ── Utilitários de entrada ─────────────────────────────────────

    private static String lerTexto() {
        return scanner.nextLine().trim();
    }

    private static int lerInteiro() {
        try {
            String linha = scanner.nextLine().trim();
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            System.out.print("Valor inválido, digite um número: ");
            return lerInteiro();
        }
    }
}
