package symbolic;

public class CharToken extends Token {
	private char value;
	public CharToken(char value, TokenType type) {
		super(type);
		this.value = value;
	}
	
	public String toString() {
		return Character.toString(value);
	}
	
	public char getValue() {
		return value; 
	}
}