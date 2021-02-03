package symbolic;

public class ParseException extends RuntimeException {
	public ParseException(String msg, Throwable err) {
		super(msg, err);
	}

}
