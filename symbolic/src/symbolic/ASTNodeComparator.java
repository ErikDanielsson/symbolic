package symbolic;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ASTNodeComparator implements Comparator<ASTNode> {
	private static Map<Class<? extends ASTNode>, Integer> valMap;
	static {
		valMap = new HashMap<>();
		valMap.put(Addition.class, 5);
		valMap.put(Constant.class, 2);
		valMap.put(Division.class, 6);
		valMap.put(Double_.class, 1);
		valMap.put(Evaluate.class, 4);
		valMap.put(Evaluate.class, 4);
		valMap.put(Function_.class, 4);
		valMap.put(Multiplication.class, 5);
		valMap.put(Negate.class, 1);
		valMap.put(Variable.class, 3);
	}

	private static Map<Class<? extends ASTNode>, BiFunction<ASTNode, ASTNode, Integer>> compMap;
	static {
		compMap = new HashMap<>();
		compMap.put(Addition.class, (x, y) -> compAddition(x, y));
		compMap.put(Constant.class, (x, y) -> compConstant(x, y));
		compMap.put(Division.class, (x, y) -> compDivision(x, y));
		compMap.put(Double_.class, (x, y) -> compDouble_(x, y));
		compMap.put(Differentiate.class, (x, y) -> compDiffereniate(x, y));
		compMap.put(Evaluate.class, (x, y) -> compEvaluate(x, y));
		compMap.put(Function_.class, (x, y) -> compFunction_(x, y));
		compMap.put(Multiplication.class, (x, y) -> compMultiplication(x, y));
		compMap.put(Negate.class, (x, y) -> compNegate(x, y));
		compMap.put(Variable.class, (x, y) -> compVariable(x,y));
	}

	private static int compAddition(ASTNode fst, ASTNode snd) {
		return 0;
		/*
		Addition arg1 = (Addition) fst;
		Addition arg2 = (Addition) snd;
		*/
	}

	private static int compConstant(ASTNode fst, ASTNode snd) {
		Constant arg1 = (Constant) fst;
		Constant arg2 = (Constant) snd;
		return arg1.getType().compareTo(arg2.getType());
	}
	
	private static int compDivision(ASTNode fst, ASTNode snd) {
		/*
		Division arg1 = (Division) fst;
		Division arg2 = (Division) snd;
		*/
		return 0;
	}
	
	private static int compDouble_(ASTNode fst, ASTNode snd) {
		/*
		Double_ arg1 = (Double_) fst;
		Double_ arg2 = (Double_) snd;
		*/
		return 0;
	}

	private static int compDiffereniate(ASTNode fst, ASTNode snd) {
		/*
		Differentiate arg1 = (Differentiate) fst;
		Differentiate arg2 = (Differentiate) snd;
		*/
		return 0;
	}

	private static int compEvaluate(ASTNode fst, ASTNode snd) {
		/*
		Evaluate arg1 = (Evaluate) fst;
		Evaluate arg2 = (Evaluate) snd;
		*/
		return 0;
	}
	/*
	 * Replace with perfect hash later...
	 */
	private static Map<String, Integer> funcMap;
	static {
		funcMap = new HashMap<>();
		funcMap.put("power", 0);
		funcMap.put("cos", 1);
		funcMap.put("sin", 2);
		funcMap.put("exp", 3);
		funcMap.put("ln", 4);
	}

	private static int compFunction_(ASTNode fst, ASTNode snd) {
		Function_ arg1 = (Function_) fst;
		Function_ arg2 = (Function_) snd;
		return funcMap.get(arg2.getFuncName()) - funcMap.get(arg1.getFuncName());
	}

	private static int compMultiplication(ASTNode fst, ASTNode snd) {
		/*
		Multiplication arg1 = (Multiplication) fst;
		Multiplication arg2 = (Multiplication) snd;
		*/
		return 0;
	}

	private static int compNegate(ASTNode fst, ASTNode snd) {
		Negate arg1 = (Negate) fst;
		Negate arg2 = (Negate) snd;
		return _compare(arg1.getExpr(), arg2.getExpr());
	}

	private static int compVariable(ASTNode fst, ASTNode snd) {
		Variable arg1 = (Variable) fst;
		Variable arg2 = (Variable) snd;
		return arg1.getName().compareTo(arg2.getName());
	}

	private static int _compare(ASTNode arg0, ASTNode arg1) {
		if (arg0.getClass() != arg1.getClass())
			return valMap.get(arg1.getClass()) - valMap.get(arg0.getClass()); 
		else {
			return compMap.get(arg0.getClass()).apply(arg0, arg1);
		}
	}
	@Override
	public int compare(ASTNode arg0, ASTNode arg1) {
		return -_compare(arg0, arg1);
	}


}
