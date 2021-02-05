package symbolic;

public class Variable implements ASTNode {
	private String name;

	public Variable(String name) {
		this.name = name;
	}

	@Override
	public ASTNode diff(String var) {
		if (var.equals(name)) {
			return new Double_(1);
		} else {
			return new Variable(name);
		}
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		if (var.equals(name)) {
			return x.copy();
		} else {
			return this.copy();
		}
	}

	@Override
	public ASTNode copy() {
		return new Variable(name);
	}

	@Override
	public String toString() {
		return name;
	}
}
