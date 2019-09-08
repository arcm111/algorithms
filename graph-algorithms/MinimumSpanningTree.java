/**
 * Minimum spanning tree algorithms.
 * Works only on weighted, connected, undirected graphs.
 * It connects all vertices in the graph using minimimum weight of edges.
 * It has V - 1 edges and no cycles.
 * MST may not be unique, there could exist different MST for the same graph. 
 */
public class MinimumSpanningTree {
    private final static int NIL = -1;

    /**
     * Kruskal minimum-spanning-tree algorithm.
     * Greedy algorithm works by finding an edge with minimum weight and
     * adds it to the tree.
     * It sorts vertices by weight to find the light edge to add to MST.
     * It uses Disjoint-set-forests data structure to find safe edges in
     * each step to add to MST.
     * Running time <em>O(ElgV)</em>.
     * @return {@code WeightedGraph<T, E>} the minimum-spanning-tree.
     */
    public static <T extends VertexInterface, E extends Comparable<E>> 
            WeightedGraph<T, E> kruskal(WeightedGraph<T, E> G) {
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

    /**
     * Prim's algorithm for finding minimum spanning tree of a weighted graph.
     * It starts from an arbirary vertex and grows until it span all vertices
     * in the graph.
     * The starting vertex could be any vertex in the graph and the algorithm
     * would work regardless. However, the resulting MST might be different.
     * It uses a minimum-heap priority queue that can perform search
     * operation {@code inQueue} in constant time. The priority queue allows
     * it to find a safe light edge to add to MST in each step.
     * Running time <em>O(ElgV)</em>
     */
    public static <T extends VertexInterface, E extends Comparable<E>> 
            WeightedGraph<T, E> prim(WeightedGraph<T, E> G) {
        return prim(G, 0);
    }

    /**
     * Perform prim algorithm using a specific starting vertex.
     * Same as the method above except for the starting vertex which can
     * be chosen to be other than 0.
     */
    @SuppressWarnings("unchecked")
    public static <T extends VertexInterface, E extends Comparable<E>> 
            WeightedGraph<T, E> prim(WeightedGraph<T, E> G, int r) {
        WeightedGraph<T, E> MST = G.newInstance();
        int n = G.V();
        // store all graph vertices in an array of primvertex elements
        PrimVertex<T, E>[] vertices = (PrimVertex<T, E>[]) new PrimVertex[n];
        for (int i = 0; i < n; i++) {
            PrimVertex<T, E> v = new PrimVertex<>(G.getVertex(i));
            v.setKey(PrimVertex.POSITIVE_INFINITY);
            v.parent = PrimVertex.NIL;
            vertices[i] = v;
        }
        vertices[r].setKey(PrimVertex.NEGATIVE_INFINITY);
        PrimMinPriorityQueue<T, E> Q = new PrimMinPriorityQueue<>(vertices, n);
        while (!Q.isEmpty()) {
            PrimVertex<T, E> u = Q.extractMin();
            // Get edges indident from u and compare their weights to u.key
            for (WeightedEdge<T, E> e : G.adjEdges(u.vertex)) {
                PrimVertex<T, E> v = vertices[e.incidentTo()];
                if (Q.inQueue(v) && less(e.getWeight(), v)) {
                    v.parent = u.getVertex();
                    Q.decreaseKey(v, e.getWeight());
                }
            }
        }
        for (int i = 0; i < n; i++) {
            PrimVertex<T, E> x = vertices[i];
            if (x.parent != PrimVertex.NIL) MST.addEdge(x.parent, i, x.key);
        }
        return MST;
    }

    private static <T extends VertexInterface, E extends Comparable<E>> 
            boolean less(E w, PrimVertex<T, E> v) {
        if (v.infinity == PrimVertex.POSITIVE_INFINITY) return true;
        if (v.infinity == PrimVertex.NEGATIVE_INFINITY) return false;
        return w.compareTo(v.key) == -1;
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
        // testing kruskal and prim algorithms on G which should result in:
        //              7
        //     1     2-----3   
        //  4 /     / \     \ 9 
        //   /     /2  \     \ 
        //  0     8     \4    4
        //   \           \     
        //  8 \           \     
        //     7-----6-----5   
        //        1     2
        System.out.println("Minimum spanning tree (Kruskal): ");
        System.out.println(MinimumSpanningTree.kruskal(G));
        System.out.println("Minimum spanning tree (Prim): ");
        System.out.println(MinimumSpanningTree.prim(G));
    }
}
