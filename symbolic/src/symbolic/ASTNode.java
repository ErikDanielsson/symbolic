package symbolic;

public interface ASTNode {
	public ASTNode diff();
	public double eval(double x);
	public ASTNode copy();
}
