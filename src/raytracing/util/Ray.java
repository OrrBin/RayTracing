package raytracing.util;

public class Ray {
	Vector3 position;
	Vector3 direction;

	public Ray(Vector3 position, Vector3 angle) {
		super();
		this.position = position;
		this.direction = angle;
	}

	public Vector3 point(double distance) {
		return position.add(direction.multiply(distance));
	}
	
	public Vector3 getPosition() {
		return position.cpy();
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getDirection() {
		return direction.cpy();
	}

	public void setDirection(Vector3 angle) {
		this.direction = angle;
	}
	
	@Override
	public String toString() {
		return String.format("position: %s, direction: %s", position, direction);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ray other = (Ray) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	
	
}
