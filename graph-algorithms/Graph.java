/**
 * An implementation of undirected graphs using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class Graph<T extends VertexInterface> {
    public final int V;
    public int E = 0;
    public final LinkedList<T>[] adj;

    /**
     * Constructor.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param V the number of vertices in the graph.
     */
    @SuppressWarnings("unchecked")
    public Graph(int V) {
        this.V = V;
        this.adj = (LinkedList<T>[]) new LinkedList[V];
        for (int i = 0; i < V; i++) adj[i] = new LinkedList<T>();
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
        class Vertex implements VertexInterface {
            public final int v;

            public Vertex(int v) {
                this.v = v;
            }

            @Override
            public int getVertex() {
                return v;
            }

            @Override
            public String toString() {
                return Integer.toString(v);
            }
        }

        Graph<Vertex> graph = new Graph<>(5);
        graph.addEdge(new Vertex(0), new Vertex(1));
        graph.addEdge(new Vertex(0), new Vertex(4));
        graph.addEdge(new Vertex(1), new Vertex(3));
        graph.addEdge(new Vertex(1), new Vertex(4));
        graph.addEdge(new Vertex(2), new Vertex(1));
        graph.addEdge(new Vertex(2), new Vertex(3));
        graph.addEdge(new Vertex(3), new Vertex(4));
        System.out.println(graph.toString());
    }
}
