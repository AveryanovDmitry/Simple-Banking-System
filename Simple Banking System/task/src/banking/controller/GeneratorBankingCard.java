package banking.controller;

import banking.model.Card;

import java.util.List;
import java.util.Random;

public class GeneratorBankingCard {
    private static final String DEFAULT_BIN_NUMBER = "400000";
    private static final int ASCII_ZERO = 48;
    private final Random random = new Random();

    private String generateNumber(){
        StringBuilder numberCard = new StringBuilder(DEFAULT_BIN_NUMBER);
        int needMoreNumbers = 9; //Amount of numbers up to the full card number

        for(int i = 0; i < needMoreNumbers; i++){
            numberCard.append(random.nextInt(10));
        }
        numberCard.append(gernerateLuhnAlgorithm(numberCard.toString()));
        return numberCard.toString();
    }


    public boolean checkLuhnAlgorithm(String numberCard){
        int sum = 0;
        int tmp;
        for(int i = 0; i < numberCard.length(); i++){
            tmp = Integer.parseInt(String.valueOf(numberCard.charAt(i)));

            if(i % 2 == 0)
                tmp *= 2;
            if(tmp > 9)
                tmp -= 9;

            sum += tmp;
        }
        if(sum % 10 == 0)
            return true;
        else
            return false;
    }


    private String gernerateLuhnAlgorithm(String strNum) {
        int sum = 0;
        for (int i = 0; i < strNum.length(); i++){
            int tmp = strNum.charAt(i) - ASCII_ZERO;
            if(i % 2 == 0){
                tmp *= 2;
                if(tmp > 9)
                    tmp -= 9;
            }
            sum += tmp;
        }
        if (sum % 10 != 0)
            return String.valueOf(10 - (sum % 10));
        else
            return String.valueOf(0);
    }

    private String generatePin(){
        StringBuilder pinCode = new StringBuilder();
        int needMoreNumbers = 4; //Amount of numbers up to the pin code
        for(int i = 0; i < needMoreNumbers; i++){
            pinCode.append(random.nextInt(10));
        }
        return pinCode.toString();
    }

    public Card GeneratorCard(ManagerDB managerDB){
        Card card = new Card();
        card.setNumber(generateNumber());
        card.setPin(generatePin());
        managerDB.addToTable(card);
        return card;
    };
}
