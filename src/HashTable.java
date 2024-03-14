
public class HashTable<T> { // open addressing hashtable
	HashNode<T>[] table; // flag -> e: empty, f: full, d or null: deleted
	int m;
	int items = 0;
	public HashTable(int m) {
		table = (HashNode<T>[]) new HashNode[m];
		this.m = m;
		initialize();
	}

	public void initialize() {
		for (int i = 0; i < m; i++) {
			table[i] = new HashNode<T>(null, 'e');
		}
	}

	public void add(T key) {
		if(key == null) return;
		if(items == m/2) { // resize if the table is half full
			m = 2*m;
			HashTable<T> newTable = new HashTable<T>(m);
			for(int i = 0 ; i < m/2 ; i++) {
				newTable.add(table[i].getData());
			}
			table = newTable.getTable();
		}
		int index = key.hashCode() % m;
		if(index < 0) index = -index;
		while (table[index].getFlag() == 'f') {
			index = (index + 1) % m;			
		}
		table[index].setData(key);
		table[index].setFlag('f');
		items++;
	}

	public void remove(T key) {
		int index = key.hashCode() % m;
		if(index < 0) index = -index;
		table[index].setData(null);
	}

	public T getKey(T key) {
		int index = key.hashCode() % m;
		if(index < 0) index = -index;
		if (table[index].getFlag() == 'd') {
			System.out.println("Item was deleted");
			return null;
		} else if (table[index].getFlag() == 'e') {
			System.out.println("Item not found");
		} else {
			while (table[index].getFlag() == 'f' && !table[index].getData().equals(key))
				index = (index + 1) % m;
			return table[index].getData(); // key found since flag == f
		}
		return null;
	}
	
	public void print() {
		System.out.println();

		for(int i = 0 ; i < m ; i++) {
			System.out.print(i + "\t");
		}
		System.out.println();
		for(int i = 0 ; i < m ; i++) {
			if(table[i].getFlag() == 'f' || table[i].getFlag() == 'e')
			System.out.print(table[i] + "\t");
		}
		System.out.println();
	}
	
	public HashNode<T>[] getTable(){
		return this.table;
	}
	
	
}
