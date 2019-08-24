import java.util.Arrays;

/**
 * An undirected unweighted graph implementation using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class Graph<T extends VertexInterface> implements GraphInterface<T> {
    protected final Class<T> C;
    protected int V;
    protected int E = 0;
    protected LinkedList<T>[] adj;
    protected T[] vertices;

    /**
     * Constructor for initializing the vertices array.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param C the class of the generic type parameter T.
     * @param V the number of vertices in the graph.
     * @throws Exception if {@code makeInstance} failed.
     */
    public Graph(Class<T> C, int V) {
        this.C = C;
        this.V = V;
        this.vertices = newVerticesArray(V);
        this.adj = newAdjacencyList(V);
    }

    @SuppressWarnings("unchecked")
    private T[] newVerticesArray(int V) {
        T[] arr = (T[]) new VertexInterface[V];
        for (int i = 0; i < V; i++) {
            try {
                arr[i] = makeInstance(C, i);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    private LinkedList<T>[] newAdjacencyList(int V) {
        LinkedList<T>[] adjList = (LinkedList<T>[]) new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<T>();
        }
        return adjList;
    }

    private T makeInstance(Class<T> c, int i) throws Exception {
        return c.getDeclaredConstructor(Integer.class).newInstance(i);
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
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws IllegalArgumentException if the supplied indexes are not 
     *		   valid or are equal (thus creating a self loop).
     */
    public void addEdge(int x, int y) {
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

    protected void copyEdges(LinkedList<T>[] adj, int V) {
        if (V != this.V) {
            throw new IllegalArgumentException("V invalid");
        }
        this.V = V;
        this.adj = newAdjacencyList(V);
        int E = 0;
        for (int u = 0; u < V; u++) {
            for (T v : adj[u]) {
                this.adj[u].add(vertices[v.getVertex()]);
                E++;
            }
        }
        this.E = E;
    }

    /**
     * Clones the graph.
     * Returns an immutable copy of the current graph in <em>O(V + E)</em>.
     * @return a copy of the graph as {@code Object}.
     */
    public Graph<T> copy() {
        Graph<T> graph = new Graph<>(C, V);
        graph.copyEdges(adj, V);
        return graph;
    }

    /**
     * Transposes this graph.
     * It reverse all edges in the graph.
     * Takes <em>O(V + E)</em> worst case running time.
     */
    public void transpose() {
        // create a new adjacency list
        LinkedList<T>[] newAdj = newAdjacencyList(V);
        // reverse every edge in the graph and add it to the new list
        for (T u : getVertices()) {
            for (T v : adj(u)) {
                newAdj[v.getVertex()].add(u);
            }
        }
        this.adj = newAdj;
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
     * @return vertices array as a {@code List<T>}.
     */
    public Iterable<T> getVertices() {
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
            if (v < V - 1) builder.append("\n");
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
        Graph<Vertex> G = new Graph<>(Vertex.class, 5);
        G.addEdge(0, 1);
        G.addEdge(0, 4);
        G.addEdge(1, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 1);
        G.addEdge(2, 3);
        G.addEdge(3, 4);
        System.out.println(G);
    }
}
