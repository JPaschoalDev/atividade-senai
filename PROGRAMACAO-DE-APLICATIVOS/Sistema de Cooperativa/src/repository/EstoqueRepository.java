package repository;

import model.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório responsável pelo armazenamento e recuperação dos itens do estoque.
 *
 * <p>Atua como a camada de acesso a dados da aplicação. Toda operação
 * que envolve ler ou escrever na lista de itens passa obrigatoriamente
 * por esta classe.</p>
 *
 * <p><b>Limitação:</b> os dados são mantidos em memória e perdidos ao
 * encerrar a aplicação. Em uma versão futura, esta classe poderia ser
 * substituída por uma implementação com banco de dados sem impactar
 * as demais camadas.</p>
 *
 * @author João Victor Paschoal
 * @version 2.0
 */
public class EstoqueRepository {

    /** Lista interna que armazena todos os itens cadastrados na sessão. */
    private final List<Item> itens = new ArrayList<>();

    /**
     * Adiciona um novo item à lista do estoque.
     *
     * <p>Não realiza validações — a responsabilidade de garantir que
     * o item é válido pertence à camada de serviço.</p>
     *
     * @param item item a ser adicionado
     */
    public void adicionar(Item item) {
        itens.add(item);
    }

    /**
     * Retorna todos os itens atualmente cadastrados no estoque.
     *
     * @return lista com todos os itens (pode estar vazia, nunca {@code null})
     */
    public List<Item> listarTodos() {
        return itens;
    }

    /**
     * Busca um item pelo seu identificador único.
     *
     * <p>Realiza busca linear na lista. Retorna {@code null} caso nenhum
     * item com o ID informado seja encontrado.</p>
     *
     * @param id identificador do item a ser localizado
     * @return item encontrado, ou {@code null} se não existir
     */
    public Item buscarPorId(int id) {
        for (Item item : itens) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Verifica se já existe um item cadastrado com o ID informado.
     *
     * <p>Utilizado antes do cadastro para garantir unicidade de IDs.</p>
     *
     * @param id identificador a ser verificado
     * @return {@code true} se o ID já existir, {@code false} caso contrário
     */
    public boolean existePorId(int id) {
        return buscarPorId(id) != null;
    }

    /**
     * Verifica se o estoque está vazio.
     *
     * @return {@code true} se não houver nenhum item cadastrado
     */
    public boolean estaVazio() {
        return itens.isEmpty();
    }
}
