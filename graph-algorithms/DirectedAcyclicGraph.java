import java.util.Arrays;

/**
 * A directed acyclic graph implementation using adjacency lists.
 * <p>It throws an exception if adding a new edge creates a cycle or a 
 * self-loop.
 * <p>Initialization running time is <em>O(E^2 + EV)</em> because every 
 * {@code areConnected} takes <em>O(E + V)</em> and we do <em>E</em>
 * Operations with <em>E</em> increasing from <em>1</em> to <em>E</em>
 * <em>O(/sum_{i=1}^E (V + i)) = O(E^2 + EV)</em>.
 */
public class DirectedAcyclicGraph<T extends VertexInterface>
        implements GraphInterface<T> {
    public final int V;
    public int E = 0;
    private final LinkedList<T>[] adj;
    private final T[] vertices;

    /**
     * Constructor.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param V the number of vertices in the graph.
     */
    @SuppressWarnings("unchecked")
    public DirectedAcyclicGraph(int V) {
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
    public DirectedAcyclicGraph(Class<T> C, int V) {
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

    /**
     * Returns the number of vertices in the graph.
     * @Return V the number of vertices.
     */
    @Override
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the graph.
     * @Return E the number of edges.
     */
    @Override
    public int E() {
        return E;
    }

    /**
     * Creates an instance of the supplied vertex class <em>T</em>.
     * The vertex index is passed to the constructor of the provided class.
     * @param c the class of the {@code VertexInterface} implementation class.
     * @param i the index of the vertex to be instantiated.
     * @return an instance of <em>T</em> with a vertex index <em>i</em>.
     */
    private T makeInstance(Class<T> c, int i) throws Exception {
        return c.getDeclaredConstructor(Integer.class).newInstance(i);
    }

    /**
     * Returns an iterable of the adjacecy vertices of a vertex v.
     * @param v the vertex to get the adjacency list for.
     * @return the iterable.
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
        addEdge(v, w);
    }

    /**
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws UnsupportedOperationException if the vertices array is not
     *		   initialized using the second constructor.
     * @throws IllegalArgumentException if x or y are invalid.
     * @throws IllegalArgumentException if x equals y.
     */
    public void addEdge(int x, int y) {
        if (vertices == null) {
            // vertices must be instantiated first before using this
            // method by passing the T class to the instructor.
            throw new UnsupportedOperationException("wrong instructor used");
        }
        validateVertex(x);
        validateVertex(y);
        if (x == y) {
            throw new IllegalArgumentException("self-loops not allowed: " + x);
        }
        if (DepthFirstSearch.areConnected(this, y, x)) {
            String err = "adding edge (%d, %d) will create a cycle";
            String ex = String.format(err, x, y);
            throw new IllegalArgumentException(ex);
        }
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
     * @return the vertex with index v in the vertices array.
     * @throws IllegalArgumentException if the index v is invalid.
     */
    public T getVertex(int v) {
        validateVertex(v);
        return vertices[v];
    }

    /**
     * Returns an iterator which iterates over all the stored vertices.
     * @return an iterable list of the vertices;
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
     * @Return String the representation.
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
        //
        // The following graph contains a cycle which should be detected 
        // after adding the edge that creates the cycle. If detected, an 
        // IllegalArgumentException will be thrown.
        int V = 6;
        DirectedAcyclicGraph<DFSVertex> G =
                new DirectedAcyclicGraph<>(DFSVertex.class, V);
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
