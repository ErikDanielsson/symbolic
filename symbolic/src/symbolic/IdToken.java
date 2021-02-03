package symbolic;

public class IdToken implements Token {
	private String id;
	
	public IdToken(String id) {
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
