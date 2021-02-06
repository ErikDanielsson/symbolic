package symbolic;

public class Constant implements ASTNode {
	private ConstantType type;
	private String name;
	private double value;

	public Constant(ConstantType type) {
		this.type = type;
		switch (type) {
			case E:
				this.name = "e";
				value = Math.E;
				return;
			case PI:
				this.name = "\u03C0";
				value = Math.PI;
				return;
			case EULER_MASCHERONI:
				this.name = "\u03B3";
				value = 0.5772156649;
				return;
			case GOLDEN_RATIO:
				this.name = "\u03C6";
				value = (Math.sqrt(5)+1)/2;
				return;
		}
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
		return new Constant(this.type);
	}

	@Override
	public String toString() {
		return name;
	}
	
	public ConstantType getType() {
		return type;
	}
	
	public double getValue() {
		return value;
	}

	@Override
	public boolean equal(Object other) {
		if (!(other instanceof ASTNode))
			throw new IllegalArgumentException();
		if (!(other instanceof Constant))
			return false;
		return ((Constant)(other)).getType() == type;
	}
}
