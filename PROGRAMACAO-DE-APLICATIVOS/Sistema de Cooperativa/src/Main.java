import repository.EstoqueRepository;
import service.EstoqueService;
import view.Menu;

/**
 * Ponto de entrada da aplicação de gestão de estoque.
 *
 * <p>Responsável exclusivamente por instanciar as dependências,
 * popular o estoque inicial de demonstração e iniciar a interface
 * de usuário. Nenhuma regra de negócio deve residir aqui.</p>
 *
 * <p>Fluxo de inicialização:
 * <ol>
 *   <li>Cria o repositório (armazenamento em memória)</li>
 *   <li>Cria o serviço injetando o repositório</li>
 *   <li>Popula o estoque com itens de exemplo</li>
 *   <li>Cria o menu injetando o serviço</li>
 *   <li>Inicia o loop principal da aplicação</li>
 * </ol>
 *
 * @author João Victor Paschoal
 * @version 2.0
 */
public class Main {

    /**
     * Método principal que inicializa e executa o sistema.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {

        // 1. Cria o repositório — responsável por guardar os itens
        EstoqueRepository repository = new EstoqueRepository();

        // 2. Cria o serviço — responsável pelas regras de negócio
        EstoqueService service = new EstoqueService(repository);

        // 3. Popula o estoque com itens iniciais de demonstração
        service.cadastrarItem(1, "Teclado RGB",  15, 150.00);
        service.cadastrarItem(2, "Mouse Gamer",  22,  89.90);
        service.cadastrarItem(3, "Monitor 24'",   5, 850.00);

        // 4. Cria o menu — responsável pela interface com o usuário
        Menu menu = new Menu(service);

        // 5. Inicia o sistema
        menu.iniciar();
    }
}