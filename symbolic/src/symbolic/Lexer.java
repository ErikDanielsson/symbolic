package symbolic;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class Lexer {
	private static Map<String, Function<Void, Token>> keywords;
	static {
		keywords = new HashMap<>();
		keywords.put("diff", (Void v) -> new DiffToken());
		keywords.put("eval", (Void v) -> new EvalToken());
	}
	String input;
	int index;
	int len;
	char curr;

	public Lexer(String input) {
		this.input = input;
		this.index = -1;
		this.len = input.length();
		getChar();
	}
	
	private void getChar() {
		index++;
		if (index < len) {
			curr = input.charAt(index);
		} else {
			curr = 0x00;
		}
	}
	
	public Token getToken() {
		while (curr != 0x00) {
			while (Character.isWhitespace(curr)) {
				getChar();
			}
			if (Character.isAlphabetic(curr)) {
				StringBuilder value = new StringBuilder();
				do { 
					value.append(curr);
					getChar();
				} while (Character.isAlphabetic(curr));
				String s = value.toString();
				if (keywords.containsKey(s))
					return keywords.get(s).apply(null);
				else
					return new IdToken(value.toString());
			}
			if (Character.isDigit(curr)) {
				int value = 0;
				do {
					value *= 10;
					value += curr - 0x30;
					getChar();
				} while( index < len && Character.isDigit(curr));
				if (curr == '.') {
					getChar();
					double dvalue = value;
					int i = -1;
					do {
						dvalue += (curr - 0x30) * Math.pow(10, i);
						i--;
						getChar();
					} while(Character.isDigit(curr));
					return new DoubleToken(dvalue);
				} else {
					return new IntToken(value);
				}
			}
			TokenType type;
			switch(curr) {
				case '(':
					type = TokenType.LPAREN;
					break;
				case ')':
					type = TokenType.RPAREN;
					break;
				case '+':
					type = TokenType.PLUS;
					break;
				case '-':
					type = TokenType.MINUS;
					break;
				case '*':
					type = TokenType.MULT;
					break;
				case '/':
					type = TokenType.DIV;
					break;
				case '^':
					type = TokenType.EXP;
					break;
				case ',':
					type = TokenType.COMMA;
					break;
				case '=':
					type = TokenType.EQUAL;
					break;
				case ';':
					type = TokenType.SEMI;
					break;
				default:
					return null;
			}
			getChar();
			return new CharToken(curr, type);
		}
		return new EOFToken();
	}
}
