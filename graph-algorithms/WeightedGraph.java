import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * An undirected weighted graph implementation using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class WeightedGraph<T extends VertexInterface, E extends Number> 
        implements GraphInterface<T> {
    protected final Class<T> C;
    protected int V;
    protected int E = 0;
    protected T[] vertices;
    protected LinkedList<WeightedEdge<T, E>>[] adj;

    /**
     * Constructor for initializing the vertices array.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param C the class of the generic type parameter T.
     * @param V the number of vertices in the graph.
     * @throws Exception if {@code makeInstance} failed.
     */
    public WeightedGraph(Class<T> C, int V) {
        this.C = C;
        this.V = V;
        this.vertices = newVerticesArray(V);
        this.adj = newAdjacencyList(V);
    }

    /**
     * creates a copy of this graph containing only vertices but no edges.
     */
    public WeightedGraph<T, E> newInstance() {
        return new WeightedGraph<T, E>(C, V);
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
    private LinkedList<WeightedEdge<T, E>>[] newAdjacencyList(int V) {
        LinkedList<WeightedEdge<T, E>>[] adjList = 
                (LinkedList<WeightedEdge<T, E>>[]) new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<WeightedEdge<T, E>>();
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
        LinkedList<T> iter = new LinkedList<>();
        for (WeightedEdge<T, E> e : adj[v]) iter.add(e.getDstVertex());
        return iter;
    }

    /**
     * Returns an iterable of the outdegree edges of a vertex v.
     * @param v the vertex to get the adjacency list for.
     * @throws IllegalArgumentException if v is invalid.
     */
    public Iterable<WeightedEdge<T, E>> adjEdges(T x) {
        int v = x.getVertex();
        validateVertex(v);
        return adj[v];
    }

    public List<WeightedEdge<T, E>> getEdges() {
        List<WeightedEdge<T, E>> edges = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            for (WeightedEdge<T, E> e : adj[i]) edges.add(e);
        }
        return edges;
    }

    /**
     * Sort this graph edges by weight and return an iterable.
     * @return iterable of the sorted edges.
     */
    public Iterable<WeightedEdge<T, E>> sortedEdges() {
        List<WeightedEdge<T, E>> edges = getEdges();
        Collections.sort(edges, new Comparator<WeightedEdge<T, E>>(){
            @Override
            public int compare(WeightedEdge<T, E> a, WeightedEdge<T, E> b){
                E w1 = a.getWeight();
                E w2 = b.getWeight();
                return (int) (w1.floatValue() - w2.floatValue());
            }
        });
        return edges;
    }

    public void addEdge(int x, int y) {
        addEdge(x, y, null);
    }

    /**
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws IllegalArgumentException if the supplied indexes are not 
     *		   valid or are equal (thus creating a self loop).
     */
    public void addEdge(int x, int y, E w) {
        validateVertex(x);
        validateVertex(y);
        if (x == y) {
            String ex = "self-loops are not allowed " + x + ":" + y;
            throw new IllegalArgumentException(ex);
        }
        adj[x].add(new WeightedEdge<T, E>(vertices[x], vertices[y], w));
        adj[y].add(new WeightedEdge<T, E>(vertices[y], vertices[x], w));
        this.E++;
    }

    protected void copyEdges(LinkedList<WeightedEdge<T, E>>[] adj, int V) {
        if (V != this.V) {
            throw new IllegalArgumentException("V invalid");
        }
        this.V = V;
        this.adj = newAdjacencyList(V);
        int E = 0;
        for (int u = 0; u < V; u++) {
            for (WeightedEdge<T, E> e : adj[u]) {
                T source = vertices[e.incidentFrom()];
                T dst = vertices[e.incidentTo()];
                E weight = e.getWeight();
                this.adj[u].add(new WeightedEdge<T, E>(source, dst, weight));
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
    public WeightedGraph<T, E> copy() {
        WeightedGraph<T, E> G = new WeightedGraph<>(C, V);
        G.copyEdges(adj, V);
        return G;
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
            for (WeightedEdge<T, E> x : adj[v]) {
                builder.append(x.getDstVertex() + ":" + x.getWeight() + " ");
            }
            if (v < V - 1) builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Unit tests.
     *    3
     *  0---1  
     *  |  /|\7  
     * 5|4/3| 2
     *  |/  |/9
     *  4---3
     *    2
     * Above graph should be created with 5 vertices and 7 edges.
     */
    public static void main(String[] args) {
        // testing graph construction
        WeightedGraph<Vertex, Integer> G = new WeightedGraph<>(Vertex.class, 5);
        G.addEdge(0, 1, 3);
        G.addEdge(0, 4, 5);
        G.addEdge(1, 3, 3);
        G.addEdge(1, 4, 4);
        G.addEdge(2, 1, 7);
        G.addEdge(2, 3, 9);
        G.addEdge(3, 4, 2);
        System.out.println(G);

        // testing sortedEdges
        System.out.println("Sorted edges: ");
        for (WeightedEdge<Vertex, Integer> e : G.sortedEdges()) {
            System.out.println(e);
        }
    }
}
