import java.util.ArrayList;
import java.util.List;

public class Graph {
	List<RoomInstance> nodes;
	List<Edge> edges;

	public Graph(List<RoomInstance> nodes) {
		this.nodes = nodes;
		this.edges = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				edges.add(new Edge(nodes.get(i), nodes.get(j)));
			}
		}
	}

	public List<Edge> primsMST() {
		if (nodes.isEmpty()) return null;

		ArrayList<Edge> result = new ArrayList<>();
		ArrayList<RoomInstance> visitedNodes = new ArrayList<>();

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

