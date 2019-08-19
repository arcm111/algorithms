import java.util.Arrays;

/**
 * An undirected unweighted graph implementation using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class DirectedGraph <T extends VertexInterface> 
        implements GraphInterface<T> {
    public final int V;
    public int E = 0;
    public final LinkedList<T>[] adj;
    public final T[] vertices;

    /**
     * Constructor.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param V the number of vertices in the graph.
     */
    @SuppressWarnings("unchecked")
    public DirectedGraph(int V) {
        this.V = V;
        this.adj = (LinkedList<T>[]) new LinkedList[V];
        this.vertices = null;
        for (int i = 0; i < V; i++) adj[i] = new LinkedList<T>();
    }

    /**
     * Constructor for initializing the vertices array.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param C the class of the generic type parameter T.
     * @param V the number of vertices in the graph.
     * @throws Exception if {@code makeInstance} failed.
     */
    @SuppressWarnings("unchecked")
    public DirectedGraph(Class<T> C, int V) {
        this.V = V;
        this.adj = (LinkedList<T>[]) new LinkedList[V];
        this.vertices = (T[]) new VertexInterface[V];
        for (int i = 0; i < V; i++) {
            try {
                vertices[i] = makeInstance(C, i);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
            adj[i] = new LinkedList<T>();
        }
    }

    private T makeInstance(Class<T> c, int i) throws Exception {
        return c.getDeclaredConstructor(Integer.class).newInstance(i);
    }

    /**
     * Returns an iterable of the adjacecy vertices of a vertex v.
     * @param v the vertex to get the adjacency list for.
     * @throws IllegalArgumentException if v is invalid.
     */
    public Iterable<T> adj(T x) {
        int v = x.getVertex();
        validateVertex(v);
        return adj[v];
    }

    /**
     * Add an edge to the graph.
     * It is an undirected graph so add edges in both directions.
     * @param v first vertex of the edge.
     * @param w second vertex of the edge.
     * @throws IllegalArgumentException if either v or w are invalid.
     */
    public void addEdge(T x, T y) {
        int v = x.getVertex();
        int w = y.getVertex();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(y);
        this.E++;
    }

    /**
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws UnsupportedOperationException if the vertices array is not
     *		   initialized using the second constructor.
     * @throws IllegalArgumentException if the supplied indexes are invalid.
     */
    public void addEdge(int x, int y) {
        if (vertices == null) {
            // vertices must be instantiated first before using this
            // method by passing the T class to the instructor.
            throw new UnsupportedOperationException("wrong instructor used");
        }
        validateVertex(x);
        validateVertex(y);
        adj[x].add(vertices[y]);
        this.E++;
    }

    /**
     * Validates vertices to be within valid values: 0 and V-1.
     * @param v vertex to be validated.
     * @throws IllegalArgumentException if v is invalid.
     */
    public void validateVertex(int v) {
        if (v < 0 || v >= V) {
            String err = "vertex " + v + " is not between 0 and " + (V - 1);
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * Gets the stored vertex at a given index.
     * @param v the index of the vertex to find.
     * @throws IllegalArgumentException if the index v is invalid.
     */
    public T getVertex(int v) {
        validateVertex(v);
        return vertices[v];
    }

    /**
     * Returns an iterator which iterates over all the stored vertices.
     * @throws UnsupportedOperationException if vertices are uninitialized.
     */
    public Iterable<T> getVertices() {
        if (vertices == null) {
            // vertices must be instantiated first before using this
            // method by passing T class to the constructor.
            throw new UnsupportedOperationException("wrong instructor used");
        }
        return Arrays.asList(vertices);
    }

    /**
     * Generates a string representation of the graph.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("V: " + V + ", E: " + E + "\n");
        for (int v = 0; v < V; v++) {
            builder.append("[" + v + "]: ");
            for (T x : adj[v]) {
                builder.append(x.toString() + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // 0->1  2
        // | ^| /|
        // v/ vv v
        // 3<-4  5<>
        int V = 6;
        DirectedGraph<DFSVertex> G = new DirectedGraph<>(DFSVertex.class, V);
        G.addEdge(0, 1);
        G.addEdge(0, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 4);
        G.addEdge(2, 5);
        G.addEdge(3, 1);
        G.addEdge(4, 3);
        G.addEdge(5, 5);
        System.out.println(G.toString());
    }
}
