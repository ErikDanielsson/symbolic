package symbolic;

import java.util.function.UnaryOperator;

public abstract class PureFunction {
	String name;
	protected UnaryOperator<Double> function;

	public PureFunction(String name, UnaryOperator<Double> function) {
		this.name = name;
		this.function = function;
	}
	
	public abstract ASTNode diff(ASTNode argument);
	
	public ASTNode eval(ASTNode arg) {
		if (arg instanceof Double_) {
			Double_ value = (Double_) arg;
			return new Double_(function.apply(value.getValue()));
		} else if (arg instanceof Constant) {
			return new Double_(function.apply(((Constant)arg).getValue()));
		} else {
			return new Function_(arg.copy(), this.copy());
		}
	}
	
	public abstract PureFunction copy();
	
	public String toString() {
		return name;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof PureFunction)) {
			throw new IllegalArgumentException();
		}
		return ((PureFunction)other).name.equals(name);
	}
}
