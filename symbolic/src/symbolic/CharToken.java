package symbolic;

public class CharToken implements Token {
	private char value;
	public CharToken(char value) {
		this.value = value;
	}
	
	public String toString() {
		return Character.toString(value);
	}
	
	public char getValue() {
		return value; 
	}
}