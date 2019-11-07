public class Preflow <E extends Number> {
    private final int V;
    private int E;
    private final PushRelabelVertex<E>[] vertices;
    private final FlowNetworkEdge<PushRelabelVertex<E>, E>[][] edges;
    private final LinkedList<PushRelabelVertex<E>>[] inDegree;
    private final LinkedList<PushRelabelVertex<E>>[] outDegree;

    @SuppressWarnings("unchecked")
    public <T extends VertexInterface> Preflow(FlowNetwork<T, E> fnet) {
        int n = fnet.V();
        this.inDegree = (LinkedList<PushRelabelVertex<E>>[]) new LinkedList[n];
        this.outDegree = (LinkedList<PushRelabelVertex<E>>[]) new LinkedList[n];
        this.vertices = (PushRelabelVertex<E>[]) new PushRelabelVertex[n];
        this.edges = (FlowNetworkEdge<PushRelabelVertex<E>, E>[][]) 
                new FlowNetworkEdge[n][n];
        for (int i = 0; i < n; i++) {
            this.inDegree[i] = new LinkedList<PushRelabelVertex<E>>();
            this.outDegree[i] = new LinkedList<PushRelabelVertex<E>>();
            this.vertices[i] = new PushRelabelVertex<E>(i);
            this.edges[i] = (FlowNetworkEdge<PushRelabelVertex<E>, E>[]) 
                    new FlowNetworkEdge[n];
        }
        for (FlowNetworkEdge<T, E> e : fnet.getEdges()) {
            addEdge(e.incidentFrom(), e.incidentTo(), e.getCapacity());
        }
        this.V = n;
    }

    public void addEdge(int u, int v, NumericKey<E> capacity) {
        edges[u][v] = new FlowNetworkEdge<>(vertices[u], vertices[v], capacity);
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

    public PushRelabelVertex<E> getVertex(int v) {
        return vertices[v];
    }

    public boolean hasEdge(int u, int v) {
        return edges[u][v] != null;
    }

    public FlowNetworkEdge<PushRelabelVertex<E>, E> findEdge(int u, int v) {
        return edges[u][v];
    }

    public Iterable<FlowNetworkEdge<PushRelabelVertex<E>, E>> getEdges() {
        LinkedList<FlowNetworkEdge<PushRelabelVertex<E>, E>> output = 
                new LinkedList<>();
        for (int u = 0; u < V; u++) {
            for (PushRelabelVertex<E> v : outDegree[u]) {
                output.add(edges[u][v.getVertex()]);
            }
        }
        return output;
    }

    public Iterable<FlowNetworkEdge<PushRelabelVertex<E>, E>> 
            neighbourEdges(PushRelabelVertex<E> u) {
        return neighbourEdges(u.getVertex());
    }

    public Iterable<FlowNetworkEdge<PushRelabelVertex<E>, E>> 
            neighbourEdges(int u) {
        LinkedList<FlowNetworkEdge<PushRelabelVertex<E>, E>> a = 
                new LinkedList<>();
        NumericKey<E> zero = new NumericKey<>(NumericKey.ZERO);
        for (PushRelabelVertex<E> v : outDegree[u]) {
            FlowNetworkEdge<PushRelabelVertex<E>, E> e = 
                    edges[u][v.getVertex()];
            if (e.getCapacity().compareTo(zero) == 1) {
                a.add(e);
            }
        }
        for (PushRelabelVertex<E> w : inDegree[u]) {
            FlowNetworkEdge<PushRelabelVertex<E>, E> e = 
                    edges[u][w.getVertex()];
            if (e.getCapacity().compareTo(zero) == 1) {
                a.add(e);
            }
        }
        return a;
    }

    public Iterable<Integer> neighbourVertices(PushRelabelVertex<E> u) {
        return neighbourVertices(u.getVertex());
    }

    public Iterable<Integer> neighbourVertices(int u) {
        LinkedList<Integer> a = new LinkedList<>();
        for (PushRelabelVertex<E> v : outDegree[u]) {
            a.add(v.getVertex());
        }
        for (PushRelabelVertex<E> w : inDegree[u]) {
            a.add(w.getVertex());
        }
        return a;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < V; i++) {
            builder.append(vertices[i] + ": ");
            for (PushRelabelVertex<E> v : outDegree[i]) {
                NumericKey<E> c = edges[i][v.getVertex()].getCapacity();
                NumericKey<E> f = edges[i][v.getVertex()].getFlow();
                builder.append(v + "[" + f + "/" + c + "]");
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
