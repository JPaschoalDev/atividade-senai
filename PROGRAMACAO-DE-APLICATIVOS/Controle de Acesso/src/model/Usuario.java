package model;

public class Usuario {

    // Definição de atributos
    // Características que cada objeto do tipo "USUÁRIO" vai ter
    // Private é usado para que os atributos não sejam alterados (Encapsulamento)
    // APENAS A CLASSE "USUARIO" pode alterar esses atributos
    private String nome;    // Guarda o nome do usuário (texto)
    private int id;         // Guarda o iD do funcionário (inteiro)
    private String email;   // Guarda o email do usuário (texto)
    private String senha;   // Guarda a senha do usuário (texto)
    private Cargo cargo;    // Guarda o cargo do usuário (usa o "Cargo.java")

    // Metodo construtor
    // É executado apenas ao ser chamado: 'new Usuario(...)'
    // É utilizado para garantir que o objeto seja criado com as informações obrigatórias.
    public Usuario(int i, String nome, String email, String admin123, Cargo cargo) {
        // "this.email" está se referindo ao atribuito privado de mesmo nome (email)
        // "email" é o paramêtro que chega entre o parêntese
        // Esse metodo diz: Pegue o valor recebido e armazene no atributo deste objeto
        this.nome = nome;
        this.id = id;
        this.email = email;
        this.senha = hashSenha(senha);
        this.cargo = cargo;
    }

    // Método auxiliar
    //O método "hashSenha(...)" serve para "bagunçar" a senha e não salva-la em texto puro
    private String hashSenha(String senhaOriginal) {
        return "SECRET_" + senhaOriginal + "6sdfvg46sn56vs4g6";
    }

    // Método auxiliar
    // Serve para validar a senha no ato do login
    public boolean validarSenha(String senhaDigitada) {
        return this.senha.equals("SECRET_" + senhaOriginal + "6sdfvg46sn56vs4g6");
    }

    // Metodo auxiliar
    // O método "exibirDados()" serve para printar os dados de forma elegante.
    public void exibirDados() {
        System.out.println("------------------------------------");
        // O 'this.' aqui é opcional, mas ajuda a lembrar que estamos pegando as variáveis do próprio objeto.
        System.out.println("iD: " + this.id);
        System.out.println("Nome: " + this.nome);
        System.out.println("Email: " + this.email);
        System.out.println("Acesso: [" + this.cargo + "]");
        System.out.println("------------------------------------");
    }

    // Metodos "GETTERS e SETTERS"
    // Os "GETTERS" recebem o valor inserido e retorna uma String
    // Os "Setters" servem para alterar o valor do nome, alterando assim na classe Usuário sem acessar os paramêtros privados
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Cargo getCargo() {
        return cargo;
    }
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String novaSenha) {
        this.senha = hashSenha(novaSenha);
    }
}