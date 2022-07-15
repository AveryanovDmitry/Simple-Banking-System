package banking.controller;

import banking.model.Card;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class ManagerDB {
    SQLiteDataSource dataSource;
    Connection connection;
    Statement statement;
    public void createTable(String url){
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try {
            connection = dataSource.getConnection();
            try {
                statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int addToTable(Card card){
        int i = 0;
        try {
            i = statement.executeUpdate("INSERT INTO card(number, pin) VALUES " +
                    "(" + card.getNumber() + ", " + card.getPin() + ")");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return i;
    }

    public void printTable() {
        try (ResultSet cards = statement.executeQuery("SELECT * FROM card")) {
            while (cards.next()) {
                int id = cards.getInt("id");
                String number = cards.getString("number");
                String pin = cards.getString("pin");
                String balance = cards.getString("balance");

                System.out.printf("id %d%n", id);
                System.out.printf("\tnumber: %s%n", number);
                System.out.printf("\tpin: %s%n", pin);
                System.out.printf("\tbalance: %s%n", balance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getBalance(String card){
        String balance = null;
        try {
            ResultSet rowWithBalance = searchCardInTable(card);
            if(rowWithBalance.next()){
                balance = rowWithBalance.getString("balance");
            } else {
                balance = "Such a card does not exist";
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return Integer.valueOf(balance);
    }

    public ResultSet searchCardInTable(String card){
        ResultSet rowWithCard = null;
        try {
            rowWithCard = statement.executeQuery(
                    "SELECT * FROM card WHERE number = "+ card);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rowWithCard;
    }

    public boolean checkCardInTable(Card card){
        ResultSet rowWithCard = null;
        try {
            rowWithCard = statement.executeQuery(
                    "SELECT * FROM card WHERE number = "+ card.getNumber() + " AND "
                            + "pin = " + card.getPin());
            if (rowWithCard.next())
                return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkCardInTableWithoutPin(String card){
        ResultSet rowWithCard = null;
        try {
            rowWithCard = statement.executeQuery("SELECT * FROM card WHERE number = "+ card);
            if (rowWithCard.next())
                return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public String updateBalance(int changeBalanceTo, String cardNumber){
        String resultOperation = null;
        String updateBalance = "UPDATE card SET balance = ? WHERE number = ?";
        try(PreparedStatement statementBalance = connection.prepareStatement(updateBalance)){
            statementBalance.setString(1, String.valueOf(getBalance(cardNumber) + changeBalanceTo));
            statementBalance.setString(2, cardNumber);
            statementBalance.executeUpdate();
            resultOperation = "Income was added!";
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultOperation;
    }

    public String updateBalanceWithoutPin(int changeBalanceTo, String card){
        String resultOperation = null;
        String updateBalance = "UPDATE card SET balance = ? WHERE number = ?";
        try(PreparedStatement statementBalance = connection.prepareStatement(updateBalance)){
            statementBalance.setString(1, String.valueOf(getBalance(card) + changeBalanceTo));
            statementBalance.setString(2, card);
            statementBalance.executeUpdate();
            resultOperation = "Income was added!";
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultOperation;
    }

    public void deleteEntyFromTable(Card card){
        String deleteFromTable = "DELETE FROM card WHERE number = ?";
        try(PreparedStatement deleteStatement = connection.prepareStatement(deleteFromTable)){
            deleteStatement.setString(1, card.getNumber());
            deleteStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public boolean doTransfer(String cardNumber, int amountOfMoney, Card card) {
        String selectCard = "SELECT balance FROM card WHERE number = " + card.getNumber();
        String updateCard = "UPDATE card SET balance = ? WHERE number = ?";
        try(ResultSet statementSelectFromTransferCard = statement.executeQuery(selectCard);
            PreparedStatement statementSelectToTransferCard = connection.prepareStatement(updateCard)) {
            int balance =  statementSelectFromTransferCard.getInt("balance");
            if(balance < amountOfMoney) {
                System.out.println("Not enough money!");
                return false;
            }
            updateBalance(amountOfMoney * -1, card.getNumber());
            updateBalanceWithoutPin(amountOfMoney, cardNumber);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public void close(){
        try {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
