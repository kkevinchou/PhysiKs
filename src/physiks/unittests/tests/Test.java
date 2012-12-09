package physiks.unittests.tests;

public abstract class Test {
	private int assertionNumber;
	
	protected Test() {
		assertionNumber = 0;
	}
	
	protected final void tAssert (Object a, Object b) {
		if ((a == null) && (b == null)) {
			System.out.println("[" + assertionNumber++ + "] .");
		} else if ((a != null) && a.equals(b)) {
			System.out.println("[" + assertionNumber++ + "] .");
		} else {
			System.out.println("[" + assertionNumber++ + "] Assertion Failed! | Given: " + a + ", Expecting: " + b);
		}
	}
	
	protected final void tNotAssert (Object a, Object b) {
		if ((a == null) && (b == null)) {
			System.out.println("[" + assertionNumber++ + "] Not Assertion Failed! | Given: " + a);
		} else if ((a != null) && a.equals(b)) {
			System.out.println("[" + assertionNumber++ + "] Not Assertion Failed! | Given: " + a);
		} else {
			System.out.println("[" + assertionNumber++ + "] .");
		}
	}
	
	public abstract void setup();
	public abstract void run();
}