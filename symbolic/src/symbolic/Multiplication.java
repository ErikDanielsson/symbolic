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
		if (newLeft instanceof Negate) {
			Negate n1 = (Negate) newLeft;
			if (newRight instanceof Negate) {
				Negate n2 = (Negate) newRight;
				ASTNode temp = new Multiplication(n1.getExpr(), n2.getExpr());
				return temp.eval("", null);
			} else {
				ASTNode temp = new Multiplication(n1.getExpr(), newRight);
				temp = temp.eval("", null);
				return new Negate(temp);
			}
		} else if (newRight instanceof Negate) {
			Negate n2 = (Negate) newRight;
			ASTNode temp = new Multiplication(newLeft, n2.getExpr());
			temp = temp.eval("", null);
			return new Negate(temp);
		}
		if (newLeft instanceof Double_) {
				Double_ d1 = (Double_) newLeft;
				if (d1.getValue() == 0)
					return new Double_(0);
				if (newRight instanceof Double_) {
					Double_ d2 = (Double_) newRight;
					return new Double_(d1.getValue() * d2.getValue());
				} else if (d1.getValue() == 1) {
					return newRight.copy();
				}
		} else if (newRight instanceof Double_) {
			Double_ d2 = (Double_) newRight;
			if (d2.getValue() == 0)
				return new Double_(0);
			else if (d2.getValue() == 1) {
				return newLeft.copy();
			} else {
				return new Multiplication(newRight, newLeft);
			}

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
	@Override
	public boolean equal(Object other) {
		if (!(other instanceof ASTNode))
			throw new IllegalArgumentException();
		if (!(other instanceof Differentiate))
			return false;
		return false;
	}

}
