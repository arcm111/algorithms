public class ResidualNetwork<E extends Number> {
    private int V;
    private NumericKey<E>[][] capacity;
    private LinkedList<Integer>[] inDegree;
    private LinkedList<Integer>[] outDegree;

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

    public int V() {
        return V;
    }

    public NumericKey<E> residualCapacity(int u, int v) {
        return capacity[u][v];
    }

    public Iterable<Integer> neighbours(int u) {
        LinkedList<Integer> a = new LinkedList<>();
        for (int v : outDegree[u]) {
            if (!capacity[u][v].isZero()) a.add(v);
        }
        for (int w : inDegree[u]) {
            if (!capacity[w][u].isZero()) a.add(w);
        }
        return a;
    }

    public void updateResidualCapacity(int u, int v, NumericKey<E> delta) {
        capacity[u][v] = capacity[u][v].minus(delta);
        capacity[v][u] = capacity[v][u].plus(delta);
    }

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
