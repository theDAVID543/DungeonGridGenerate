public class Edge {
	GridNode source;
	GridNode destination;
	double weight;

	public Edge(GridNode source, GridNode destination) {
		this.source = source;
		this.destination = destination;
		this.weight = Math.sqrt(Math.pow(source.x - destination.x, 2) + Math.pow(source.y - destination.y, 2));
	}
}
