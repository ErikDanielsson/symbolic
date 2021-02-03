package symbolic;

public class Lexer {
	public static void main(String[] args) {
		Lexer lexer = new Lexer("diff ( cos(x^2))");
		try {
			Token t = lexer.getToken();
			while (!(t instanceof EOFToken)) {
				System.out.println(t.toString());
				t = lexer.getToken();
			}
		} catch (Exception e) {
			System.out.println("Hejsan");
		}
	}
	String input;
	int index;
	int len;

	public Lexer(String input) {
		this.input = input;
		this.index = 0;
		this.len = input.length() - 1;
	}
	
	public Token getToken() throws Exception {
		while (index != len) {
			char curr = input.charAt(index);
			while (Character.isWhitespace(curr)) {
				index++;
				curr = input.charAt(index);
			}
			if (Character.isAlphabetic(curr)) {
				StringBuilder value = new StringBuilder();
				do { 
					value.append(curr);
					index++;
					curr = input.charAt(index);
				} while (Character.isAlphabetic(input.charAt(index)));
				return new IdToken(value.toString());
			}
			if (Character.isDigit(curr)) {
				int value = 0;
				do {
					value *= 10;
					value += curr - 0x30;
					index++;
					curr = input.charAt(index);
				} while(Character.isDigit(curr));
				if (curr == '.') {
					index++;
					curr = input.charAt(index);
					double dvalue = value;
					int i = -1;
					do {
						dvalue += Math.pow(curr - 0x30, i);
						i--;
						index++;
						curr = input.charAt(index);
					} while(Character.isDigit(curr));
					return new DoubleToken(dvalue);
				} else {
					return new IntToken(value);
				}
			}
			switch(curr) {
				case '(':
				case ')':
				case '+':
				case '-':
				case '*':
				case '/':
				case '^':
				case ',':
					index++;
					return new CharToken(curr);
				default:
					throw new Exception("Unknown token!");
			}
		}
		return new EOFToken();
	}
}
