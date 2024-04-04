import java.util.HashSet;
import java.util.Set;

public class RoomInstance{
	int x, y, width, height;
	Set<GridNode> doors = new HashSet<>();

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
	void randGenerateDoor(){
		int direction = (int) (Math.random() * 4);
		if(direction == 0){
			addDoor(new GridNode((int)(Math.random() * (width - 1)), -1));
		}else if(direction == 1){
			addDoor(new GridNode((int)(Math.random() * (width - 1)), height));
		}else if(direction == 2){
			addDoor(new GridNode(-1, (int)(Math.random() * (height - 1))));
		}else{
			addDoor(new GridNode(width, (int)(Math.random() * (height - 1))));
		}
	
	}
	void addDoor(GridNode doorLocation){
		doors.add(doorLocation);
	}
	Set<GridNode> getDoors(){
		return doors;
	}
	Set<GridNode> getDoorsPosition(){
		Set<GridNode> doorPositions = new HashSet<>();
		doors.forEach(door -> {
			doorPositions.add(new GridNode(door.x + x, door.y + y));
		});
		return doorPositions;
	}
}
