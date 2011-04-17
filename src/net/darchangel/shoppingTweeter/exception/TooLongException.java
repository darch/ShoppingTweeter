package net.darchangel.shoppingTweeter.exception;

public class TooLongException extends Exception {

	private static final long serialVersionUID = 7433358933344934785L;
	private int length = 0;

	public TooLongException(int length) {
		this.length = length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}
}
