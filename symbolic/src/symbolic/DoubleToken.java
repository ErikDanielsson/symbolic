package symbolic;

public class DoubleToken extends Token {
	private double value;
	public DoubleToken(double value) {
		super(TokenType.DOUBLE);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	public double getValue() {
		return value;
	}
}
