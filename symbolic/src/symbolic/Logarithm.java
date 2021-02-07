package symbolic;

import java.util.ArrayList;
import java.util.List;

public class Logarithm extends PureFunction {

	public Logarithm() {
		super("ln", x -> (Math.log(x)));
	}

	@Override
	public ASTNode diff(ASTNode argument) {
		return new Division(new Double_(1), argument.copy());
	}

	@Override
	public ASTNode eval(ASTNode arg) {
		if (arg instanceof Function_) {
			Function_ func = (Function_) arg;
			if (func.getFuncName().equals("exp"))
				return func.getArg();
			if (func.getFuncName().equals("power")) {
				Power p = (Power)(func.getFunction());
				return new Multiplication(new Double_(p.getDeg()), new Function_(func.getArg(), new Logarithm()));
			}
		} else if (arg instanceof Multiplication) {
			Multiplication mult = (Multiplication) arg;
			List<ASTNode> adds = new ArrayList<>();
			for (ASTNode node : mult)
				adds.add((new Function_(node, new Logarithm())).eval("", null));
			return new Addition(adds);
		}
		return super.eval(arg);
	}
	@Override
	public PureFunction copy() {
		return new Logarithm();
	}
}
