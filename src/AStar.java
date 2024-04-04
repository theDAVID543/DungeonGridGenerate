import java.util.*;

public class AStar {
	private final Map<GridNode, Integer> grid = new HashMap<>();
	private static GridNode start;
	private static GridNode end;

	public AStar(List<RoomInstance> roomInstances, GridNode start, GridNode end){
		AStar.start = start;
		AStar.end = end;
		roomInstances.forEach(room -> {
			for(int x = room.x; x < room.x + room.width; x++) {
				for (int y = room.y; y < room.y + room.height; y++) {
					grid.put(new GridNode(x,y), 1);
				}
			}
		});
//		for (int i = -GrowingRoomGenerator.QUERY_RANGE; i < GrowingRoomGenerator.QUERY_RANGE; i++) {
//			for (int j = -GrowingRoomGenerator.QUERY_RANGE; j < GrowingRoomGenerator.QUERY_RANGE; j++) {
//				boolean occupied = false;
//				for (Room room : rooms) {
//					if (j >= room.x && j < room.x + room.width && i >= room.y && i < room.y + room.height) {
//						occupied = true;
//						break;
//					}
//				}
//				grid.put(new GridNode(j, i), occupied ? 1 : null);
//			}
//		}
	}

	public Set<GridNode> findPath() {
		HashMap<GridNode, GridNode> cameFrom = new HashMap<>();
		HashMap<GridNode, Double> gScore = new HashMap<>();
		HashMap<GridNode, Double> fScore = new HashMap<>();
		PriorityQueue<GridNode> openSet = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));

		gScore.put(start, 0.0);
		fScore.put(start, heuristic(start));
		openSet.add(start);

		while (!openSet.isEmpty()) {
			GridNode current = openSet.poll();

			if (current.equals(end)) {
				return reconstructPath(cameFrom, current);
			}

			for (GridNode neighbor : getNeighbors(current)){
				double tentativeGScore = gScore.get(current) + 1;
				if(GrowingRoomGenerator.path.contains(neighbor)){
					tentativeGScore --;
				}
				if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tentativeGScore);
					fScore.put(neighbor, gScore.get(neighbor) + heuristic(neighbor));
					if (!openSet.contains(neighbor)) {
						openSet.add(neighbor);
					}
				}
			}
		}
		return null;
	}

	private static double heuristic(GridNode node) {
		return Math.abs(node.x - end.x) + Math.abs(node.y - end.y);
	}

	private List<GridNode> getNeighbors(GridNode node) {
		List<GridNode> neighbors = new ArrayList<>();
		GridNode temp;
		Integer tempNeighbor;
		temp = new GridNode(node.x - 1, node.y);
		tempNeighbor = grid.get(temp);
		if(node.x - 1 >= -GrowingRoomGenerator.QUERY_RANGE && (tempNeighbor == null || tempNeighbor != 1)){
			neighbors.add(new GridNode(node.x - 1, node.y));
		}
		temp = new GridNode(node.x + 1, node.y);
		tempNeighbor = grid.get(temp);
		if(node.x + 1 <= GrowingRoomGenerator.QUERY_RANGE && (tempNeighbor == null || tempNeighbor != 1)){
			neighbors.add(new GridNode(node.x + 1, node.y));
		}
		temp = new GridNode(node.x, node.y - 1);
		tempNeighbor = grid.get(temp);
		if(node.y - 1 >= -GrowingRoomGenerator.QUERY_RANGE && (tempNeighbor == null || tempNeighbor != 1)){
			neighbors.add(new GridNode(node.x, node.y - 1));
		}
		temp = new GridNode(node.x, node.y + 1);
		tempNeighbor = grid.get(temp);
		if(node.y + 1 <= GrowingRoomGenerator.QUERY_RANGE && (tempNeighbor == null || tempNeighbor != 1)){
			neighbors.add(new GridNode(node.x, node.y + 1));
		}
		return neighbors;
	}

	private static Set<GridNode> reconstructPath(HashMap<GridNode, GridNode> cameFrom, GridNode current) {
		Set<GridNode> totalPath = new HashSet<>();
		totalPath.add(current);
		while(cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			totalPath.add(current);
		}
		return totalPath;
	}
}

