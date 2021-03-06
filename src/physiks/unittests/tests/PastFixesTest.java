package physiks.unittests.tests;

import physiks.collision.SeparatingAxisTest;
import physiks.engine.misc.PhysHelper;
import physiks.entities.RigidBody;
import physiks.geometry.Vector2D;
import physiks.main.PhysiKsSim;
import physiks.main.misc.PhysSimHelper;

public class PastFixesTest extends Test {

	@Override
	public void setup() {
		System.out.println("=== MiscTest ===");
	}

	@Override
	public void run() {
		separatingVectorTest();
//		t2();
	}

	private void separatingVectorTest() {
//		Previous Position:[Vector2D X: 7.86578 Y: 8.50792]
//		Current Position: [Vector2D X: 9.38858 Y: 6.98512]
//		Target Position: [Vector2D X: 4.75992 Y: 22.77066]
//		Separating Vector: [Vector2D X: 1.5228 Y: -1.5228]
				
		RigidBody b1 = PhysSimHelper.createDiamond(7.86578f, 8.50792f, 20, 20, 1);
		RigidBody b2 = PhysSimHelper.createDiamond(4.75992f, 22.77066f, 20, 20, 1);
		
		Vector2D sepVec = PhysHelper.calculateMinimumSeparatingVector(b1, b2);
		b1.setPosition(b1.getPosition().add(sepVec));
		
//		Vector2D sepVec2 = PhysHelper.calculateMinimumSeparatingVector(b1, b2);
//		
//		System.out.println(sepVec2);
		
		Vector2D sepAxis = SeparatingAxisTest.getSeparatingAxis(b1, b2);
		tNotAssert(sepAxis, null);
	}
	
	private void t2() {
//		Previous Position:[Vector2D X: 51.386684 Y: 480.37625]
//		Current Position: [Vector2D X: 51.386684 Y: 480.75247]
//		Target Position: [Vector2D X: 0.0 Y: 500.0]
//		Separating Vector: [Vector2D X: -0.0 Y: 0.3762207]
		RigidBody b1 = PhysSimHelper.createDiamond(51.386684f, 480.37625f, 20, 20, 1);
		RigidBody b2 = PhysSimHelper.createBox(0f, 500f, PhysiKsSim.WIDTH, PhysiKsSim.HEIGHT, 1);
		
		Vector2D sepVec = new Vector2D(-0f, 0.3762207f);
		b1.setPosition(b1.getPosition().add(sepVec));
		
		Vector2D sepAxis = SeparatingAxisTest.getSeparatingAxis(b1, b2);
		tNotAssert(sepAxis, null);
	}
}
