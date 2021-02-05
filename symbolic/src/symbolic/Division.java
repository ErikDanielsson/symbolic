package symbolic;

public class Division implements ASTNode {
	private ASTNode left;
	private ASTNode right;
	
	public Division(ASTNode right, ASTNode left) {
		this.left = left;
		this.right = right;
	}
	@Override
	public ASTNode diff(String var) {
		return new Division(
				new Addition(
					new Multiplication(left.diff(var),right.copy()), 
					new Multiplication(left.copy(), right.diff(var)), 
					true), 
				new Function_(right.copy(), new Power(2)));
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		ASTNode newLeft = left.eval(var, x);
		ASTNode newRight = right.eval(var, x);
		if (newLeft instanceof Double_ && newRight instanceof Double_) {
				Double_ d1 = (Double_) newLeft;
				Double_ d2 = (Double_) newRight;
				return new Double_(d1.getValue() / d2.getValue());
		} else {
			return new Division(newLeft, newRight);
		}
	}

	@Override
	public ASTNode copy() {
		return new Division(left.copy(), right.copy());
	}

	@Override
	public String toString() {
		return left.toString() + " / " + right.toString();
	}
}
