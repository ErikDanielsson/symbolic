package symbolic;

public class Power extends PureFunction {
	private double deg;

	public Power(double deg) {
		super("power", x -> Math.pow(x, deg));
		this.deg = deg;
	}
	
	public double getDeg() {
		return deg;
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
	public ASTNode eval(ASTNode arg) {
		if (deg == 1) 
			return arg;
		if (arg instanceof Double_)
			return new Double_(function.apply(((Double_)arg).getValue()));
		else 
			return new Function_(arg, new Power(deg));
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
