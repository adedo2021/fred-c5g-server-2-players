package ie.game.fred.model;

public enum C5Token {

	X('X'), O('O');

	public char asChar() {
		return asChar;
	}

	private final char asChar;

	C5Token(char asChar) {
		this.asChar = asChar;
	}

}
