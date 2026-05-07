package model;
// Usado para pegar data e hora real
import java.time.LocalDateTime;

public class RegistroAcesso {
    private LocalDateTime dataHora;
    private Usuario usuario; // Aqui não pega apenas o nome do usuário, mas sim todos os atributos da classe "Usuario"
    private String area;
    private boolean autorizado; // Utilizado para validar a entrada ou não

    // Metodo usado para armazenar informações no ato do registro
    public RegistroAcesso(Usuario usuario, String area, boolean autorizado) {
        this.dataHora = LocalDateTime.now(); // Pega o momento exato
        this.usuario = usuario;
        this.area = area;
        this.autorizado = autorizado;
    }

    // "@Override" transcreve o método toString para exibir de forma legível para o usuário
    @Override
    public String toString() {
        // Operador ternário (?) é um if/else elegante de uma linha só
        // Lê-se "Se autorizado for verdadeiro, coloque "AUTORIZADO". Senão (:), coloque "NEGADO"
        String status = autorizado ? "AUTORIZADO" : "NEGADO";
        return "[" + dataHora + "] Usuário: " + usuario.getNome() +
                " | Área: " + area + " | Resultado: " + status;
    }
}
