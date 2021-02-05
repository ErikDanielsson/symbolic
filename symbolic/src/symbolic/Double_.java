package symbolic;

public class Double_ implements ASTNode {
	private double value;
	
	public Double_(double value) {
		this.value = value;
	}
	@Override
	public ASTNode diff(String var) {
		return new Double_(0);
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		return new Double_(value);
	}

	@Override
	/**
	 * Returns a copy of the node.
	 */
	public ASTNode copy() {
		return new Double_(value);
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}
}
