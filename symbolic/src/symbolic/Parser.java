package symbolic;

public class Parser {
	Lexer lexer;
	Token currToken;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	private void eat(Class<?> expected) {
		if (expected.isInstance(currToken)) {
			currToken = lexer.getToken();
		} else {
			try {
				throw new Exception("");
			} catch (Exception e) {
				throw new ParseException("Did not expect token: " + currToken.toString(), e);
			}
		}
	}

	public void doTheParse() {
		if (currToken instanceof IdToken) {
			IdToken temp = (IdToken) currToken;
			if (temp.toString().equals("eval")) {
				eat(IdToken.class);
				ASTNode tree = parse();

				print(tree.eval)
			}
		} else {
			
		}
		Token temp = currToken;

		eat(IdToken.class);
		IdToken idToken
		if (temp.toString().compareTo())
	}
}
