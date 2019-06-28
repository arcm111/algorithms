public class Node<T extends Comparable<T>, V> {
	public int n;
	public boolean isLeaf = true;
	private final Array<Key<T, V>> keys;
	private final Array<Node<T, V>> children;

	public Node(int minimumDegree) {
		this.keys = new Array<>(2 * minimumDegree);
		this.children = new Array<>(2 * minimumDegree);
	}

	public Key<T, V> key(int ind) {
		return keys.get(ind);
	}

	public void setKey(int i, Key<T, V> k) {
		keys.set(i, k);
	}

	public Node<T, V> child(int ind) {
		return children.get(ind);
	}

	public void setChild(int i, Node<T, V> node) {
		children.set(i, node);
	}

	@Override
	public String toString() {
		if (n == 0) return "";
		StringBuilder s = new StringBuilder();
		System.out.println("Node[" + n + "]: isleaf = " + isLeaf);
		for (int i = 0; i < n; i++) {
			Key cur = keys.get(i);
			if (cur == null) {
				System.out.println("Error: key[" + i + "] is null. n = " + n);
			} else {
				s.append(cur.toString() + "\n");
			}
		}
		if (!isLeaf) {
			for (int i = 0; i <= n; i++) {
				Node<T, V> child = children.get(i);
				if (child == null) {
					System.out.println("Error: child[" + i + "] is null");
				} else {
					s.append(child.toString());
				}
			}
		}
		return s.toString();
	}
}
