package symbolic;

import java.util.function.UnaryOperator;

public abstract class PureFunction {
	String name;
	private UnaryOperator<Double> function;

	public PureFunction(String name, UnaryOperator<Double> function) {
		this.name = name;
		this.function = function;
	}
	
	public abstract ASTNode diff(ASTNode argument);
	
	public double eval(double arg) {
		return function.apply(arg);
	}
	
	public abstract PureFunction copy();
}
