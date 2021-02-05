package symbolic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Parser for symbolic expression 
 * @author Erik Danielsson
 * Grammar:
 * expr -> diff '(' ID ',' expr ')'
 *		 | eval '(' subList ';' expr ')' 
 *		 | term '+' term
 *		 | term '-' term
 *		 | term  
 *
 * subList -> sub, subList | sub
 * sub -> '(' ID '=' expr ')'  
 * 
 * term -> factor '*' factor
 * 		 | factor '/' factor
 * 		 | factor
 * 
 * factor -> exp '^' factor
 * 		   | exp '^' INT
 * 		   | 'e' '^' exp
 * 		   | exp
 * 
 * exp -> ID '(' expr ')'
 * 		| (' expr ')'
 * 		| DOUBLE
 * 		| INT
 * 		| ID
 */

public class Parser {
	private Lexer lexer;
	private Token currToken;
	private Deque<Token> pastBuffer;
	private Deque<Token> newBuffer;
	private static Map<String, Function<Void, PureFunction>> functions;
	static {
		functions = new HashMap<>();
		functions.put("cos", (Void x) -> new Cosine());
		functions.put("sin", (Void x) -> new Sine());
		functions.put("exp", (Void x) -> new Exponential());
		functions.put("ln" , (Void x) -> new Logarithm());
	}

	private static Map<String, Function<Void, Constant>> constants;
	static {
		constants = new HashMap<>();
		constants.put("e"   , (Void x) -> new Constant("e"));
		constants.put("pi"  , (Void x) -> new Constant("pi"));
		constants.put("em"  , (Void x) -> new Constant("em"));
		constants.put("phi" , (Void x) -> new Constant("phi"));
	}

	public Parser(Lexer lexer) {
		this.lexer = lexer;
		pastBuffer = new ArrayDeque<Token>();
		newBuffer = new ArrayDeque<Token>();
		currToken = lexer.getToken();

	}
	
	private void eat(TokenType expected) {
		if (expected == currToken.getType()) {
			try {
				if (newBuffer.isEmpty()) {
					currToken = lexer.getToken();
				} else {
					currToken = newBuffer.pop();
				}
			} catch (Exception e) {
				throw new LexException("Unknown token!", e);
			}
		} else {
			try {
				throw new Exception("");
			} catch (Exception e) {
				throw new ParseException("Did not expect token: " + currToken.toString(), e);
			}
		}
	}

	private Token peek() {
		pastBuffer.push(currToken);
		currToken = lexer.getToken();
		return currToken;
	}

	private void reversePeek() {
		newBuffer.push(currToken);
		currToken = pastBuffer.pop();
	}

	public ASTNode parse() {
		ASTNode expr = expr();
		eat(TokenType.EOF);
		return expr;
	}
	
	private ASTNode expr() {
		TokenType type = currToken.getType();
		if (type == TokenType.EVAL) {
			eat(TokenType.EVAL);
			eat(TokenType.LPAREN);
			List<Tuple<String, ASTNode>> subs = subList();
			eat(TokenType.SEMI);
			ASTNode expr = expr();
			eat(TokenType.RPAREN);
			return new Evaluate(subs, expr);
		} else if (type == TokenType.DIFF) {
			eat(TokenType.DIFF);
			eat(TokenType.LPAREN);
			IdToken id = (IdToken) currToken;
			eat(TokenType.ID);
			String var = id.getValue();
			eat(TokenType.COMMA);
			ASTNode expr = expr();
			eat(TokenType.RPAREN);
			return new Differentiate(var, expr);
		} else {
			ASTNode node = term();
			type = currToken.getType();
			while (type == TokenType.PLUS || type == TokenType.MINUS) {
				node = new Addition(node, term(), type != TokenType.PLUS);
				type = currToken.getType();
			}
			return node;
		}
	}
	
	private List<Tuple<String, ASTNode>> subList() {
		List<Tuple<String, ASTNode>> l = new ArrayList<>();
		l.add(sub());
		while (currToken.getType() == TokenType.COMMA) {
			eat(TokenType.COMMA);
			l.add(sub());
		}
		return l;
	}
	private Tuple<String, ASTNode> sub() {
		IdToken var = (IdToken) currToken;
		eat(TokenType.ID);
		String name = var.getValue();
		eat(TokenType.EQUAL);
		ASTNode expr = expr();
		return new Tuple<>(name, expr);
	}
	private ASTNode term() {
		ASTNode node = factor();
		TokenType type = currToken.getType();
		while (type == TokenType.MULT || type == TokenType.DIV) {
			if (type == TokenType.MULT) {
				eat(TokenType.MULT);
				node = new Multiplication(node, factor());
			} else {
				eat(TokenType.DIV);
				node = new Division(node, factor());
			}
			type = currToken.getType();
		}
		return node;
	}
	
	private ASTNode	factor() {
		TokenType type = currToken.getType();
		ASTNode node;
		if (type == TokenType.CONSTANT) {
			if (currToken.toString().equals("e")) {
				if (peek().getType() == TokenType.EXP) {
					node = new Function_(exp(), new Exponential());
				} else {
					reversePeek();
					node  = exp();
				}
			} else {
				node = exp();
			}
		} else {
			node = exp();
			type = currToken.getType();
			while (type == TokenType.EXP) {
				eat(TokenType.EXP);
				type = currToken.getType();
				if (type == TokenType.INT) {
					IntToken t = (IntToken) currToken;
					eat(TokenType.INT);
					node = new Function_(node, new Power(t.getValue()));
				} else if (type == TokenType.DOUBLE) {
					DoubleToken t = (DoubleToken) currToken;
					eat(TokenType.DOUBLE);
					node = new Function_(node, new Power(t.getValue()));
				} else {
					node = new Function_(
								new Multiplication(
									exp(), new Function_(node, new Logarithm())),
								new Exponential()
							);
				}
				type = currToken.getType();
			}
		}
		return node;
	}
	
	private ASTNode exp() {
		TokenType type = currToken.getType();
		switch (type) {
			case ID: { 
				IdToken id = (IdToken) currToken;
				eat(TokenType.ID);
				String name = id.getValue();
				if (functions.containsKey(name)) {
					PureFunction function = functions.get(name).apply(null);
					eat(TokenType.LPAREN);
					ASTNode expr = expr();
					eat(TokenType.RPAREN);
					return new Function_(expr, function);
				} else if (constants.containsKey(name)) {
					return constants.get(name).apply(null);
				} else {
					return new Variable(name);
				}
			}
			case DOUBLE: {
				DoubleToken d = (DoubleToken) currToken;
				eat(TokenType.DOUBLE);
				return new Double_(d.getValue());
			}
			case INT: {
				IntToken i = (IntToken) currToken;
				eat(TokenType.INT);
				return new Double_(i.getValue());
			}
			default:
				return null;
		}
	}
}
