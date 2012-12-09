package physiks.unittests;

import java.util.ArrayList;
import java.util.List;

import physiks.unittests.tests.*;

public class TestDriver {
	public static void main(String[] args) {
		List<Test> tests = new ArrayList<Test>();
		
		Test a = new PolyBodyNormalsTest();
		Test b = new SatTest();
		Test c = new QuadTreeTest();
		Test d = new PhysUtilsTest();
		Test e = new PastFixesTest();
		
		tests.add(a);
		tests.add(b);
		tests.add(c);
		tests.add(d);
		tests.add(e);
		
		System.out.println("Starting Tests...");
		
		for (Test test : tests) {
			test.setup();
			test.run();
		}
		
		System.out.println("Tests Complete!");
	}
}
