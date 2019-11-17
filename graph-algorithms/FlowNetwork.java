/**
 * Flow Network.
 * A flow network is basically a directed graph whose edges have non-negative
 * weights called capacity in addition to another non-negative weights called
 * flow that must not exceed capacity.
 * Anti-parallel edges (such as (u, v) and (v, u)) are not allowed.
 * Self-loops are not allowed.
 * For every vertex in the network there is a path fron source to sink passing
 * through it. Therfore, the network is a connected graph.
 * <em>The flow in the network must satisfies two properties</em>:
 *   1- Capacity constraint: {@code 0 <= flow <= capacity}
 *   2- Flow conservation: for each vertex, flow in = flow out
 * A cut in a netwrok is partitioning of vertices into two disjoint subsets, S
 * containing the source s and the T contains the sink t.
 * Net-flow f(S, T) across a cut (S, T) is the flow from S to T minus the flow
 * in the reverse direction from T to S.
 * The capacity of a cut (S, T) is the sum of the capacities of all edges
 * going from S to T.
 * The value of any flow in the network is less than the capacity of any cut.
 * A mimimum cut in a flow network is a cut whose capacity is mimimum over
 * all cuts of the network.
 * The net-flow of a minimum-cut is the maximum flow of a network.
 * <em>Max-flow min-cut theorem</em>:
 * For a network with flow f, source s and sink t, the following are equivalent:
 *   1- f is maximum flow.
 *   2- The residual network has no augmenting paths.
 *   3- For some cut (S, T) the capacity of the cut equals the flow value.
 */
public class FlowNetwork<T extends VertexInterface, E extends Number> {
    private int V;
    private int E;
    private T[] vertices;
    private FlowNetworkEdge<T, E>[][] edges;
    private LinkedList<T>[] incomingEdges;
    private LinkedList<T>[] outgoingEdges;

    /**
     * Constructor.
     * @param rawVertices the network vertices
     */
    public FlowNetwork(T[] rawVertices) {
        init(rawVertices.length);
        this.vertices = rawVertices;
    }

    /**
     * Constructor.
     * Builds a flow-network from a weighted-directed-graph.
     * @param G the weighted-directed-graph
     */
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
            outgoingEdges[u].add(vertices[v]);
        }
    }

    /**
     * Initializes the flow-network.
     * @param n number of vertices
     */
    @SuppressWarnings("unchecked")
    private void init(int n) {
        this.incomingEdges = (LinkedList<T>[]) new LinkedList[n];
        this.outgoingEdges = (LinkedList<T>[]) new LinkedList[n];
        this.edges = (FlowNetworkEdge<T, E>[][]) new FlowNetworkEdge[n][n];
        for (int i = 0; i < n; i++) {
            this.incomingEdges[i] = new LinkedList<T>();
            this.outgoingEdges[i] = new LinkedList<T>();
            this.edges[i] = (FlowNetworkEdge<T, E>[]) new FlowNetworkEdge[n];
        }
        this.vertices = (T[]) new VertexInterface[n];
        this.V = n;
    }

    /**
     * adds a flow-edge to the network.
     * @param u vertex which the edge is incident from
     * @param v vertex which the edge is incident to
     * @param capacity the edge capacity
     */
    public void addEdge(int u, int v, E capacity) {
        addEdge(new FlowNetworkEdge<T, E>(vertices[u], vertices[v], capacity));
    }

    /**
     * adds a flow-edge to the network.
     * @param e flow-edge
     */
    public void addEdge(FlowNetworkEdge<T, E> e) {
        int u = e.incidentFrom();
        int v = e.incidentTo();
        edges[u][v] = e;
        incomingEdges[v].add(vertices[u]);
        outgoingEdges[u].add(vertices[v]);
        this.E++;
    }

    /**
     * Sets flow value for a certain edge.
     * @param u vertex which the edge is incident from
     * @param v vertex which the edge is incident to
     * @param flow the edge's flow vlue
     */
    public void setFlow(int u,int v, NumericKey<E> flow) {
        this.edges[u][v].setFlow(flow);
    }

    /**
     * Sets capacity value for a certain edge.
     * @param u vertex which the edge is incident from
     * @param v vertex which the edge is incident to
     * @param capacity the edge's capacity vlue
     */
    public void setCapacity(int u, int v, NumericKey<E> capacity) {
        this.edges[u][v].setCapacity(capacity);
    }

    /**
     * Returns the number of vertices in the network.
     * @return number of vertices
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the network.
     * @return number of edges
     */
    public int E() {
        return E;
    }

    /**
     * Returns a stored network vertex at a given index.
     * @param v vertex index
     * @return network vertex
     */
    public T getVertex(int v) {
        return vertices[v];
    }

    /**
     * Checks if an edge exists in the network.
     * @param u vertex which the edge is incident from
     * @param v vertex which the edge is incident to
     * @return true if edge exits, otherwise, false.
     */
    public boolean hasEdge(int u, int v) {
        return edges[u][v] != null;
    }

    /**
     * Checks if an edge exists in the network.
     * @param e flow-network edge
     * @return true if edge exits, otherwise, false.
     */
    public boolean hasEdge(FlowNetworkEdge<T, E> e) {
        return edges[e.incidentFrom()][e.incidentTo()] != null;
    }

    /**
     * Finds and return an edge in the network.
     * @param u vertex which the edge is incident from
     * @param v vertex which the edge is incident to
     * @return the edge if existed, otherwise, null
     */
    public FlowNetworkEdge<T, E> findEdge(int u, int v) {
        return edges[u][v];
    }

    /**
     * Returns a list of all edges in the network as iterable.
     * @return network edges
     */
    public Iterable<FlowNetworkEdge<T, E>> getEdges() {
        LinkedList<FlowNetworkEdge<T, E>> output = new LinkedList<>();
        for (int u = 0; u < V; u++) {
            for (T v : outgoingEdges[u]) {
                output.add(edges[u][v.getVertex()]);
            }
        }
        return output;
    }

    /**
     * Returns list of incoming and outgoing edges for a given vertex.
     * @param u incident vertex
     * @return adjacent edges
     */
    public Iterable<FlowNetworkEdge<T, E>> neighbourEdges(T u) {
        return neighbourEdges(u.getVertex());
    }

    /**
     * Returns list of incoming and outgoing edges for a given vertex.
     * @param u index of incident vertex
     * @return adjacent edges
     */
    public Iterable<FlowNetworkEdge<T, E>> neighbourEdges(int u) {
        LinkedList<FlowNetworkEdge<T, E>> a = new LinkedList<>();
        NumericKey<E> zero = new NumericKey<>(NumericKey.ZERO);
        for (T v : outgoingEdges[u]) {
            FlowNetworkEdge<T, E> e = edges[u][v.getVertex()];
            if (e.getCapacity().compareTo(zero) == 1) {
                a.add(e);
            }
        }
        for (T w : incomingEdges[u]) {
            FlowNetworkEdge<T, E> e = edges[u][w.getVertex()];
            if (e.getCapacity().compareTo(zero) == 1) {
                a.add(e);
            }
        }
        return a;
    }

    /**
     * Returns adjacent vertices incident to incoming and outgoing edges of
     * a given vertex as iterable.
     * @param u the incident source vertex
     * @return adjacent vertices
     */
    public Iterable<Integer> neighbourVertices(T u) {
        return neighbourVertices(u.getVertex());
    }

    /**
     * Returns adjacent vertices incident to incoming and outgoing edges of
     * a given vertex as iterable.
     * @param u the index of the incident source vertex
     * @return adjacent vertices
     */
    public Iterable<Integer> neighbourVertices(int u) {
        LinkedList<Integer> a = new LinkedList<>();
        for (T v : outgoingEdges[u]) {
            a.add(v.getVertex());
        }
        for (T w : incomingEdges[u]) {
            a.add(w.getVertex());
        }
        return a;
    }

    public Iterable<FlowNetworkEdge<T, E>> outEdges(int uInd) {
        LinkedList<FlowNetworkEdge<T, E>> out = new LinkedList<>();
        for (T v : outgoingEdges[uInd]) {
            int vInd = v.getVertex();
            out.add(edges[uInd][vInd]);
        }
        return out;
    }

    /**
     * Returns a string representaion of the flow-network.
     * @return flow-network as a string representaion
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < V; i++) {
            builder.append("[" + i + "]: ");
            for (T v : outgoingEdges[i]) {
                NumericKey<E> c = edges[i][v.getVertex()].getCapacity();
                NumericKey<E> f = edges[i][v.getVertex()].getFlow();
                builder.append(v.getVertex() + "(" + f + "/" + c + ") ");
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
