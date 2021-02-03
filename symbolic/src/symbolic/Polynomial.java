package symbolic;

public class Polynomial implements ASTNode {
	int deg;

	public Polynomial(int deg) {
		this.deg = deg;
	}
	@Override
	public ASTNode diff() {
		if (deg == 1) {
			return new Num(1);
		} else {
			return new Multiplication(new Num(deg), new Polynomial(deg-1));
		}
	}

	@Override
	public double eval(double x) {
		return Math.pow(x, deg);
	}

	@Override
	public ASTNode copy() {
		return new Polynomial(deg);
	}


}
