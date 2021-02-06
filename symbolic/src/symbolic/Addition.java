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

	private Addition(List<ASTNode> additions) {
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
		return new Addition(diffList);
	}

	@Override
	public ASTNode eval(String var, ASTNode x) {
		List<ASTNode> newAdditions = new ArrayList<>();
		for (ASTNode node : additions)
			newAdditions.add(node.eval(var, x));
		Collections.sort(newAdditions, new ASTNodeComparator());
		int length = newAdditions.size();
		int i = 0;
		double sum = 0;
		for (; i < length; i++) {
			ASTNode node = newAdditions.get(i);
			if (node instanceof Double_) {
				Double_ d = ((Double_)node);
				sum += d.getValue();
			} else if (node instanceof Negate) {
				Double_ d  = ((Double_)((Negate)node).getExpr());
				sum -= d.getValue();
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

		if (i < length && newAdditions.get(i) instanceof Constant) {
			Constant currConstant = (Constant)(newAdditions.get(i));
			ConstantType conType = currConstant.getType();
			int counter = 0;
			for (; i < length && newAdditions.get(i) instanceof Constant; i++) {
				currConstant = (Constant)(newAdditions.get(i));
				ConstantType newType = currConstant.getType();
				if (conType == newType) {
					counter++;
				} else {
					ASTNode node = new Multiplication(new Double_(counter), new Constant(conType));
					counter = 1;
					newNewList.add(node.eval(var, x));
					conType = newType;
				}
			}
			if (counter > 0) {
				ASTNode node = new Multiplication(new Double_(counter), new Constant(conType));
				newNewList.add(node.eval(var, x));
			}
		}

		if (i < length && newAdditions.get(i) instanceof Variable) {
			Variable currVariable = (Variable)(newAdditions.get(i));
			String varName = currVariable.getName();
			int counter = 0;
			for (; i < length && newAdditions.get(i) instanceof Variable; i++) {
				currVariable = (Variable)(newAdditions.get(i));
				String newName = currVariable.getName();
				if (varName.equals(newName)) {
					counter++;
				} else {
					ASTNode node = new Multiplication(new Double_(counter), new Variable(varName));
					counter = 1;
					newNewList.add(node.eval(var, x));
					varName = newName;
				}
			}
			if (counter != 0) {
				ASTNode node = new Multiplication(new Double_(counter), new Variable(varName));
				newNewList.add(node.eval(var, x));
			}
		}
		for (; i < length; i++)
			newNewList.add(newAdditions.get(i));
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
