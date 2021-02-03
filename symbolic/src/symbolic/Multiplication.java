package symbolic;

public class Multiplication implements ASTNode {
	private ASTNode left;
	private ASTNode right;
	
	public Multiplication(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public ASTNode diff() {
		return new Addition(new Multiplication(left.diff(), right.copy()), new Multiplication(left.copy(), right.diff()));
	}

	@Override
	public double eval(double x) {
		return left.eval(x) * right.eval(x);
	}

	@Override
	public ASTNode copy() {
		return new Multiplication(left.copy(), right.copy());
	}

}
