package symbolic;

public class Function_ implements ASTNode {
	private ASTNode argument;
	private PureFunction function;
	
	public Function_(ASTNode argument, PureFunction function) {
		this.argument = argument;
		this.function = function;
	}
	@Override
	public ASTNode diff(String var) {
		return new Multiplication(argument.diff(var), function.diff(argument.copy())); 
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		return function.eval(argument.eval(var, x));
	}

	@Override
	public ASTNode copy() {
		return new Function_(argument.copy(), function.copy());
	}

	@Override
	public String toString() {
		if (function instanceof Power) {
			return argument.toString() + function.toString();
		}
		return function.toString() + "(" + argument.toString() + ")";
	}
}
