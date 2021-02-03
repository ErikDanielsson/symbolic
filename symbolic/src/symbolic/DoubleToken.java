package symbolic;

public class DoubleToken implements Token {
	private double value;
	public DoubleToken(double value) {
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
