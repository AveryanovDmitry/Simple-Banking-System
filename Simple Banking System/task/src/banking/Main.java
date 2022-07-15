package banking;

import banking.controller.ControllerMenu;

public class Main {
    public static void main(String[] args) {
        ControllerMenu menuBankingSystem = new ControllerMenu("jdbc:sqlite:"+args[1]);
        menuBankingSystem.controllerMainMenu();
    }
}