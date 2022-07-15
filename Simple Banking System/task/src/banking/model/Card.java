package banking.model;

public class Card {
    private String number;
    private String pin;

    public Card(){
    }

    public boolean equals(Card card){
        return this.number.equals(card.getNumber()) && this.pin.equals(card.getPin());
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
