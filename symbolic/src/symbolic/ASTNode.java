package symbolic;

public interface ASTNode {
	public ASTNode diff(String var);
	public ASTNode eval(String var, ASTNode x);
	public ASTNode copy();
	public String toString();
}
