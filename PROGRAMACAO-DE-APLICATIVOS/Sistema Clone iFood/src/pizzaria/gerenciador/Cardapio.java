package pizzaria.gerenciador;

import pizzaria.modelo.Pizza;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASSE: Cardapio
 *
 * Gerencia a lista de pizzas disponíveis no estabelecimento.
 *
 * CONCEITO POO APLICADO:
 *   - Responsabilidade Única (SRP): essa classe cuida APENAS do cardápio.
 *   - ArrayList como coleção de objetos Pizza.
 *   - Métodos de busca encapsulados (buscarPorId, listarDisponiveis).
 *
 * NOTA: Não é MVC — não há separação em View/Controller.
 *   A lógica de exibição fica no Main; aqui ficam apenas os dados e regras.
 */
public class Cardapio {

    // Lista de todas as pizzas cadastradas (disponíveis ou não)
    private List<Pizza> pizzas;

    // Controle de ID automático (incrementa a cada nova pizza)
    private int proximoId;

    // -------------------------
    // CONSTRUTOR
    // -------------------------
    public Cardapio() {
        this.pizzas = new ArrayList<>();
        this.proximoId = 1;
        carregarPizzasIniciais();  // Popula o cardápio com dados de exemplo
    }

    /**
     * Popula o cardápio com pizzas de exemplo ao iniciar o sistema.
     * Em um sistema real, isso viria de um banco de dados ou arquivo.
     */
    private void carregarPizzasIniciais() {
        adicionar("Calabresa",           "Molho, mussarela, calabresa e cebola", 39.90, "M");
        adicionar("Frango c/ Catupiry",  "Molho, frango desfiado e catupiry",    44.90, "M");
        adicionar("Portuguesa",          "Molho, presunto, ovos, cebola, ervilha",42.90, "M");
        adicionar("Quatro Queijos",      "Molho, 4 tipos de queijo",             48.90, "G");
        adicionar("Margherita",          "Molho, mussarela e manjericão fresco", 37.90, "M");
        adicionar("Chocolate",           "Chocolate ao leite e morango",         46.90, "M");
        adicionar("Romeo e Julieta",     "Goiabada e cream cheese",              44.90, "P");
    }

    // -------------------------
    // MÉTODOS DE NEGÓCIO
    // -------------------------

    /**
     * Adiciona uma nova pizza ao cardápio.
     * O ID é gerado automaticamente pela própria classe.
     */
    public Pizza adicionar(String nome, String descricao, double preco, String tamanho) {
        Pizza nova = new Pizza(proximoId++, nome, descricao, preco, tamanho);
        pizzas.add(nova);
        return nova;
    }

    /**
     * Busca uma pizza pelo ID. Retorna null se não encontrar.
     * Usamos 'null' como sinal de "não encontrado" — prática comum em Java.
     */
    public Pizza buscarPorId(int id) {
        for (Pizza p : pizzas) {
            if (p.getId() == id) return p;
        }
        return null;  // Não encontrado
    }

    /**
     * Retorna apenas as pizzas marcadas como disponíveis.
     * Cria uma nova lista (não expõe a lista interna diretamente).
     */
    public List<Pizza> listarDisponiveis() {
        List<Pizza> disponiveis = new ArrayList<>();
        for (Pizza p : pizzas) {
            if (p.isDisponivel()) disponiveis.add(p);
        }
        return disponiveis;
    }

    /**
     * Retorna todas as pizzas (disponíveis e indisponíveis).
     * Usado na tela de gerenciamento do cardápio.
     */
    public List<Pizza> listarTodas() {
        return new ArrayList<>(pizzas);  // Cópia da lista, não a referência original
    }

    /**
     * Remove uma pizza do cardápio (marca como indisponível, não exclui).
     * Isso preserva o histórico de pedidos que já usaram essa pizza.
     */
    public boolean desativar(int id) {
        Pizza p = buscarPorId(id);
        if (p == null) return false;
        p.setDisponivel(false);
        return true;
    }

    public boolean ativar(int id) {
        Pizza p = buscarPorId(id);
        if (p == null) return false;
        p.setDisponivel(true);
        return true;
    }

    public int getTotalPizzas() {
        return pizzas.size();
    }
}
