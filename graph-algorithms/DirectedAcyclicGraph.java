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
        extends DirectedGraph<T> {
    /**
     * Constructor.
     */
    public DirectedAcyclicGraph(Class<T> C, int V) {
        super(C, V);
    }

    /**
     * Adds edges to the adjacency list using indexes of the stored vertices.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     * @throws IllegalArgumentException if x or y are invalid.
     * @throws IllegalArgumentException if x equals y.
     */
    @Override
    public void addEdge(int x, int y) {
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
