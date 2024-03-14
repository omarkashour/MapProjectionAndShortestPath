import java.util.LinkedList;
import java.util.Objects;

public class Vertix implements Comparable<Vertix> {
	private String data;
	private double longitude;
	private double latitude;
	private double distance; // initialized to infinity
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	private double x;
	private double y;
	private Vertix previous;

	public Vertix(String data) {
		this.data = data;
		this.distance = Double.MAX_VALUE;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Vertix getPrevious() {
		return previous;
	}

	public void setPrevious(Vertix previous) {
		this.previous = previous;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Vertix(String data, double latitude, double longitude) {
		this.data = data;
		this.longitude = longitude;
		this.latitude = latitude;
		this.distance = Double.MAX_VALUE;
	}
	
	

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Vertix(String data, double longitude, double latitude, LinkedList<Edge> edges) {
		super();
		this.data = data;
		this.longitude = longitude;
		this.latitude = latitude;
		this.edges = edges;
	}



	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LinkedList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(LinkedList<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertix other = (Vertix) obj;
		return Objects.equals(data, other.data);
	}
	
	public String toString() {
		return this.data;
	}

	@Override
	public int compareTo(Vertix o) {
		if(this.distance > o.getDistance())
			return 1;
		else if(this.distance < o.getDistance())
			return -1;
		return 0;
	}
	
	
}
