package banking.views;

import banking.model.Card;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public void printMenu(){
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    public void printMenuPersonalAccount(){
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    public String printTransferMenu(){
        System.out.println("Transfer\n" +
                "Enter card number:");
        return scanner.next();
    }

    public int scanSumForTransfer(){
        System.out.println("Enter how much money you want to transfer:");
        return  scanner.nextInt();
    }

    public void printResultTransfer(boolean result){
        if (result){
            System.out.println("Success!");
        } else {
            System.out.println("Such a card does not exist.");
        }
    }
    public int scanAddIncomeMenu(){
        System.out.println("Enter income:");
        return scanner.nextInt();
    }

    public int scanUserChoice(){
        return scanner.nextInt();
    }

    public void printCreateCardMessage(String numberCard, String pinCode){
        System.out.println("Your card has been created\nYour card number:");
        System.out.println(numberCard);
        System.out.println("Your card PIN:");
        System.out.println(pinCode);
    }

    public Card checkCardNumber(){
        Card userInput = new Card();
        System.out.println("Enter your card number:");
        userInput.setNumber(scanner.next());
        System.out.println("Enter your PIN:");
        userInput.setPin(scanner.next());
        return userInput;
    }
}
