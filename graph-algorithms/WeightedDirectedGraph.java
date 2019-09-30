import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * A directed weighted graph implementation using adjacenty-lists.
 */
public class WeightedDirectedGraph <T extends VertexInterface, E extends Number>
        extends DirectedGraph<T> {
    protected LinkedList<WeightedVertex<T, E>>[] adjVertices; //out-degree edges

    /**
     * Constructor for initializing the vertices array.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param C the class of the generic type parameter T.
     * @param V the number of vertices in the graph.
     * @throws Exception if {@code makeInstance} failed.
     */
    public WeightedDirectedGraph(Class<T> C, int V) {
        super(C, V);
        initAdjVertices(V);
    }

    public WeightedDirectedGraph(T[] vertices) {
        super(vertices);
        initAdjVertices(V);
    }

    @SuppressWarnings("unchecked")
    private void initAdjVertices(int n) {
        this.adjVertices = 
                (LinkedList<WeightedVertex<T, E>>[]) new LinkedList[n];
        for (int i = 0; i < n; i++) {
            adjVertices[i] = new LinkedList<>();
        }
    }
    
    @Override
    public void addEdge(int u, int v) {
        addEdge(u, v, null);
    }

    /**
     * Adds a directed edges to the adjacency list.
     * @param x the index of the first vertex
     * @param y the index of the second vertex
     * @param w the weight of the edge
     */
    public void addEdge(int x, int y, E w) {
        validateVertex(x);
        validateVertex(y);
        WeightedVertex<T, E> z = new WeightedVertex<>(vertices[y], x);
        if (w == null) {
            z.setKey(WeightedVertex.POSITIVE_INFINITY);
        } else {
            z.setKey(w);
        }
        adjVertices[x].add(z);
        this.E++;
    }

    public Iterable<WeightedVertex<T, E>> adjEdges(T u) {
        return adjVertices[u.getVertex()];
    }

    /**
     * creates a copy of this graph containing only vertices but no edges.
     */
    public WeightedDirectedGraph<T, E> newInstance() {
        if (C == null) {
            throw new UnsupportedOperationException();
        }
        return new WeightedDirectedGraph<T, E>(C, V);
    }

    /**
     * Clones the graph.
     * Returns an immutable copy of the current graph in <em>O(V + E)</em>.
     * @return a copy of the graph as {@code Object}.
     */
    public WeightedDirectedGraph<T, E> copy() {
        if (C == null) {
            throw new UnsupportedOperationException();
        }
        WeightedDirectedGraph<T, E> G = new WeightedDirectedGraph<>(C, V);
        G.copyEdges(adj, V);
        return G;
    }

    /**
     * Returns a list of all the edges in the graph.
     * @return the edges as a {@code List<WeightedEdge<T, E>>}
     */
    public List<WeightedEdge<T, E>> getEdges() {
        List<WeightedEdge<T, E>> edges = new ArrayList<>();
        for (int u = 0; u < V; u++) {
            for (WeightedVertex<T, E> v : adjVertices[u]) {
                edges.add(new WeightedEdge<>(vertices[u], v.vertex, v.key));
            }
        }
        return edges;
    }

    /**
     * Sort this graph edges by weight and return an iterable.
     * @return iterable of the sorted edges.
     */
    public Iterable<WeightedEdge<T, E>> sortedEdges() {
        List<WeightedEdge<T, E>> edges = getEdges();
        Collections.sort(edges);
        return edges;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // testing graph construction
        //
        //    3
        //  0---->1  
        //  |    /|^  
        //  |   / | \7
        // 5| 4/ 3|  2
        //  | /   | /9
        //  VV    VV
        //  4-----3
        //    2
        //
        WeightedDirectedGraph<Vertex, Integer> G = 
                new WeightedDirectedGraph<>(Vertex.class, 5);
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
