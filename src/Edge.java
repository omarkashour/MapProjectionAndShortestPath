
public class Edge implements Comparable<Edge> {
	private Vertix startVertix;
	private Vertix endVertix;
	private double weight;
	
	
	public Edge(Vertix startVertix, Vertix endVertix, double weight) {
		this.startVertix = startVertix;
		this.endVertix = endVertix;
		this.weight = weight;
	}


	public Edge(Vertix endVertix , double weight) {
		this.endVertix = endVertix;
		this.weight = weight;
	}


	public Vertix getEndVertix() {
		return endVertix;
	}


	public void setEndVertix(Vertix endVertix) {
		this.endVertix = endVertix;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	@Override
	public int compareTo(Edge o) {
		if(this.weight > o.weight) {
			return 1;
		}
		else if( this.weight < o.weight) {
			return -1;
		}
		return 0;
	}
	
	public String toString() {
		return this.endVertix.toString()+":"+this.weight ;
	}


	public Vertix getStartVertix() {
		return startVertix;
	}


	public void setStartVertix(Vertix startVertix) {
		this.startVertix = startVertix;
	}
	
	
}
