public class Edge {
	roomInstance source;
	roomInstance destination;
	double weight;

	public Edge(roomInstance source, roomInstance destination) {
		this.source = source;
		this.destination = destination;
		this.weight = Math.sqrt(Math.pow(source.getCenter().x - destination.getCenter().x, 2) + Math.pow(source.getCenter().y - destination.getCenter().y, 2));
	}
}
