/**
 * Breadth-first-search algorithm for searching a grahp.
 *
 * Works both for directed and undirected graphs.
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
public class BFS {
    private Queue Q;
    private final Graph G;
    private final int s;

    public BFS(Graph G, int s) {
        this.G = G;
        this.s = s;
    }

    private static enum Colour {
        WHITE,
        GREY,
        BLACK
    }

    private class Vertex {
        public Colour colour;
        public int d;
        public int pi;
    }
}
