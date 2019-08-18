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
    private static final int NIL = -1;
    private static final int INFINITY = Integer.MAX_VALUE;
    
    /**
     * Performs breadth-first-search on graph <em>G</em> from the
     * source <em>source</em>.
     * @param G the undirected graph.
     * @param T the source vertex.
     * @throws IllegalArgumentException if source is an invalid vertex.
     */
    public <T extends Vertex> void process(Graph<T> G, T source) {
        G.validateVertex(source.getVertex());
        // every vertex in G has already been instantiated to have the values: 
        // colour=WHITE, d=INFINITY and pi=NIL by default.
        source.colour = Vertex.Colour.GREY;
        source.d = 0;
        Queue<T> Q = new Queue<>();
        Q.enqueue(source);
        while (!Q.isEmpty()) {
            T u = Q.dequeue();
            for (T v : G.adj(u)) {
                if (v.colour == Vertex.Colour.WHITE) {
                    v.colour = Vertex.Colour.GREY;
                    v.d = u.d + 1;
                    v.pi = u;
                    Q.enqueue(v);
                }
            }
            u.colour = Vertex.Colour.BLACK;
        }
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
        Graph<Vertex> G = new Graph<>(V);
        Vertex[] vertices = new Vertex[V];
        for (int i = 0; i < V; i++) vertices[i] = new Vertex(i);
        G.addEdge(vertices[0], vertices[1]);
        G.addEdge(vertices[0], vertices[4]);
        G.addEdge(vertices[1], vertices[5]);
        G.addEdge(vertices[2], vertices[5]);
        G.addEdge(vertices[2], vertices[6]);
        G.addEdge(vertices[2], vertices[3]);
        G.addEdge(vertices[3], vertices[6]);
        G.addEdge(vertices[3], vertices[7]);
        G.addEdge(vertices[5], vertices[6]);
        G.addEdge(vertices[6], vertices[7]);

        System.out.println("before BFS: ");
        System.out.println(G.toString());
        BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.process(G, vertices[1]);
        System.out.println("after BFS: ");
        System.out.println(G.toString());
    }
}
