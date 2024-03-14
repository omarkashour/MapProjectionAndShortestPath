
public class HashNode<T> {
	private T data;
	private char flag;
	
	public HashNode(T data, char flag) {
		this.data = data;
		this.flag = flag;
	}

	public T getData() {
		return data;
	}

	public char getFlag() {
		return flag;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setFlag(char flag) {
		this.flag = flag;
	}
	
	public String toString() {
		return this.data +"";
	}
	
}
