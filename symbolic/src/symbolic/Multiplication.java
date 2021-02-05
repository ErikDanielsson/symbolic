package symbolic;

public class Multiplication implements ASTNode {
	private ASTNode left;
	private ASTNode right;
	
	public Multiplication(ASTNode left, ASTNode right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public ASTNode diff(String var) {
		return new Addition(new Multiplication(left.diff(var), right.copy()), new Multiplication(left.copy(), right.diff(var)), false);
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		ASTNode newLeft = left.eval(var, x);
		ASTNode newRight = right.eval(var, x);
		if (newLeft instanceof Double_ && newRight instanceof Double_) {
				Double_ d1 = (Double_) newLeft;
				if (d1.getValue() == 0)
					return new Double_(0);
				if (newRight instanceof Double_) {
					Double_ d2 = (Double_) newRight;
					return new Double_(d1.getValue() * d2.getValue());
				}
		} else if (newRight instanceof Double_) {
			Double_ d2 = (Double_) newRight;
			if (d2.getValue() == 0)
				return new Double_(0);

		}
		return new Multiplication(newLeft, newRight);
	}

	@Override
	public ASTNode copy() {
		return new Multiplication(left.copy(), right.copy());
	}

	@Override
	public String toString() {
		return left.toString() + " * " + right.toString();
	}
}
