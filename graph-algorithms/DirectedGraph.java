/**
 * An undirected unweighted graph implementation using adjacenty-lists.
 * Self-loops are forbidden in undirected graphs. {@code addEdge} prevents
 * adding such cycles.
 */
public class DirectedGraph <T extends VertexInterface> extends Graph<T> {
    /**
     * Constructor.
     */
    public DirectedGraph(Class<T> C, int V) {
        super(C, V);
    }

    public DirectedGraph(T[] vertices) {
        super(vertices);
    }

    /**
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws IllegalArgumentException if the supplied indexes are invalid.
     */
    @Override
    public void addEdge(int x, int y) {
        validateVertex(x);
        validateVertex(y);
        adj[x].add(vertices[y]);
        this.E++;
    }

    @Override
    public DirectedGraph<T> copy() {
        DirectedGraph<T> graph = new DirectedGraph<>(C, V);
        graph.copyEdges(adj, V);
        return graph;
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
        System.out.println(G);

        // Testing transpose method
        System.out.println("G transposed: ");
        G.transpose();
        System.out.println(G);
    }
}
