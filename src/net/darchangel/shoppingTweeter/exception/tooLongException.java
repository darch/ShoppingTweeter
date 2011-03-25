package net.darchangel.shoppingTweeter.exception;

public class tooLongException extends Exception {

	private static final long serialVersionUID = 7433358933344934785L;
	private int length = 0;

	public tooLongException(int length) {
		this.length = length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}
}
