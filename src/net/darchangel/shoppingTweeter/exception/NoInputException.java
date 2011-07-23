package net.darchangel.shoppingTweeter.exception;

public class NoInputException extends Exception {
    private static final long serialVersionUID = -3705089021098729700L;
    private String name;

    public NoInputException(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
