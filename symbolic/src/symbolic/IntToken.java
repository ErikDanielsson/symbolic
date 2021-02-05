package symbolic;

public class IntToken extends Token {
	private int value;
	public IntToken(int value) {
		super(TokenType.INT);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
	public int getValue() {
		return value;
	}
}
