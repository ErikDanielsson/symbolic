package symbolic;

public class Logarithm extends PureFunction {

	public Logarithm() {
		super("ln", x -> (Math.log(x)));
	}

	@Override
	public ASTNode diff(ASTNode argument) {
		return new Division(new Double_(1), argument.copy());
	}

	@Override
	public PureFunction copy() {
		return new Logarithm();
	}
}
