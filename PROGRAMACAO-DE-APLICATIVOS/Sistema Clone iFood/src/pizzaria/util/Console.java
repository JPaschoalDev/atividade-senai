package pizzaria.util;

import java.util.Scanner;

/**
 * CLASSE UTILITÁRIA: Console
 *
 * Centraliza a leitura de entradas do usuário via Scanner.
 *
 * CONCEITO POO APLICADO:
 *   - Classe utilitária com métodos que encapsulam operações repetitivas.
 *   - Evita criar vários Scanner espalhados pelo código (má prática).
 *   - Trata erros de entrada (ex: usuário digita letra onde esperava número).
 *
 * USO: Console.lerInt("Digite o ID: ")
 */
public class Console {

    // Um único Scanner para toda a aplicação (static = pertence à classe, não ao objeto)
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lê uma linha de texto do usuário.
     * @param mensagem Prompt exibido antes da entrada
     */
    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();  // trim() remove espaços extras
    }

    /**
     * Lê um número inteiro. Repete até o usuário digitar um número válido.
     */
    public static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Digite um número inteiro válido.");
            }
        }
    }

    /**
     * Lê um número decimal (double). Repete até o usuário digitar um valor válido.
     */
    public static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                // Substitui vírgula por ponto (compatibilidade com pt-BR)
                String entrada = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Digite um valor numérico válido (ex: 39.90).");
            }
        }
    }

    /**
     * Exibe uma linha separadora para organizar a interface do console.
     */
    public static void separador() {
        System.out.println("----------------------------------------");
    }

    public static void titulo(String texto) {
        System.out.println("\n========================================");
        System.out.println("  " + texto);
        System.out.println("========================================");
    }

    /**
     * Pausa o programa e pede que o usuário pressione Enter para continuar.
     */
    public static void pausar() {
        System.out.print("\n[Pressione ENTER para continuar...]");
        scanner.nextLine();
    }
}
