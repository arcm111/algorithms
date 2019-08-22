import java.util.Arrays;

/**
 * An undirected unweighted graph implementation using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class Graph<T extends VertexInterface> implements GraphInterface<T> {
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
    public Graph(int V) {
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
    public Graph(Class<T> C, int V) {
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
     * @throws IllegalArgumentException if v equals w to prevent self-loops.
     */
    public void addEdge(T x, T y) {
        int v = x.getVertex();
        int w = y.getVertex();
        validateVertex(v);
        validateVertex(w);
        if (v == w) {
            String ex = "self-loops are not allowed " + v + ":" + w;
            throw new IllegalArgumentException(ex);
        }
        adj[v].add(y);
        adj[w].add(x);
        this.E++;
    }

    /**
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws UnsupportedOperationException if the vertices array is not
     *		   initialized using the second constructor.
     * @throws IllegalArgumentException if the supplied indexes are not 
     *		   valid or are equal (thus creating a self loop).
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
            String ex = "self-loops are not allowed " + x + ":" + y;
            throw new IllegalArgumentException(ex);
        }
        adj[x].add(vertices[y]);
        adj[y].add(vertices[x]);
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
     * 0---1  
     * |  /|\   
     * | / | 2
     * |/  |/
     * 4---3
     * Above graph should be created with 5 vertices and 7 edges.
     */
    public static void main(String[] args) {
        Graph<Vertex> graph = new Graph<>(5);
        graph.addEdge(new Vertex(0), new Vertex(1));
        graph.addEdge(new Vertex(0), new Vertex(4));
        graph.addEdge(new Vertex(1), new Vertex(3));
        graph.addEdge(new Vertex(1), new Vertex(4));
        graph.addEdge(new Vertex(2), new Vertex(1));
        graph.addEdge(new Vertex(2), new Vertex(3));
        graph.addEdge(new Vertex(3), new Vertex(4));

        //Graph<Vertex> graph = new Graph<>(Vertex.class, 5);
        //graph.addEdge(0, 1);
        //graph.addEdge(0, 4);
        //graph.addEdge(1, 3);
        //graph.addEdge(1, 4);
        //graph.addEdge(2, 1);
        //graph.addEdge(2, 3);
        //graph.addEdge(3, 4);

        System.out.println(graph.toString());
    }
}
