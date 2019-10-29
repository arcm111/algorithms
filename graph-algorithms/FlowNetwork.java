public class FlowNetwork<T extends VertexInterface, E extends Number> {
    private int V;
    private int E;
    private T[] vertices;
    private FlowNetworkEdge<T, E>[][] edges;
    private LinkedList<T>[] inDegree;
    private LinkedList<T>[] outDegree;

    public FlowNetwork(T[] rawVertices) {
        init(rawVertices.length);
        this.vertices = rawVertices;
    }

    public <W extends WeightedDirectedGraph<T, E>> FlowNetwork(W G) {
        init(G.V());
        int i = 0;
        for (T w : G.getVertices()) {
            this.vertices[i++] = w;
        }
        for (WeightedEdge<T, E> e : G.getEdges()) {
            int u = e.incidentFrom();
            int v = e.incidentTo();
            FlowNetworkEdge<T, E> x = 
                    new FlowNetworkEdge<>(vertices[u], vertices[v]);
            x.setCapacity(e.getKey());
            x.setFlow(new NumericKey<E>(NumericKey.ZERO));
            edges[u][v] = x;
            outDegree[u].add(vertices[v]);
        }
    }

    @SuppressWarnings("unchecked")
    private void init(int n) {
        this.inDegree = (LinkedList<T>[]) new LinkedList[n];
        this.outDegree = (LinkedList<T>[]) new LinkedList[n];
        this.edges = (FlowNetworkEdge<T, E>[][]) new FlowNetworkEdge[n][n];
        for (int i = 0; i < n; i++) {
            this.inDegree[i] = new LinkedList<T>();
            this.outDegree[i] = new LinkedList<T>();
            this.edges[i] = (FlowNetworkEdge<T, E>[]) new FlowNetworkEdge[n];
        }
        this.vertices = (T[]) new VertexInterface[n];
        this.V = n;
    }

    public void addEdge(int u, int v, E capacity) {
        T src = vertices[u];
        T dst = vertices[v];
        FlowNetworkEdge<T, E> e = new FlowNetworkEdge<>(src, dst, capacity);
        addEdge(e);
    }

    public void addEdge(FlowNetworkEdge<T, E> e) {
        int u = e.incidentFrom();
        int v = e.incidentTo();
        edges[u][v] = e;
        inDegree[v].add(vertices[u]);
        outDegree[u].add(vertices[v]);
        this.E++;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public boolean hasEdge(int u, int v) {
        return edges[u][v] != null;
    }

    public boolean hasEdge(FlowNetworkEdge<T, E> e) {
        return edges[e.incidentFrom()][e.incidentTo()] != null;
    }

    public FlowNetworkEdge<T, E> findEdge(int u, int v) {
        return edges[u][v];
    }

    public Iterable<FlowNetworkEdge<T, E>> getEdges() {
        LinkedList<FlowNetworkEdge<T, E>> output = new LinkedList<>();
        for (int u = 0; u < V; u++) {
            for (T v : outDegree[u]) {
                output.add(edges[u][v.getVertex()]);
            }
        }
        return output;
    }

    public Iterable<FlowNetworkEdge<T, E>> neighbours(T u) {
        return neighbours(u.getVertex());
    }

    public Iterable<FlowNetworkEdge<T, E>> neighbours(int u) {
        LinkedList<FlowNetworkEdge<T, E>> a = new LinkedList<>();
        NumericKey<E> zero = new NumericKey<>(NumericKey.ZERO);
        for (T v : outDegree[u]) {
            FlowNetworkEdge<T, E> e = edges[u][v.getVertex()];
            if (e.getCapacity().compareTo(zero) == 1) {
                a.add(e);
            }
        }
        for (T w : inDegree[u]) {
            FlowNetworkEdge<T, E> e = edges[u][w.getVertex()];
            if (e.getCapacity().compareTo(zero) == 1) {
                a.add(e);
            }
        }
        return a;
    }

    public Iterable<Integer> neighbourVertices(T u) {
        return neighbourVertices(u.getVertex());
    }

    public Iterable<Integer> neighbourVertices(int u) {
        LinkedList<Integer> a = new LinkedList<>();
        for (T v : outDegree[u]) {
            a.add(v.getVertex());
        }
        for (T w : inDegree[u]) {
            a.add(w.getVertex());
        }
        return a;
    }

    public FlowNetwork<T, E> residualNetwork() {
        FlowNetwork<T, E> residual = new FlowNetwork<>(vertices);
        for (FlowNetworkEdge<T, E> e : getEdges()) {
            FlowNetworkEdge<T, E> forwardEdge = new FlowNetworkEdge<>(e);
            forwardEdge.setCapacity(e.getCapacity());
            FlowNetworkEdge<T, E> reverseEdge = forwardEdge.reverse();
            reverseEdge.setCapacity(new NumericKey<E>(NumericKey.ZERO));
            residual.addEdge(forwardEdge);
            residual.addEdge(reverseEdge);
        }
        return residual;
    }

    public void updateResidualCapacity(int u, int v, NumericKey<E> delta) {
        FlowNetworkEdge<T, E> e = edges[u][v];
        e.setCapacity(e.getCapacity().minus(delta));
        FlowNetworkEdge<T, E> r = edges[v][u];
        r.setCapacity(r.getCapacity().plus(delta));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < V; i++) {
            builder.append("[" + i + "]: ");
            for (T v : outDegree[i]) {
                NumericKey<E> c = edges[i][v.getVertex()].getCapacity();
                NumericKey<E> f = edges[i][v.getVertex()].getFlow();
                builder.append(v.getVertex() + "(" + f + "/" + c + ") ");
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

        // Testing with a weighted-directed-graph
        for (int i = 0; i < 6; i++) vertices[i] = new Vertex(i);
        WeightedDirectedGraph<Vertex, Integer> G = 
                new WeightedDirectedGraph<>(vertices);
        G.addEdge(0, 1, 16);
        G.addEdge(0, 2, 13);
        G.addEdge(1, 3, 12);
        G.addEdge(2, 1, 4);
        G.addEdge(2, 4, 14);
        G.addEdge(3, 2, 9);
        G.addEdge(3, 5, 20);
        G.addEdge(4, 3, 7);
        G.addEdge(4, 5, 4);
        fn = new FlowNetwork<>(G);
        System.out.println(fn);
    }
}
