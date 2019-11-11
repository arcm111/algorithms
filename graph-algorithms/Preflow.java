/**
 * Preflow used in push-relabel and relabel-to-front algorithms.
 * Very similar to a flow-network but uses a special type of vertices which
 * maintains special properties of height and excess flow which are important
 * for push-relabel and relabel-to-front algorithms.
 * Another difference to flow-networks is that preflow does not satisfy the
 * flow conservation property, instead is allows vertices to have more in-flow
 * than out-flow, therefore, allowing them to behave as reservoirs that can 
 * accumulate excess-flow.
 */
public class Preflow <E extends Number> {
    private final int V;
    private int E;
    private final PushRelabelVertex<E>[] vertices;
    private final FlowNetworkEdge<PushRelabelVertex<E>, E>[][] edges;
    private final LinkedList<PushRelabelVertex<E>>[] inDegree;
    private final LinkedList<PushRelabelVertex<E>>[] outDegree;

    /**
     * Constructor.
     * Initializes the preflow using edges of a given flow-network.
     * @param fnet the flow-network for which we need to construct a preflow
     */
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

    /**
     * Adds an edge to the preflow.
     * @param u head vertex
     * @param v tail vertex
     * @param capacity edge's capacity
     */
    public void addEdge(int u, int v, NumericKey<E> capacity) {
        edges[u][v] = new FlowNetworkEdge<>(vertices[u], vertices[v], capacity);
        inDegree[v].add(vertices[u]);
        outDegree[u].add(vertices[v]);
        this.E++;
    }

    /**
     * Returns number of vertices in the flow-network/preflow.
     * @return number of vertices
     */
    public int V() {
        return V;
    }

    /**
     * Returns number of edges in the flow-network/preflow.
     * @return number of edges
     */
    public int E() {
        return E;
    }

    /**
     * Returns a preflow vertex at a given index.
     * @param v the index of the vertex
     * @return the preflow vertex
     */
    public PushRelabelVertex<E> getVertex(int v) {
        return vertices[v];
    }

    /**
     * Checks if an edge exists in the preflow.
     * @param u the head vertex
     * @param v the tail vertex
     * @return true if the edge exists, otherwise, false
     */
    public boolean hasEdge(int u, int v) {
        return edges[u][v] != null;
    }

    /**
     * Finds a returns an edge in the preflow.
     * @param u the head vertex
     * @param v the tail veretx
     * @return the edge if found or null if does not exist
     */
    public FlowNetworkEdge<PushRelabelVertex<E>, E> findEdge(int u, int v) {
        return edges[u][v];
    }

    /**
     * Returns a list of all edges in the preflow as an iterable.
     * @return preflow edges
     */
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

    /**
     * Returns edges incident of a given vertex.
     * @param u the vertex to find in-degree and out-degree edges for
     * @return list of edges as an iterable
     */
    public Iterable<FlowNetworkEdge<PushRelabelVertex<E>, E>> 
            neighbourEdges(PushRelabelVertex<E> u) {
        return neighbourEdges(u.getVertex());
    }

    /**
     * Returns edges incident of a given vertex.
     * @param u vertex index for which to find in-degree and out-degree edges 
     * @return list of edges as an iterable
     */
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

    /**
     * Return adjacent vertices for a given vertex.
     * @param u the vertex for which to find adjacent vertices
     * @return adjacent vertices as an iterable
     */
    public Iterable<Integer> neighbourVertices(PushRelabelVertex<E> u) {
        return neighbourVertices(u.getVertex());
    }

    /**
     * Return adjacent vertices for a given vertex.
     * @param u index of the vertex for which to find adjacent vertices
     * @return adjacent vertices as an iterable
     */
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

    /**
     * Returns a string representation of the preflow.
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < V; i++) {
            builder.append(vertices[i] + ": ");
            for (PushRelabelVertex<E> v : outDegree[i]) {
                NumericKey<E> c = edges[i][v.getVertex()].getCapacity();
                NumericKey<E> f = edges[i][v.getVertex()].getFlow();
                builder.append(v + "[" + f + "/" + c + "] ");
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
