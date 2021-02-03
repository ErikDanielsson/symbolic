package symbolic;

public class Function implements ASTNode {
	private ASTNode argument;
	private PureFunction function;
	
	public Function(ASTNode argument, PureFunction function) {
		this.argument = argument;
		this.function = function;
	}
	@Override
	public ASTNode diff() {
		return new Multiplication(argument.diff(), function.diff(argument.copy())); 
	}

	@Override
	public double eval(double x) {
		return function.eval(argument.eval(x));
	}

	@Override
	public ASTNode copy() {
		return new Function(argument.copy(), function.copy());
	}

}
