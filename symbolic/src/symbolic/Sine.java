package symbolic;

public class Sine extends PureFunction {

	public Sine() {
		super("sin", x -> Math.sin(x));
	}
	@Override
	public ASTNode diff(ASTNode argument) {
		return new Function_(argument, new Cosine());
	}

	@Override
	public PureFunction copy() {
		return new Sine();
	}
}
