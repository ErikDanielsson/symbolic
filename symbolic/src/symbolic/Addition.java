package symbolic;

public class Addition implements ASTNode {
	private ASTNode left;
	private ASTNode right;
	public Addition(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}
	@Override
	public ASTNode diff() {
		return new Addition(left.diff(), right.diff());  
	}

	@Override
	public double eval(double x) {
		return left.eval(x) + right.eval(x);
	}

	@Override
	/**
	 * Returns a deep copy of the node.
	 */
	public ASTNode copy() {
		return new Addition(left.copy(), right.copy());
	}

}
