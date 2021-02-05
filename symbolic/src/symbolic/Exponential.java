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
	public PureFunction copy() {
		return new Exponential();
	}
}
