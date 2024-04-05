import java.util.*;
import java.util.List;

public class GrowingRoomGenerator {
	private static final int MAX_ROOM_SIZE = 7;
	private static final int MIN_ROOM_SIZE = 2;
	private static final int MAX_ATTEMPTS = 10;
	public static int QUERY_RANGE = 50;
	private static final int MAX_ROOMS = 50;

	private final List<RoomInstance> roomInstances = new ArrayList<>();

	private boolean roomCollides(RoomInstance roomInstance) {
		for (RoomInstance existingRoomInstance : roomInstances) {
			if (roomInstance.collidesWith(existingRoomInstance)) {
				return true;
			}
		}
		return false;
	}

	public void generateRooms() {
		Random rand = new Random();

		// Start with a smaller initial room centered.
		RoomInstance initialRoomInstance = new RoomInstance(-2, -2, 4, 4);
		initialRoomInstance.randGenerateDoor();
		roomInstances.add(initialRoomInstance);

		List<RoomInstance> activeRoomInstances = new ArrayList<>();
		activeRoomInstances.add(initialRoomInstance);

		while (!activeRoomInstances.isEmpty() && roomInstances.size() < MAX_ROOMS) {
			RoomInstance current = activeRoomInstances.get(rand.nextInt(activeRoomInstances.size()));

			boolean roomAdded = false;
			for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
				int newWidth = rand.nextInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE) + 1;
				int newHeight = rand.nextInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE) + 1;

				// Determine side
				int side = rand.nextInt(4);
				int newX = side == 0 ? current.x + rand.nextInt(current.width) :
						side == 1 ? current.x + current.width + 1 :
								side == 2 ? current.x + rand.nextInt(current.width) - newWidth :
										current.x - newWidth - 1;

				int newY = side == 0 ? current.y - newHeight - 1 :
						side == 1 ? current.y + rand.nextInt(current.height) :
								side == 2 ? current.y + current.height + 1 :
										current.y + rand.nextInt(current.height) - newHeight;

				RoomInstance newRoomInstance = new RoomInstance(newX, newY, newWidth, newHeight);
				if (!roomCollides(newRoomInstance)) {
//					for(int i = 0; i < rand.nextInt(3); i++){
//						newRoomInstance.randGenerateDoor();
//					}
					newRoomInstance.randGenerateDoor();
					roomInstances.add(newRoomInstance);
					activeRoomInstances.add(newRoomInstance);
					roomAdded = true;
					break;
				}
			}

			if (!roomAdded) {
				activeRoomInstances.remove(current);
			}
		}
	}
	private void getMinQueryRange(){
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for(RoomInstance roomInstance : roomInstances){
			if(roomInstance.x < minX){
				minX = roomInstance.x;
			}
			if(roomInstance.y < minY){
				minY = roomInstance.y;
			}
			if(roomInstance.x + roomInstance.width > maxX){
				maxX = roomInstance.x + roomInstance.width;
			}
			if(roomInstance.y + roomInstance.height > maxY){
				maxY = roomInstance.y + roomInstance.height;
			}
		}
		System.out.println("MinX: " + minX + " MinY: " + minY + " MaxX: " + maxX + " MaxY: " + maxY);
		QUERY_RANGE = Math.max(Math.max(Math.abs(minX), Math.abs(minY)), Math.max(Math.abs(maxX), Math.abs(maxY)));
	}

	public void displayGrid() {
		for (int i = -QUERY_RANGE; i < QUERY_RANGE; i++) {
			for (int j = -QUERY_RANGE; j < QUERY_RANGE; j++) {
				boolean occupied = false;
				for (RoomInstance roomInstance : roomInstances) {
					if (j >= roomInstance.x && j < roomInstance.x + roomInstance.width && i >= roomInstance.y && i < roomInstance.y + roomInstance.height) {
						occupied = true;
						break;
					}
				}
				System.out.print(occupied ? " # " : " . ");
			}
			System.out.println();
		}
	}
	public static Set<GridNode> path = new HashSet<>();

	public static void main(String[] args) {
		for(int e = 0; e < 1; e++){
			GrowingRoomGenerator generator = new GrowingRoomGenerator();
			generator.generateRooms();
			generator.displayGrid();
			path = new HashSet<>();
			Graph graph = new Graph(generator.roomInstances);
			List<Edge> mstEdges = graph.primsMST();
			mstEdges.forEach(v -> {
				System.out.println("Edge from: (" + v.source.x + ", " + v.source.y + ") to (" + v.destination.x + ", " + v.destination.y + ")");
				AStar aStar = new AStar(generator.roomInstances, new GridNode(v.source.x, v.source.y), new GridNode(v.destination.x, v.destination.y));
				Set<GridNode> temp = aStar.findPath();
				if(temp != null){
					path.addAll(aStar.findPath());
				}else {
					System.out.println("No path found");
				}
			});
			Map<GridNode, String> finalResult = new HashMap<>();

//			for (int i = -QUERY_RANGE; i < QUERY_RANGE; i++) {
//				for (int j = -QUERY_RANGE; j < QUERY_RANGE; j++) {
//					boolean occupied = false;
//					for (Room room : generator.rooms) {
//						if (j >= room.x && j < room.x + room.width && i >= room.y && i < room.y + room.height) {
//							occupied = true;
//							break;
//						}
//					}
//					finalResult.put(new GridNode(j, i), occupied ? "███" : "   ");
//				}
//			}
			path.forEach(v -> {
//				finalResult.put(v, "━╋━");
				finalResult.put(v, getPathSingle(v));
			});
			generator.roomInstances.forEach(room -> {
				for(int y = room.y; y < room.y + room.height; y++) {
					for (int x = room.x; x < room.x + room.width; x++) {
						finalResult.put(new GridNode(x, y), "███");
						for(GridNode door : room.getDoorsPosition()){
							finalResult.put(new GridNode(door.x, door.y), getDoorSingle(door, finalResult));
						}
					}
				}
			});
//			for(int y = -QUERY_RANGE; y < QUERY_RANGE; y++){
//				for(int x = -QUERY_RANGE; x < QUERY_RANGE; x++){
//					if(path.contains(new GridNode(x, y))){
//						finalResult.put(new GridNode(x, y), "━╋━");
//					}
//				}
//			}
			for(int y = -QUERY_RANGE; y < QUERY_RANGE; y++){
				for(int x = -QUERY_RANGE; x < QUERY_RANGE; x++){
					if(finalResult.get(new GridNode(x, y)) == null){
						System.out.print("   ");
					}else {
						System.out.print(finalResult.get(new GridNode(x, y)));
					}
				}
				System.out.println();
			}
		}
	}
	public static String getPathSingle(GridNode now){
		StringBuilder sb = new StringBuilder();
		if(path.contains(new GridNode(now.x - 1, now.y))){
			sb.append("━");
		}else{
			sb.append(" ");
		}
		if(path.contains(new GridNode(now.x, now.y + 1)) && path.contains(new GridNode(now.x, now.y - 1))){
			sb.append("┃");
		}else if(path.contains(new GridNode(now.x, now.y + 1))){
			sb.append("╻");
		}else if(path.contains(new GridNode(now.x, now.y - 1))){
			sb.append("╹");
		}else{
			sb.append(" ");
		}
		if(path.contains(new GridNode(now.x + 1, now.y))){
			sb.append("━");
		}else{
			sb.append(" ");
		}
		return sb.toString();
	}
	public static String getDoorSingle(GridNode now, Map<GridNode, String> finalResult){
		StringBuilder sb = new StringBuilder();
		if(!Objects.equals(finalResult.get(new GridNode(now.x - 1, now.y)), null)){
			sb.append("━");
		}else{
			sb.append(" ");
		}
		if(!Objects.equals(finalResult.get(new GridNode(now.x, now.y + 1)), null) && !Objects.equals(finalResult.get(new GridNode(now.x, now.y - 1)), null)){
			sb.append("┃");
		}else if(!Objects.equals(finalResult.get(new GridNode(now.x, now.y + 1)), null)){
			sb.append("╻");
		}else if(!Objects.equals(finalResult.get(new GridNode(now.x, now.y - 1)), null)){
			sb.append("╹");
		}else{
			sb.append(" ");
		}
		if(!Objects.equals(finalResult.get(new GridNode(now.x + 1, now.y)), null)){
			sb.append("━");
		}else{
			sb.append(" ");
		}
		return sb.toString();
	}
}
