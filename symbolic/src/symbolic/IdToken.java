package symbolic;

public class IdToken extends Token {
	private String id;
	
	public IdToken(String id) {
		super(TokenType.ID);
		this.id = id;
	}
	@Override
	public String toString() {
		return id;
	}

	public String getValue() {
		return id;
	}
}
