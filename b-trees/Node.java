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
        StringBuilder s = new StringBuilder();
        System.out.println("Node[" + n + "]: isleaf = " + isLeaf);
        for (int i = 0; i < n; i++) {
            s.append("key => ");
            String txt = "null";
            if (keys.get(i) != null) {
                txt = keys.get(i).toString();
            }
            s.append(txt + "\n");
        }
        if (isLeaf) return s.toString();
        for (int i = 0; i <= n; i++) {
            s.append("child => ");
            String txt = "null";
            Node<T, V> child = children.get(i);
            if (children.get(i) != null) {
                txt = children.get(i).toString();
            }
            s.append(txt);
        }
        return s.toString();
    }
}
