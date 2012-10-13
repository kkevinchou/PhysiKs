package collision;

import geometry.Vector2D;

public class SatResult {
	private Vector2D separatingAxis;
	private Vector2D minimumSeparatingVector;
	
	public SatResult() {
		separatingAxis = null;
		minimumSeparatingVector = null;
	}

	public Vector2D getSeparatingAxis() {
		return separatingAxis.copy();
	}

	public void setSeparatingAxis(Vector2D separatingAxis) {
		this.separatingAxis = separatingAxis;
	}

	public Vector2D getMinimumSeparatingVector() {
		return minimumSeparatingVector;
	}

	public void setMinimumSeparatingVector(Vector2D minimumSeparatingVector) {
		this.minimumSeparatingVector = minimumSeparatingVector;
	}
}
