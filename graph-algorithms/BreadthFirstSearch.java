/**
 * Breadth-first-search algorithm for searching a grahp.
 *
 * Works both for directed and undirected graphs. However, in this 
 * implementation we are using undirected graphs only.
 *
 * Given a graph <em>G</em> and a source vertex <em>s</em>, the algorithm
 * discover every vertex in <em>G</em> reachable from <em>s</em> and computes
 * its distance from the source.
 *
 * The algorithm also produces a breadth-first-tree rooted at <em>s</em>
 * and contains all reachable vertices from <em>s</em>.
 *
 * It is so named because is expands the border between discovered and 
 * undiscovered vertices uniformally across the breadth of the border.
 */
public class BreadthFirstSearch {
    /**
     * Performs breadth-first-search on graph <em>G</em> from the
     * source <em>source</em>.
     * @param G the undirected graph.
     * @param T the source vertex.
     * @throws IllegalArgumentException if source is an invalid vertex.
     */
    public static <T extends BFSVertex, E extends GraphInterface<T>> 
            void process(E G, T source) {
        G.validateVertex(source.getVertex());
        // every vertex in G has already been instantiated to have the values: 
        // colour=WHITE, d=INFINITY and pi=NIL by default.
        source.colour = T.Colour.GREY;
        source.d = 0;
        Queue<T> Q = new Queue<>();
        Q.enqueue(source);
        while (!Q.isEmpty()) {
            T u = Q.dequeue();
            for (T v : G.adj(u)) {
                if (v.colour == T.Colour.WHITE) {
                    v.colour = T.Colour.GREY;
                    v.d = u.d + 1;
                    v.pi = u;
                    Q.enqueue(v);
                }
            }
            u.colour = T.Colour.BLACK;
        }
    }

    /**
     * Runs BFS on a graph using a given source.
     * @param G the undirected graph.
     * @param T the index of the source vertex.
     * @throws IllegalArgumentException if source is an invalid vertex.
     */
    public static <T extends BFSVertex> void process(Graph<T> G, int s) {
        G.validateVertex(s);
        process(G, G.getVertex(s));
    }

    /**
     * Unit tests.
     * 0--1  2--3
     * |  | /| /|
     * |  |/ |/ |
     * 4  5--6--7
     */
    public static void main(String[] args) {
        int V = 8;

        Graph<BFSVertex> G = new Graph<>(BFSVertex.class, V);
        G.addEdge(0, 1);
        G.addEdge(0, 4);
        G.addEdge(1, 5);
        G.addEdge(2, 5);
        G.addEdge(2, 6);
        G.addEdge(2, 3);
        G.addEdge(3, 6);
        G.addEdge(3, 7);
        G.addEdge(5, 6);
        G.addEdge(6, 7);

        System.out.println(G.toString());
        System.out.println("before BFS: ");
        for (BFSVertex x : G.getVertices()) System.out.println(x);
        BreadthFirstSearch.process(G, 1);
        System.out.println("after BFS: ");
        for (BFSVertex x : G.getVertices()) System.out.println(x);
    }
}
