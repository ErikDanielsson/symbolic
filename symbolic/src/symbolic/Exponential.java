package symbolic;

public class Exponential extends PureFunction {

	public Exponential() {
		super("exp", x -> Math.exp(x));
	}
	@Override
	public ASTNode diff(ASTNode argument) {
		return new Function_(argument, new Exponential());
	}

	@Override
	public ASTNode eval(ASTNode arg) {
		if (arg instanceof Function_) {
			Function_ func = (Function_) arg;
			if (func.getFuncName().equals("ln"))
				return func.getArg();
		}
		return super.eval(arg);
	}
	@Override
	public PureFunction copy() {
		return new Exponential();
	}
}
