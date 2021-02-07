package symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Multiplication implements ASTNode, Iterable<ASTNode> {
	private List<ASTNode> mults;
	
	public Multiplication(ASTNode fst, ASTNode snd) {
		mults = new ArrayList<>();
		mults.add(fst);
		mults.add(snd);
	}

	private Multiplication(List<ASTNode> mults) {
		this.mults = mults;
	}
	public void add(ASTNode elem) {
		mults.add(elem);
	}

	@Override
	public ASTNode diff(String var) {
		List<ASTNode> newMults = new ArrayList<>();
		List<ASTNode> additions = new ArrayList<>();
		for (int i = 0; i < mults.size(); i++) {
			for (int j = 0;j < mults.size(); j++) {
				if (i == j)
					newMults.add(mults.get(j).diff(var));
				else
					newMults.add(mults.get(j).copy());
			}
			additions.add(new Multiplication(newMults));
			newMults = new ArrayList<>();
		}
		return new Addition(additions);
	}

	private Tuple<ASTNode, Boolean> get(List<ASTNode> l, int i) {
		return Negate.getExpr(l.get(i));
	}
	private List<ASTNode> getMults() {
		return mults;
	}
	private List<ASTNode> assoc(List<ASTNode> l) {
		List<ASTNode> newL = new ArrayList<>();
		for (int i = 0; i < l.size(); i++) {
			ASTNode elem = l.get(i);
			if (elem instanceof Multiplication) {
				newL.addAll(((Multiplication)(elem)).getMults());
			} else {
				newL.add(elem);
			}
		}
		return newL;
	}
	@Override
	public ASTNode eval(String var, ASTNode x) {
		List<ASTNode> newMults = new ArrayList<>();
		for (ASTNode node : mults)
			newMults.add(node.eval(var, x));
		newMults = assoc(newMults);
		Collections.sort(newMults, new ASTNodeComparator());
		List<ASTNode> newNewMults = new ArrayList<>();
		Tuple<ASTNode, Boolean> node = get(newMults, 0);
		int length = newMults.size();
		int i = 0;
		boolean isNeg = false;;
		double prod = 1;
		for (; i < length; i++) {
			node = get(newMults, i);
			if (node.getO1() instanceof Double_) {
				if (node.getO2())
					isNeg = !isNeg;
				Double_ d = ((Double_)node.getO1());
				prod *= d.getValue();
			} else {
				break;
			}
		}

		if (prod > 0) {
			if (prod != 1 || i == length)
				newNewMults.add(new Double_(prod));
		} else {
			return new Double_(0);
		}
		
		if (i < length && node.getO1() instanceof Constant) {
			Constant currConstant = (Constant)(node.getO1());
			ConstantType conType = currConstant.getType();
			int counter = 0;
			for (; i < length && node.getO1() instanceof Constant; i++) {
				node = get(newMults, i);
				currConstant = (Constant)(node.getO1());
				ConstantType newType = currConstant.getType();
				if (node.getO2())
					isNeg = !isNeg;
				if (conType == newType) {
					counter++;
				} else {
					ASTNode temp = new Function_(new Constant(conType), new Power(counter));
					counter = 1;
					newNewMults.add(temp.eval(var, x));
					conType = newType;
				}
			}
			if (counter > 0) {
				ASTNode temp = new Function_(new Constant(conType), new Power(counter));
				newNewMults.add(temp.eval(var, x));
			}
		}
		
		if (i < length && node.getO1() instanceof Variable) {
			Variable currVariable = (Variable)(node.getO1());
			String varName = currVariable.getName();
			int counter = 0;
			for (; i < length; i++) {
				node = get(newMults, i);
				if (!(node.getO1() instanceof Variable))
					break;
				currVariable = (Variable)(node.getO1());
				String newName = currVariable.getName();
				if (node.getO2())
					isNeg = !isNeg;
				if (varName.equals(newName)) {
					counter++;
				} else {
					ASTNode temp;
					if (counter == 1)
						temp = new Variable(varName);
					else
						temp = new Function_(new Variable(varName), new Power(counter));
					counter = 1;
					newNewMults.add(temp.eval(var, x));
					varName = newName;
				}
			}
			if (counter != 0) {
				ASTNode temp;
				if (counter == 1)
					temp = new Variable(varName);
				else
					temp = new Function_(new Variable(varName), new Power(counter));
				newNewMults.add(temp.eval(var, x));
			}
		}
		
		for (; i < length; i++) {
			node = get(newMults, i);
			if (node.getO2())
				isNeg = !isNeg;
			newNewMults.add(node.getO1());
		}

		if (newNewMults.size() == 1)
			if (isNeg)
				return new Negate(newNewMults.get(0));
			else
				return newNewMults.get(0);
		if (isNeg)
			return new Negate(new Multiplication(newNewMults));
		else
			return new Multiplication(newNewMults);
	}

	@Override
	public ASTNode copy() {
		List<ASTNode> newMults = new ArrayList<>();
		for (ASTNode node : mults)
			newMults.add(node.copy());
		return new Multiplication(newMults);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(" + mults.get(0));
		for (int i = 1; i < mults.size(); i++)
			sb.append(" * " + mults.get(i));
		sb.append(")");
		return new String(sb);
	}

	@Override
	public boolean equal(Object other) {
		if (!(other instanceof ASTNode))
			throw new IllegalArgumentException();
		if (!(other instanceof Differentiate))
			return false;
		return false;
	}

	@Override
	public Iterator<ASTNode> iterator() {
		return mults.iterator();
	}

}
