import java.util.Objects;

public class GridNode {
	int x, y;

	GridNode(int x, int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GridNode gridNode = (GridNode) o;
		return x == gridNode.x && y == gridNode.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}