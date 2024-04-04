import java.util.ArrayList;
import java.util.List;

public class Graph {
	List<RoomInstance> roomInstances;
	List<Edge> edges;
	List<GridNode> nodes;

	public Graph(List<RoomInstance> roomInstances) {
		this.roomInstances = roomInstances;
		this.edges = new ArrayList<>();
		nodes = new ArrayList<>();
		for (RoomInstance node : roomInstances) {
			nodes.addAll(node.getDoorsPosition());
		}
		for (GridNode door : nodes) {
			for(GridNode door2 : nodes){
				if(!door.equals(door2)){
					edges.add(new Edge(door, door2));
				}
			}
		}
//		for (int i = 0; i < nodes.size(); i++) {
//			for (int j = i + 1; j < nodes.size(); j++) {
////				edges.add(new Edge(nodes.get(i), nodes.get(j)));
//			}
//		}
	}

	public List<Edge> primsMST() {
		if (nodes.isEmpty()) return null;

		ArrayList<Edge> result = new ArrayList<>();
		ArrayList<GridNode> visitedNodes = new ArrayList<>();

		visitedNodes.add(nodes.get(0));  // start from the first room

		while (visitedNodes.size() < nodes.size()) {
			Edge minEdge = null;

			for (Edge edge : edges) {
				if (visitedNodes.contains(edge.source) && !visitedNodes.contains(edge.destination) ||
						visitedNodes.contains(edge.destination) && !visitedNodes.contains(edge.source)) {
					if (minEdge == null || edge.weight < minEdge.weight) {
						minEdge = edge;
					}
				}
			}

			if (minEdge == null) break;
			result.add(minEdge);
			visitedNodes.add(visitedNodes.contains(minEdge.source) ? minEdge.destination : minEdge.source);
		}
		return result;
	}
}

