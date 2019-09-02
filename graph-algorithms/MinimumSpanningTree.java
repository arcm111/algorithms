/**
 * Minimum spanning tree algorithms.
 * Works only on weighted undirected graphs.
 */
public class MinimumSpanningTree {
    private final static int NIL = -1;
    /**
     * Kruskall minimum-spanning-tree algorithm.
     * Greedy algorithm works by finding an edge with minimum weight and
     * adds it to the tree (or forests).
     * Running time <em>O(E.lg(V))</em>.
     * @return {@code WeightedGraph<T, E>} the minimum-spanning-tree.
     */
    public static <T extends VertexInterface, E extends Number> 
            WeightedGraph<T, E> kruskall(WeightedGraph<T, E> G) {
        // create a copy of the graph without the edges.
        WeightedGraph<T, E> MST = G.newInstance();
        DisjointSetForests UF = new DisjointSetForests(G.V());
        for (WeightedEdge<T, E> e : G.sortedEdges()) {
            int u = e.incidentFrom();
            int v = e.incidentTo();
            // same as {@code if (!UF.isConnected(u, v))}
            if (UF.findSet(u) != UF.findSet(v)) {
                MST.addEdge(u, v, e.getWeight());
                UF.union(u, v);
            }
        }
        return MST;
    }

    public static <T extends VertexInterface, E extends Number> 
            WeightedGraph<T, E> prim(WeightedGraph<T, E> G, int r) {
        WeightedGraph<T, E> MST = G.newInstance();
        int n = G.V();
        PrimVertex[] vertices = new PrimVertex[n];
        for (int i = 0; i < n; i++) {
            PrimVertex v = new PrimVertex(i);
            v.key = Integer.MAX_VALUE;
            v.parent = NIL;
            vertices[i] = v;
        }
        vertices[r].key = 0;
        MinPriorityQueue<PrimVertex> Q = new MinPriorityQueue<>(vertices, n);
        while (!Q.isEmpty()) {
            PrimVertex u = Q.extractMin();
            for (WeightedEdge e : G.adj(u.index)) {
            }
        }
    }

    private class PrimVertex extends Vertex, Comparable {
        public final int index;
        public int key;
        public int parent;

        public Vertex(int index) {
            this.index = index;
        }

        @Override
        public int compareTo(PrimVertex that) {
            return this.key - that.key;
        }
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        //        8     7
        //     1-----2-----3   
        //  4 /|11  / \  14|\ 9 
        //   / |   /2  \   | \ 
        //  0  |  8     \4 |  4
        //   \ | / \     \ | / 
        //  8 \|/7  \6    \|/ 10
        //     7-----6-----5   
        //        1     2
        WeightedGraph<Vertex, Integer> G = new WeightedGraph<>(Vertex.class, 9);
        G.addEdge(0, 1, 4);
        G.addEdge(0, 7, 8);
        G.addEdge(1, 2, 8);
        G.addEdge(1, 7, 11);
        G.addEdge(2, 3, 7);
        G.addEdge(2, 5, 4);
        G.addEdge(2, 8, 2);
        G.addEdge(3, 4, 9);
        G.addEdge(3, 5, 14);
        G.addEdge(4, 5, 10);
        G.addEdge(5, 6, 2);
        G.addEdge(6, 7, 1);
        G.addEdge(6, 8, 6);
        G.addEdge(7, 8, 7);
        System.out.println(G);
        // testing kruskall which should results in:
        //              7
        //     1     2-----3   
        //  4 /     / \     \ 9 
        //   /     /2  \     \ 
        //  0     8     \4    4
        //   \           \     
        //  8 \           \     
        //     7-----6-----5   
        //        1     2
        System.out.println("Minimum spanning tree: ");
        System.out.println(MinimumSpanningTree.kruskall(G));
    }
}
