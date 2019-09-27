import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * A directed weighted graph implementation using adjacenty-lists.
 */
public class WeightedDirectedGraph
        <T extends VertexInterface, E extends Number> 
        extends WeightedGraph<T, E> {
    /**
     * Constructor for initializing the vertices array.
     * Creats an empty adjacency list of all the vertices in the graph.
     * @param C the class of the generic type parameter T.
     * @param V the number of vertices in the graph.
     * @throws Exception if {@code makeInstance} failed.
     */
    public WeightedDirectedGraph(T[] vertices) {
        super(vertices);
    }

    public WeightedDirectedGraph(Class<T> C, int V) {
        super(C, V);
    }

    /**
     * creates a copy of this graph containing only vertices but no edges.
     */
    @Override
    public WeightedDirectedGraph<T, E> newInstance() {
        return new WeightedDirectedGraph<T, E>(C, V);
    }

    /**
     * Adds a directed edges to the adjacency list.
     * @param x the index of the first vertex.
     * @param y the index of the second vertex.
     */
    public void addEdge(int x, int y, E w) {
        validateVertex(x);
        validateVertex(y);
        adj[x].add(new WeightedVertex<T, E>(vertices[y], x, w));
        this.E++;
    }

    /**
     * Clones the graph.
     * Returns an immutable copy of the current graph in <em>O(V + E)</em>.
     * @return a copy of the graph as {@code Object}.
     */
    public WeightedDirectedGraph<T, E> copy() {
        WeightedDirectedGraph<T, E> G = new WeightedDirectedGraph<>(C, V);
        G.copyEdges(adj, V);
        return G;
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
