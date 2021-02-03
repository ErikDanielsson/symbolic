package symbolic;

public class Tuple<S1, S2> {
	private S1 o1;
	private S2 o2;
	public Tuple(S1 o1, S2 o2) {
		this.o1 = o1;
		this.o2 = o2;
	}
	
	public S1 getO1() {
		return o1;
	}
	
	public S2 getO2() {
		return o2;
	}
}
