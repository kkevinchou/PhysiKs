package physiks.collision;

import physiks.geometry.Vector2D;

public final class SatResult {
	private Vector2D separatingAxis;
	private Vector2D minimumSeparatingVector;
	
	public SatResult(Vector2D separatingAxis, Vector2D minimumSeparatingVector) {
		this.separatingAxis = separatingAxis;
		this.minimumSeparatingVector = minimumSeparatingVector;
	}

	public Vector2D getSeparatingAxis() {
		return separatingAxis;
	}

	public Vector2D getMinimumSeparatingVector() {
		return minimumSeparatingVector;
	}
}
