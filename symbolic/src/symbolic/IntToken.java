package symbolic;

public class IntToken implements Token {
	private int value;
	public IntToken(int value) {
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
