package symbolic;

public class Power extends PureFunction {
	double deg;

	public Power(double deg) {
		super("power", x -> Math.pow(x, deg));
		this.deg = deg;
	}

	@Override
	public ASTNode diff(ASTNode argument) {
		if (deg == 1) {
			return new Double_(1);
		} else {
			return new Multiplication(new Double_(deg), new Function_(argument.copy(), new Power(deg-1)));
		}
	}

	@Override
	public PureFunction copy() {
		return new Power(deg);
	}
	
	@Override
	public String toString() {
		return "^" + deg;
	}
}
