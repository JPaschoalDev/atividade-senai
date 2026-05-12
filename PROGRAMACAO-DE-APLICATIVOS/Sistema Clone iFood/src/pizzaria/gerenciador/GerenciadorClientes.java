package pizzaria.gerenciador;

import pizzaria.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASSE: GerenciadorClientes
 *
 * Responsável pelo CRUD (Create, Read, Update, Delete) de clientes.
 *
 * CONCEITO POO APLICADO:
 *   - Classe com responsabilidade única: só cuida de clientes.
 *   - Encapsulamento: a lista 'clientes' é private; acesso apenas via métodos.
 *   - Método de busca retorna null para "não encontrado" (padrão Java).
 */
public class GerenciadorClientes {

    private List<Cliente> clientes;
    private int proximoId;

    public GerenciadorClientes() {
        this.clientes = new ArrayList<>();
        this.proximoId = 1;
        carregarClientesIniciais();
    }

    /**
     * Dados de exemplo para facilitar os testes sem precisar cadastrar tudo na mão.
     */
    private void carregarClientesIniciais() {
        cadastrar("Maria Silva",   "27999001122", "Rua das Flores, 10 - Centro");
        cadastrar("João Pereira",  "27988334455", "Av. Brasil, 200 - Bairro Alto");
        cadastrar("Ana Costa",     "27977556677", "Rua XV de Novembro, 55 - Vila Nova");
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * CREATE — Cadastra um novo cliente.
     * @return O objeto Cliente criado (útil para usar logo após o cadastro).
     */
    public Cliente cadastrar(String nome, String telefone, String endereco) {
        Cliente novo = new Cliente(proximoId++, nome, telefone, endereco);
        clientes.add(novo);
        return novo;
    }

    /**
     * READ — Busca por ID.
     */
    public Cliente buscarPorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    /**
     * READ — Busca por nome (parcial, não diferencia maiúsculas/minúsculas).
     * Ex: buscar("sil") encontra "Maria Silva".
     */
    public List<Cliente> buscarPorNome(String fragmento) {
        List<Cliente> resultado = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.getNome().toLowerCase().contains(fragmento.toLowerCase())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    /**
     * UPDATE — Atualiza dados de um cliente existente.
     * @return true se encontrou e atualizou; false se o ID não existir.
     */
    public boolean atualizar(int id, String novoNome, String novoTel, String novoEndereco) {
        Cliente c = buscarPorId(id);
        if (c == null) return false;

        // Só atualiza se o novo valor não for vazio (permite atualização parcial)
        if (!novoNome.isEmpty())     c.setNome(novoNome);
        if (!novoTel.isEmpty())      c.setTelefone(novoTel);
        if (!novoEndereco.isEmpty()) c.setEndereco(novoEndereco);

        return true;
    }

    /**
     * DELETE — Remove o cliente da lista pelo ID.
     * Em um sistema real, provavelmente seria uma desativação lógica.
     */
    public boolean remover(int id) {
        Cliente c = buscarPorId(id);
        if (c == null) return false;
        clientes.remove(c);
        return true;
    }

    /**
     * READ — Lista todos os clientes cadastrados.
     */
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    public int getTotalClientes() {
        return clientes.size();
    }
}
