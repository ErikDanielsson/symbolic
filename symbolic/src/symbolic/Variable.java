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
			return new Double_(0);
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

	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equal(Object other) {
		if (!(other instanceof ASTNode))
			throw new IllegalArgumentException();
		if (!(other instanceof Differentiate))
			return false;
		Variable otherVar = (Variable) other;
		return otherVar.name.equals(name);
	}

}
