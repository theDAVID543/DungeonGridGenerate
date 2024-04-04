public class RoomInstance{
	int x, y, width, height;

	RoomInstance(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	boolean collidesWith(RoomInstance other) {
		return !(x + width + 1 <= other.x || x >= other.x + other.width + 1 ||
				y + height + 1 <= other.y || y >= other.y + other.height + 1);
	}

	GridNode getCenter() {
		return new GridNode(x + width / 2, y + height / 2);
	}
}
