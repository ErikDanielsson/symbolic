package symbolic;

import java.util.List;
import java.util.Map;

public class Evaluate implements ASTNode {
	ASTNode expr;
	List<Tuple<String,ASTNode>> substitutions;

	public Evaluate(List<Tuple<String, ASTNode>> substitutions, ASTNode expr) {
		this.substitutions = substitutions;
		this.expr = expr;
	}
	@Override
	public ASTNode diff(String var) {
		ASTNode evalExpr = expr;
		for (Tuple<String, ASTNode> sub : substitutions)
			evalExpr = evalExpr.eval(sub.getO1(), sub.getO2());
		return evalExpr.diff(var);
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		ASTNode evalExpr = expr;
		for (Tuple<String, ASTNode> sub : substitutions)
			evalExpr = evalExpr.eval(sub.getO1(), sub.getO2());
		return evalExpr.eval(var, x);
	}

	@Override
	public ASTNode copy() {
		return new Evaluate(substitutions, expr.copy());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("eval(");
		for (Tuple<String, ASTNode> t : substitutions)
			sb.append(t.getO1() + "=" + t.getO2().toString());
		sb.append(")");
		return sb.toString();
	}
}
