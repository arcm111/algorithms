/**
 * An implementation of undirected graphs using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class Graph {
    public final int V;
    public int E = 0;
    public final LinkedList[] adj;

    /**
     * Constructor.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param V the number of vertices in the graph.
     */
    public Graph(int V) {
        this.V = V;
        this.adj = new LinkedList[V];
        for (int i = 0; i < V; i++) adj[i] = new LinkedList();
    }

    /**
     * Returns an iterable of the adjacecy vertices of a vertex v.
     * @param v the vertex to get the adjacency list for.
     * @throws IllegalArgumentException if v is invalid.
     */
    public Iterable<Integer> adj(int v) {
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
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (v == w) {
            String ex = "self-loops are not allowed " + v + ":" + w;
            throw new IllegalArgumentException(ex);
        }
        adj[v].add(w);
        adj[w].add(v);
        this.E++;
    }

    /**
     * Validates vertices to be within valid values: 0 and V-1.
     * @param v vertex to be validated.
     * @throws IllegalArgumentException if v is invalid.
     */
    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            String err = "vertex " + v + " is not between 0 and " + (V - 1);
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * Generates a string representation of the graph.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("V: " + V + ", E: " + E + "\n");
        for (int v = 0; v < V; v++) {
            builder.append("[" + v + "]: ");
            for (int w : adj[v]) {
                builder.append(w + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 4);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        System.out.println(graph.toString());
    }
}
