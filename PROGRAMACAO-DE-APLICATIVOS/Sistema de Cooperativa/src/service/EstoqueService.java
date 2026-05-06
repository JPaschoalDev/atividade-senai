package service;

import model.Item;
import repository.EstoqueRepository;
import java.util.List;

/**
 * Serviço responsável pelas regras de negócio do sistema de estoque.
 *
 * <p>Centraliza todas as operações que envolvem lógica, validação e
 * cálculo financeiro. Depende do {@link EstoqueRepository} para
 * persistência dos dados em memória, mas nunca interage diretamente
 * com a interface do usuário.</p>
 *
 * <p>É a camada intermediária entre a visão ({@code view}) e os dados
 * ({@code repository}), garantindo que nenhuma regra de negócio
 * fique espalhada pelo sistema.</p>
 *
 * @author João Victor Paschoal
 * @version 2.0
 */
public class EstoqueService {

    /** Repositório utilizado para acesso e persistência dos itens. */
    private final EstoqueRepository repository;

    /**
     * Acumulador do valor total de todas as entradas registradas na sessão.
     * Calculado como: soma(quantidade_entrada × preço_unitário).
     */
    private double saldoEntradas = 0;

    /**
     * Acumulador do valor total de todas as saídas registradas na sessão.
     * Calculado como: soma(quantidade_saida × preço_unitário).
     */
    private double saldoSaidas = 0;

    /**
     * Constrói o serviço injetando o repositório que será utilizado.
     *
     * @param repository repositório de itens do estoque
     */
    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    /**
     * Cadastra um novo item no estoque após aplicar todas as validações.
     *
     * <p>Validações aplicadas:
     * <ul>
     *   <li>ID não pode já existir no estoque</li>
     *   <li>Quantidade inicial não pode ser negativa</li>
     *   <li>Preço deve ser maior que zero</li>
     * </ul>
     *
     * @param id         identificador único do item
     * @param nome       nome descritivo do item
     * @param quantidade quantidade inicial em estoque
     * @param preco      preço unitário em reais
     * @return mensagem de sucesso ou descrição do erro encontrado
     */
    public String cadastrarItem(int id, String nome, int quantidade, double preco) {
        if (repository.existePorId(id)) {
            return "ERRO: JÁ EXISTE UM ITEM COM ESTE ID.";
        }
        if (quantidade < 0) {
            return "ERRO: QUANTIDADE NÃO PODE SER NEGATIVA.";
        }
        if (preco <= 0) {
            return "ERRO: PREÇO DEVE SER MAIOR QUE ZERO.";
        }

        repository.adicionar(new Item(id, nome, quantidade, preco));
        return "ITEM CADASTRADO COM SUCESSO.";
    }

    /**
     * Busca um item no estoque pelo seu ID.
     *
     * @param id identificador do item a ser localizado
     * @return item encontrado, ou {@code null} se não existir
     */
    public Item buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    /**
     * Retorna todos os itens cadastrados no estoque.
     *
     * @return lista com todos os itens (pode estar vazia, nunca {@code null})
     */
    public List<Item> listarTodos() {
        return repository.listarTodos();
    }

    /**
     * Verifica se o estoque está vazio.
     *
     * @return {@code true} se não houver nenhum item cadastrado
     */
    public boolean estoqueVazio() {
        return repository.estaVazio();
    }

    /**
     * Registra uma entrada de mercadoria para o item informado.
     *
     * <p>Incrementa a quantidade do item e acumula o valor no saldo
     * de entradas da sessão.</p>
     *
     * @param item           item que receberá a entrada
     * @param qtdMovimentada quantidade a ser adicionada ao estoque
     * @return mensagem de sucesso ou descrição do erro encontrado
     */
    public String registrarEntrada(Item item, int qtdMovimentada) {
        if (qtdMovimentada <= 0) {
            return "ERRO: QUANTIDADE DEVE SER MAIOR QUE ZERO.";
        }

        item.setQuantidade(item.getQuantidade() + qtdMovimentada);
        saldoEntradas += qtdMovimentada * item.getPreco();
        return "ENTRADA REGISTRADA COM SUCESSO.";
    }

    /**
     * Registra uma saída de mercadoria para o item informado.
     *
     * <p>Decrementa a quantidade do item e acumula o valor no saldo
     * de saídas da sessão. Rejeita a operação se o estoque for insuficiente.</p>
     *
     * @param item           item que terá a saída registrada
     * @param qtdMovimentada quantidade a ser retirada do estoque
     * @return mensagem de sucesso ou descrição do erro encontrado
     */
    public String registrarSaida(Item item, int qtdMovimentada) {
        if (qtdMovimentada <= 0) {
            return "ERRO: QUANTIDADE DEVE SER MAIOR QUE ZERO.";
        }
        if (item.getQuantidade() < qtdMovimentada) {
            return "ERRO: ESTOQUE INSUFICIENTE.";
        }

        item.setQuantidade(item.getQuantidade() - qtdMovimentada);
        saldoSaidas += qtdMovimentada * item.getPreco();
        return "SAÍDA REGISTRADA COM SUCESSO.";
    }

    /**
     * Calcula o valor patrimonial total do estoque.
     *
     * <p>Soma o valor de todos os itens: quantidade × preço unitário.</p>
     *
     * @return valor patrimonial total em reais
     */
    public double calcularValorPatrimonial() {
        double total = 0;
        for (Item item : repository.listarTodos()) {
            total += item.getQuantidade() * item.getPreco();
        }
        return total;
    }

    /**
     * Retorna o total acumulado de entradas na sessão atual.
     *
     * @return saldo de entradas em reais
     */
    public double getSaldoEntradas() { return saldoEntradas; }

    /**
     * Retorna o total acumulado de saídas na sessão atual.
     *
     * @return saldo de saídas em reais
     */
    public double getSaldoSaidas() { return saldoSaidas; }
}