package banking.controller;

import banking.model.Card;
import banking.views.ConsoleView;

public class ControllerMenu {
    private ConsoleView consoleView = new ConsoleView();;
    private GeneratorBankingCard generatorCard = new GeneratorBankingCard();;
    private ManagerDB managerDB = new ManagerDB();;
    public ControllerMenu(String url) {
        managerDB.createTable(url);
    }
    public void controllerMainMenu() {
        consoleView.printMenu();
        int choice = consoleView.scanUserChoice();
        while (choice != 0) {


            if (choice == 1) {
                Card card = generatorCard.GeneratorCard(managerDB);
                consoleView.printCreateCardMessage(card.getNumber(), card.getPin());
            } else if (choice == 2) {
                Card card = consoleView.checkCardNumber();
                if (managerDB.checkCardInTable(card)) { //проверяем номер карты и пин код, дальше пин не проверяем
                    System.out.println("You have successfully logged in!\n");
                    while (choice != 5 && choice != 0) {
                        choice = controllerPersonalAccount(card);
                    }
                    if (choice == 0){
                        System.out.println("Bye!");
                        break;
                    }
                } else {
                    System.out.println("Wrong card number or PIN!");
                }
            } else if (choice == 0) {
                System.out.println("Bye!");
                break;
            }
            consoleView.printMenu();
            choice = consoleView.scanUserChoice();
        }
        managerDB.close();
        System.out.println("Bye!");
    }

    public int controllerPersonalAccount(Card card) {
        int choice = 0;
        consoleView.printMenuPersonalAccount(); //вывод меню пользовотеля
        choice = consoleView.scanUserChoice(); //выбор пользователя в меню
        if (choice == 1) {
            System.out.println(managerDB.getBalance(card.getNumber()));
        } else if (choice == 2) {
            System.out.println(managerDB.updateBalance(consoleView.scanAddIncomeMenu(), card.getNumber()));
        } else if (choice == 3) {
            String numberCard = consoleView.printTransferMenu();
            if(!generatorCard.checkLuhnAlgorithm(numberCard)){
                System.out.println("Probably you made a mistake in the card number. Please try again!");
                return 3;
            } else if (!managerDB.checkCardInTableWithoutPin(numberCard)) {
                System.out.println("Such a card does not exist.");
                return 3;
            } else if (managerDB.searchCardInTable(numberCard) != null) {
                int amountOfMoney = consoleView.scanSumForTransfer();
                consoleView.printResultTransfer(managerDB.doTransfer(numberCard, amountOfMoney, card));
            } else
                consoleView.printResultTransfer(false);
        } else if (choice == 4) {
            managerDB.deleteEntyFromTable(card);
            System.out.println("The account has been closed!");
        }
        return choice;
    }
}
