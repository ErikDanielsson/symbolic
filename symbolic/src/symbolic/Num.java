package symbolic;

public class Num implements ASTNode {
	public double value;
	
	public Num(double value) {
		this.value = value;
	}
	@Override
	public ASTNode diff() {
		return new Num(0);
	}

	@Override
	public double eval(double x) {
		return value;
	}

	@Override
	/**
	 * Returns a copy of the node.
	 */
	public ASTNode copy() {
		return new Num(value);
	}

}
