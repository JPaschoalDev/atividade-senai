package model;
/**
 * Representa um item do estoque do sistema de gestão.
 *
 * <p>Cada item possui um identificador único, nome, quantidade disponível
 * e preço unitário. Os atributos são encapsulados e acessados via getters,
 * sendo que apenas a quantidade pode ser alterada após a criação do objeto.</p>
 *
 * @author João Victor Paschoal
 * @version 1.0
 */
public class Item {

    /** Identificador único do item no estoque. Não pode ser alterado após o cadastro. */
    private int id;

    /** Nome descritivo do item. Não pode ser alterado após o cadastro. */
    private String nome;

    /** Quantidade atual disponível no estoque. Atualizada a cada movimentação. */
    private int quantidade;

    /** Preço unitário do item em reais (R$). Não pode ser alterado após o cadastro. */
    private double preco;

    /**
     * Constrói um novo item com todos os atributos obrigatórios.
     *
     * @param id         identificador único do item
     * @param nome       nome descritivo do item
     * @param quantidade quantidade inicial disponível no estoque
     * @param preco      preço unitário em reais
     */
    public Item(int id, String nome, int quantidade, double preco) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    /**
     * Retorna o identificador único do item.
     *
     * @return id do item
     */
    public int getId() { return id; }

    /**
     * Retorna o nome descritivo do item.
     *
     * @return nome do item
     */
    public String getNome() { return nome; }

    /**
     * Retorna a quantidade atual disponível no estoque.
     *
     * @return quantidade em estoque
     */
    public int getQuantidade() { return quantidade; }

    /**
     * Retorna o preço unitário do item.
     *
     * @return preço em reais
     */
    public double getPreco() { return preco; }

    /**
     * Atualiza a quantidade disponível do item no estoque.
     *
     * <p>Deve ser chamado após operações de entrada ou saída de mercadoria.
     * Não valida o valor recebido — a validação é responsabilidade da camada
     * que chama este método.</p>
     *
     * @param quantidade nova quantidade em estoque
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna uma representação textual formatada do item.
     *
     * <p>Formato: {@code ID: X | Nome: XXXXXXXXXXXXXXX | Qtd: X | Preço: R$ X.XX}</p>
     *
     * @return string formatada com os dados do item
     */
    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %-15s | Qtd: %d | Preço: R$ %.2f",
                id, nome, quantidade, preco);
    }
}