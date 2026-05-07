package main;

import controller.ControleAcessoController;
import view.Menu;

public class SistemaMain {
    public static void main(String[] args) {
        // 1. Cria o cérebro (onde ficam as listas e regras)
        ControleAcessoController controller = new ControleAcessoController();

        // 2. Cria a tela, passando o cérebro para ela poder mandar comandos
        Menu menu = new Menu(controller);

        // 3. Abre o menu interativo no console
        menu.exibirMenuPrincipal();
    }
}