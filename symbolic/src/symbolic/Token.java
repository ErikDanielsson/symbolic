package symbolic;

public abstract class Token {
	private TokenType type;

	public Token(TokenType type) {
		this.type = type;
	}

	public TokenType getType() {
		return type;
	}
}
