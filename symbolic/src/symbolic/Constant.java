package symbolic;

public class Constant implements ASTNode {
	String name;
	Double value;
	public Constant(String name) {
		switch (name) {
			case "e":
				this.name = name;
				value = Math.E;
				return;
			case "pi":
				this.name = "\u03C0";
				value = Math.PI;
				return;
			case "em":
				this.name = "\u1D6FE";
				value = 0.5772156649;
				return;
			case "phi":
				this.name = "\u03C6";
				value = (Math.sqrt(5)+1)/2;
				return;
			default:
				return;
		}
	}
	private Constant(String name, double value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public ASTNode diff(String var) {
		return new Double_(0);
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		return this.copy();
	}
	
	@Override
	public ASTNode copy() {
		return new Constant(new String(this.name),this.value);
	}

	@Override
	public String toString() {
		return name;
	}
}
