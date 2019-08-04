/**
 * Disjoint Set data structure using forests implementation.
 * A disjoint set data structure maintains a collection of dynamic
 * disjoint sets. Where each set has represensitive which is a member
 * of the set. Each tree in the forest represet a set.
 *
 * Forests implementation of Disjoint-Sets uses a tree where each node
 * point to its parent. The root, which is the set representative, is the
 * only nodes that its parent points to itself.
 * Forests implementation by itself is not faster than linked-list
 * implementation, however, with "weight by rank" and "path compression"
 * heuristics it can acheive an optimal running time.
 *
 * Weight-by-rank approach makes the root of the tree with the lower
 * rank points to the tree with the higher rank.
 *
 * Path-compression is also a highly affective approach, it is used during
 * {@code findSet} operations to make each node on the find path points
 * directly to the root.
 */
public class DisjointSetForests {
    private final int n;
    private final Node[] nodes;

    /**
     * Constructor. Initializes the sets by calling {@code makeSet}
     * on each node x in n.
     * @param n the number of nodes.
     * @throws IllegalArgumentException if n is less or equal to 0.
     */
    public DisjointSetForests(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid n: " + n);
        }

        this.n = n;
        this.nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            makeSet(i);
        }
    }

    public void makeSet(int x) {
        if (!isValidNode(x)) {
            throw new IllegalArgumentException(x + " is out of bounds " + n);
        }

        Node v = new Node();
        v.p = x;
        v.rank = 0;
        nodes[x] = v;
    }

    private void link(int rootX, int rootY) {
        Node x = nodes[rootX];
        Node y = nodes[rootY];
        if (x.rank > y.rank) {
            y.p = rootX;
        } else {
            x.p = rootY;
            if (x.rank == y.rank) {
                y.rank++;
            }
        }
    }

    public void union(int x, int y) {
        if (!isValidNode(x)) {
            throw new IllegalArgumentException(x + " is out of bound " + n);
        } else if (!isValidNode(y)) {
            throw new IllegalArgumentException(y + " is out of bound " + n);
        }

        int setX = findSet(x);
        int setY = findSet(y);
        if (setX != setY) {
            link(setX, setY);
        }
    }

    public int findSet(int ind) {
        if (!isValidNode(ind)) {
            throw new IllegalArgumentException(ind + " is out of bounds " + n);
        }

        Node x = nodes[ind];
        if (x.p != ind) {
            x.p = findSet(x.p);
        }
        return x.p;
    }

    public boolean isConnected(int x, int y) {
        if (!isValidNode(x)) {
            throw new IllegalArgumentException(x + " is out of bound " + n);
        } else if (!isValidNode(y)) {
            throw new IllegalArgumentException(y + " is out of bound " + n);
        }

        return findSet(x) == findSet(y);
    }

    private boolean isValidNode(int x) {
        return x >= 0 && x < n;
    }

    public static void main(String[] args) {
        DisjointSetForests dsf = new DisjointSetForests(10);
        dsf.union(0, 1);
        dsf.union(0, 2);
        dsf.union(2, 1);
        dsf.union(1, 3);
        dsf.union(4, 5);
        dsf.union(4, 6);
        dsf.union(7, 8);
        for (int i = 0; i < 10; i++) {
            System.out.println("findSet[" + i + "]: " + dsf.findSet(i));
        }
        System.out.println("connected 0, 3? " + dsf.isConnected(0, 3));
        System.out.println("connected 1, 4? " + dsf.isConnected(1, 4));
        System.out.println("connected 7, 8? " + dsf.isConnected(7, 8));
        System.out.println("connected 2, 9? " + dsf.isConnected(2, 9));
    }
}
