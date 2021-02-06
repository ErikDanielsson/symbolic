package symbolic;

public class Negate implements ASTNode {
	private ASTNode expr;
	
	public Negate(ASTNode expr) {
		this.expr = expr;
	}

	@Override
	public ASTNode diff(String var) {
		return new Negate(expr.diff(var));
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		ASTNode newExpr = expr.eval(var, x);
		if (newExpr instanceof Negate) {
			Negate neg = (Negate) newExpr;
			return neg.getExpr();
		} else {
			return new Negate(newExpr);
		}
	}

	public ASTNode getExpr() {
		return expr;
	}
	@Override
	public ASTNode copy() {
		return new Negate(expr.copy());
	}
	
	@Override
	public String toString() {
		return "(-(" + expr.toString() + "))";
	}
	@Override
	public boolean equal(Object other) {
		if (!(other instanceof ASTNode))
			throw new IllegalArgumentException();
		if (!(other instanceof Differentiate))
			return false;
		Negate otherNeg = (Negate) other;
		return otherNeg.expr.equal(expr);
	}

}
