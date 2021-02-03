package symbolic;

public class Cosine extends PureFunction {

	public Cosine() {
		super("cos", x -> Math.cos(x));
	}
	@Override
	public ASTNode diff(ASTNode argument) {
		return new Multiplication(new Num(-1), new Function(argument, new Sine()));
	}

	@Override
	public PureFunction copy() {
		return new Cosine();
	}

}
