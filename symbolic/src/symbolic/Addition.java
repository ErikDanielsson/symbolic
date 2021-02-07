package symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Addition implements ASTNode {
	private List<ASTNode> additions;

	public Addition(ASTNode fst, ASTNode snd, boolean isSubtraction) {
		additions = new ArrayList<>();
		additions.add(fst);
		if (isSubtraction)
			additions.add(new Negate(snd));
		else
			additions.add(snd);
	}

	public Addition(List<ASTNode> additions) {
		this.additions = additions;
	}
	
	public void add(ASTNode elem, boolean isSubtraction) {
		if (isSubtraction)
			additions.add(new Negate(elem));
		else
			additions.add(elem);
	}
	
	@Override
	public ASTNode diff(String var) {
		List<ASTNode> diffList = new ArrayList<>();
		for (ASTNode node : additions)
			diffList.add(node.diff(var));
		return (new Addition(diffList)).eval("", null);
	}

	private Tuple<ASTNode, Boolean> get(List<ASTNode> l, int i) {
		return Negate.getExpr(l.get(i));
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		List<ASTNode> newAdditions = new ArrayList<>();
		for (ASTNode node : additions)
			newAdditions.add(node.eval(var, x));
		Collections.sort(newAdditions, new ASTNodeComparator());
		Tuple<ASTNode, Boolean> node = get(newAdditions, 0);
		int length = newAdditions.size();
		int i = 0;
		double sum = 0;
		for (; i < length; i++) {
			node = get(newAdditions, i);
			if (node.getO1() instanceof Double_) {
				Double_ d = ((Double_)node.getO1());
				if (node.getO2())
					sum -= d.getValue();
				else
					sum += d.getValue();
			} else {
				break;
			}
		}
		List<ASTNode> newNewList = new ArrayList<>();
		if (sum < 0) {
			newNewList.add(new Negate(new Double_(-sum)));
		} else if (sum > 0) {
			newNewList.add(new Double_(sum));
		}
		if (i < length && node.getO1() instanceof Constant) {
			Constant currConstant = (Constant)(node.getO1());
			ConstantType conType = currConstant.getType();
			int counter = 0;
			for (; i < length && node.getO1() instanceof Constant; i++) {
				node = get(newAdditions, i);
				currConstant = (Constant)(node.getO1());
				ConstantType newType = currConstant.getType();

				if (conType == newType) {
					if (node.getO2())
						counter--;
					else
						counter++;
				} else {
					if (counter > 0) {
						ASTNode temp = new Multiplication(new Double_(counter), new Constant(conType));
						System.out.println(temp);
						newNewList.add(temp);
					} else if (counter < 0) {
						ASTNode temp = new Negate(new Multiplication(new Double_(-counter), new Constant(conType)));
						System.out.println(temp);
						newNewList.add(temp);
					}
					counter = node.getO2() ? -1 : 1;
					conType = newType;
				}
			}
			
			if (counter > 0) {
				ASTNode temp = new Multiplication(new Double_(counter), new Constant(conType));
				newNewList.add(temp);
			} else if (counter < 0) {
				ASTNode temp = new Negate(new Multiplication(new Double_(-counter), new Constant(conType)));
				newNewList.add(temp);
			}
		}
		if (i < length && node.getO1() instanceof Variable) {
			Variable currVariable = (Variable)(node.getO1());
			String varName = currVariable.getName();
			int counter = 0;
			for (; i < length && node.getO1() instanceof Variable; i++) {
				node = get(newAdditions, i);
				currVariable = (Variable)(node.getO1());
				String newName = currVariable.getName();
				if (varName.equals(newName)) {
					if (node.getO2())
						counter--;
					else
						counter++;
				} else {
					if (counter > 0) {
						ASTNode temp = new Multiplication(new Double_(counter), new Variable(varName));
						newNewList.add(temp.eval(var, x));
					} else if (counter < 0) {
						ASTNode temp = new Negate(new Multiplication(new Double_(-counter), new Variable(varName)));
						newNewList.add(temp.eval(var, x));
					}
					counter = node.getO2() ? -1 : 1;
					varName = newName;
				}
			}
			if (counter > 0) {
				ASTNode temp = new Multiplication(new Double_(counter), new Variable(varName));
				newNewList.add(temp.eval(var, x));
			} else if (counter < 0) {
				ASTNode temp = new Negate(new Multiplication(new Double_(-counter), new Variable(varName)));
				newNewList.add(temp.eval(var, x));
			}
		}
			
		for (; i < length; i++)
			newNewList.add(newAdditions.get(i));
		if (newNewList.size() == 1)
			return newNewList.get(0);
		return new Addition(newNewList);
	}

	@Override
	/**
	 * Returns a deep copy of the node.
	 */
	public ASTNode copy() {
		List<ASTNode> newAdditions = new ArrayList<>();
		for (ASTNode node : additions)
			newAdditions.add(node.copy());
		return new Addition(newAdditions);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(additions.get(0).toString());
		for (int i = 1; i < additions.size(); i++) {
			ASTNode node = additions.get(i);
			if (node instanceof Negate) {
				sb.append(" - " + ((Negate)node).getExpr().toString());
			} else {
				sb.append(" + " + node.toString());
			}
		}
		sb.append(")");
		return new String(sb);
	}

	@Override
	public boolean equal(Object other) {
		return false;
	}
}
