/**
 * Residual network.
 * Maintains values of how much more flow can we push through each edge in
 * the relative flow network either in the forward direction (increasing flow) 
 * or in the reverse direction (decreasing or cancelling flow on the forward 
 * edge).
 * Anti-parallel edges are allowed and used to store residual-capacity of 
 * reverse-edges (not existed in original flow-network) of all flow-network 
 * edges. These residual-capacities are used to cancel(decrease) flow.
 * The only edges of original flow-network that exists in the residual-network
 * are those who can admit more flow (residual-capacity greater than 0).
 * <em>Residual capacity cf(u,v) of an edge (u,v) is equal to:</em>
 *     c(u,f) - f(u,v)  {if (u,v) exists in the flow-network}
 *     f(u,v)           {if (u,v) does not exist in the flow-network}
 */
public class ResidualNetwork<E extends Number> {
    private int V; // number of vertices in flow-network
    private NumericKey<E>[][] capacity; // residual capacity matrix
    private LinkedList<Integer>[] inDegree; // in-degree vertices
    private LinkedList<Integer>[] outDegree; // out-degree vertices

    /** 
     * Constructor.
     * @param fnet the flow-network for which to create the residual-network.
     */
    public ResidualNetwork(FlowNetwork<?, E> fnet) {
        init(fnet.V());
        for (FlowNetworkEdge<?, E> e : fnet.getEdges()) {
            int u = e.incidentFrom();
            int v = e.incidentTo();
            capacity[u][v] = e.getCapacity();
            capacity[v][u] = new NumericKey<E>(NumericKey.ZERO);
            outDegree[u].add(v);
            inDegree[v].add(u);
        }
    }

    /**
     * Initializes the residual-network.
     * @param n the number of vertices in the network
     */
    @SuppressWarnings("unchecked")
    private void init(int n) {
        this.inDegree = (LinkedList<Integer>[]) new LinkedList[n];
        this.outDegree = (LinkedList<Integer>[]) new LinkedList[n];
        this.capacity = (NumericKey<E>[][]) new NumericKey[n][n];
        for (int i = 0; i < n; i++) {
            this.inDegree[i] = new LinkedList<Integer>();
            this.outDegree[i] = new LinkedList<Integer>();
            this.capacity[i] = (NumericKey<E>[]) new NumericKey[n];
        }
        this.V = n;
    }

    /**
     * Returns the number of vertices in the network.
     * @return the number of vertices
     */
    public int V() {
        return V;
    }

    /**
     * Returns the residual-capacity of an edge.
     * @param u the head vertex
     * @param v the tail vertex
     * @return residual capacity of (u,v)
     */
    public NumericKey<E> residualCapacity(int u, int v) {
        return capacity[u][v];
    }

    /**
     * Returns adjacent vertices on non-zero residual-capacities edges.
     * @param u the vertex
     * @return adjacent vertices
     */
    public Iterable<Integer> neighbours(int u) {
        LinkedList<Integer> a = new LinkedList<>();
        for (int v : outDegree[u]) {
            if (!capacity[u][v].isZero()) a.add(v);
        }
        for (int w : inDegree[u]) {
            if (!capacity[u][w].isZero()) a.add(w);
        }
        return a;
    }

    /**
     * Updates residual-capacities of forward and reverse edges according to
     * additional amount of flow added to the network edge.
     * Decreases residual capacity of forward edge and increases it for the
     * reverse edge by the same amount delta.
     * @param u the head vertex
     * @param v the tail vertex
     * @param delta the amount of extra flow pushed from u to v
     */
    public void updateResidualCapacity(int u, int v, NumericKey<E> delta) {
        capacity[u][v] = capacity[u][v].minus(delta);
        capacity[v][u] = capacity[v][u].plus(delta);
    }

    /**
     * Returns a string representation of the residual-netwrok.
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int u = 0; u < V; u++) {
            builder.append("[" + u + "]: ");
            // forward edges
            for (int v : outDegree[u]) {
                builder.append(v + "(" + capacity[u][v] + ") ");
            }
            if (!inDegree[u].isEmpty()) builder.append(" - ");
            // reverse edges
            for (int w : inDegree[u]) {
                builder.append(w + "(" + capacity[u][w] + ") ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        Vertex[] vertices = new Vertex[6];
        for (int i = 0; i < 6; i++) vertices[i] = new Vertex(i);
        // Testing with vertices array
        FlowNetwork<Vertex, Integer> fn = new FlowNetwork<>(vertices);
        fn.addEdge(0, 1, 16);
        fn.addEdge(0, 2, 13);
        fn.addEdge(1, 3, 12);
        fn.addEdge(2, 1, 4);
        fn.addEdge(2, 4, 14);
        fn.addEdge(3, 2, 9);
        fn.addEdge(3, 5, 20);
        fn.addEdge(4, 3, 7);
        fn.addEdge(4, 5, 4);
        System.out.println(fn);

        ResidualNetwork<Integer> rn = new ResidualNetwork<>(fn);
        System.out.println(rn);
    }
}
