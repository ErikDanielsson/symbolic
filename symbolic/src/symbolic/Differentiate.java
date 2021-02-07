package symbolic;

public class Differentiate implements ASTNode {
	private ASTNode expr;
	private String var;
	public Differentiate(String var, ASTNode expr) {
		this.var = var;
		this.expr = expr;
	}
	@Override
	public ASTNode eval(String var, ASTNode x) {
		ASTNode diffExpr = expr.eval("", null).diff(this.var);
		return diffExpr.eval(var, x);
	}

	@Override
	public ASTNode copy() {
		return new Differentiate(var, expr.copy());
	}

	@Override
	public ASTNode diff(String var) {
		return new Differentiate(var, expr.diff(var));
	}

	@Override
	public String toString() {
		return "diff(" + var + ", " + expr.toString() + ")";
	}
	
	@Override
	public boolean equal(Object other) {
		if (!(other instanceof ASTNode))
			throw new IllegalArgumentException();
		if (!(other instanceof Differentiate))
			return false;
		Differentiate otherDiff = (Differentiate) other;
		return otherDiff.var.equals(var) && otherDiff.expr.equals(expr);
	}

}
