import java.util.*;
import java.util.List;

public class GrowingRoomGenerator {
	private static final int MAX_ROOM_SIZE = 5;
	private static final int MAX_ATTEMPTS = 10;
	public static int QUERY_RANGE = 50;
	private static final int MAX_ROOMS = 50;

	private final List<roomInstance> roomInstances = new ArrayList<>();

	private boolean roomCollides(roomInstance roomInstance) {
		for (roomInstance existingRoomInstance : roomInstances) {
			if (roomInstance.collidesWith(existingRoomInstance)) {
				return true;
			}
		}
		return false;
	}

	public void generateRooms() {
		Random rand = new Random();

		// Start with a smaller initial room centered.
		roomInstance initialRoomInstance = new roomInstance(-2, -2, 4, 4);
		roomInstances.add(initialRoomInstance);

		List<roomInstance> activeRoomInstances = new ArrayList<>();
		activeRoomInstances.add(initialRoomInstance);

		while (!activeRoomInstances.isEmpty() && roomInstances.size() < MAX_ROOMS) {
			roomInstance current = activeRoomInstances.get(rand.nextInt(activeRoomInstances.size()));

			boolean roomAdded = false;
			for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
				int newWidth = rand.nextInt(MAX_ROOM_SIZE) + 1;
				int newHeight = rand.nextInt(MAX_ROOM_SIZE) + 1;

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

				roomInstance newRoomInstance = new roomInstance(newX, newY, newWidth, newHeight);
				if (!roomCollides(newRoomInstance)) {
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
		for(roomInstance roomInstance : roomInstances){
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
				for (roomInstance roomInstance : roomInstances) {
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
		for(int e = 0; e < 100; e++){
			GrowingRoomGenerator generator = new GrowingRoomGenerator();
			generator.generateRooms();
			generator.displayGrid();
			path = new HashSet<>();
			Graph graph = new Graph(generator.roomInstances);
			List<Edge> mstEdges = graph.primsMST();
			mstEdges.forEach(v -> {
				System.out.println("Edge from: (" + v.source.x + ", " + v.source.y + ") to (" + v.destination.x + ", " + v.destination.y + ")");
				AStar aStar = new AStar(generator.roomInstances, new GridNode(v.source.x-1, v.source.y), new GridNode(v.destination.x-1, v.destination.y));
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
			generator.roomInstances.forEach(room -> {
				for(int y = room.y; y < room.y + room.height; y++) {
					for (int x = room.x; x < room.x + room.width; x++) {
						finalResult.put(new GridNode(x,y), "███");
					}
				}
			});
			path.forEach(v -> {
				finalResult.put(v, "━╋━");
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
}
