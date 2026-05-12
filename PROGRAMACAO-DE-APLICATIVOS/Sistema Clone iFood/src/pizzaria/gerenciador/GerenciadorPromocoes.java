package pizzaria.gerenciador;

import pizzaria.modelo.Promocao;
import pizzaria.modelo.Promocao.TipoPromocao;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASSE: GerenciadorPromocoes
 *
 * Gerencia o cadastro e ativação/desativação de promoções.
 *
 * CONCEITO POO APLICADO:
 *   - Separação de responsabilidades: esta classe não sabe nada sobre pedidos.
 *     Ela apenas mantém o cadastro de promoções.
 *   - O cálculo do desconto fica na classe Promocao (responsabilidade do objeto).
 */
public class GerenciadorPromocoes {

    private List<Promocao> promocoes;
    private int proximoId;

    public GerenciadorPromocoes() {
        this.promocoes = new ArrayList<>();
        this.proximoId = 1;
        carregarPromocoesIniciais();
    }

    private void carregarPromocoesIniciais() {
        // Promoção percentual: 10% de desconto
        cadastrar("Desconto de Boas-vindas", TipoPromocao.PERCENTUAL, 10.0);
        // Promoção combo: leve 2, pague 1
        cadastrar("Combo 2 Pizzas",          TipoPromocao.COMBO,      0.0);
    }

    // -------------------------
    // MÉTODOS
    // -------------------------

    /**
     * Cadastra uma nova promoção.
     * Para o tipo COMBO, o valorDesconto é ignorado (pode passar 0).
     */
    public Promocao cadastrar(String nome, TipoPromocao tipo, double valorDesconto) {
        Promocao nova = new Promocao(proximoId++, nome, tipo, valorDesconto);
        promocoes.add(nova);
        return nova;
    }

    public Promocao buscarPorId(int id) {
        for (Promocao p : promocoes) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Retorna apenas promoções ativas.
     */
    public List<Promocao> listarAtivas() {
        List<Promocao> ativas = new ArrayList<>();
        for (Promocao p : promocoes) {
            if (p.isAtiva()) ativas.add(p);
        }
        return ativas;
    }

    public List<Promocao> listarTodas() {
        return new ArrayList<>(promocoes);
    }

    public boolean ativar(int id) {
        Promocao p = buscarPorId(id);
        if (p == null) return false;
        p.setAtiva(true);
        return true;
    }

    public boolean desativar(int id) {
        Promocao p = buscarPorId(id);
        if (p == null) return false;
        p.setAtiva(false);
        return true;
    }
}
