package symbolic;

public class Addition implements ASTNode {
	private ASTNode left;
	private ASTNode right;
	private boolean isSubtraction;
	public Addition(ASTNode left, ASTNode right, boolean isSubtraction) {
		this.left = left;
		this.right = right;
		this.isSubtraction = isSubtraction;
	}
	@Override
	public ASTNode diff(String var) {
		return new Addition(left.diff(var), right.diff(var), isSubtraction);  
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		ASTNode newLeft = left.eval(var, x);
		ASTNode newRight = right.eval(var, x);
		if (newLeft instanceof Double_ && newRight instanceof Double_) {
				Double_ d1 = (Double_) newLeft;
				Double_ d2 = (Double_) newRight;
				if (isSubtraction)
					return new Double_(d1.getValue() - d2.getValue());
				else
					return new Double_(d1.getValue() + d2.getValue());
		} else {
			return new Addition(newLeft, newRight, isSubtraction);
		}	
	}

	@Override
	/**
	 * Returns a deep copy of the node.
	 */
	public ASTNode copy() {
		return new Addition(left.copy(), right.copy(), isSubtraction);
	}

	public String toString() {
		if (isSubtraction) {
			return "(" + left.toString() + " - " + right.toString() + ")";
		} else {
			return "(" + left.toString() + " + " + right.toString() + ")";
		}
	}
}
