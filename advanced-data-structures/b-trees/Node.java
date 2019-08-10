public class Node<T extends Comparable<T>, V> {
    public int n;
    public boolean isLeaf = true;
    private final Key<T, V>[] keys;
    private final Node<T, V>[] children;

    public Node(int minimumDegree) {
        this.keys = (Key<T, V>[]) new Key[2 * minimumDegree];
        this.children = (Node<T, V>[]) new Node[2 * minimumDegree];
    }

    public Key<T, V> key(int ind) {
        return keys[ind];
    }

    public void setKey(int i, Key<T, V> k) {
        keys[i] = k;
    }

    public Node<T, V> child(int ind) {
        return children[ind];
    }

    public void setChild(int i, Node<T, V> node) {
        children[i] = node;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        System.out.println("Node[" + n + "]: isleaf = " + isLeaf);
        for (int i = 0; i < n; i++) {
            s.append("key => ");
            String txt = "null";
            if (keys[i] != null) {
                txt = keys[i].toString();
            }
            s.append(txt + "\n");
        }
        if (isLeaf) return s.toString();
        for (int i = 0; i <= n; i++) {
            s.append("child => ");
            String txt = "null";
            Node<T, V> child = children[i];
            if (children[i] != null) {
                txt = children[i].toString();
            }
            s.append(txt);
        }
        return s.toString();
    }
}
