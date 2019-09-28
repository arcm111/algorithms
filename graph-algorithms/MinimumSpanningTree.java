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
    public static <T extends VertexInterface, E extends Number> 
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
     * @param G the weighted undirected graph.
     * @return the minimum-spanning-tree of the graph.
     */
    @SuppressWarnings("unchecked")
    public static <T extends VertexInterface, E extends Number> 
            WeightedGraph<T, E> prim(WeightedGraph<T, E> G) {
        int n = G.V();
        // store all graph vertices in an array with maximum weights +inf
        WeightedVertex<T, E>[] vertices = 
                (WeightedVertex<T, E>[]) new WeightedVertex[n];
        for (int i = 0; i < n; i++) {
            WeightedVertex<T, E> v = new WeightedVertex<>(G.getVertex(i));
            v.setKey(WeightedVertex.POSITIVE_INFINITY);
            v.parent = WeightedVertex.NIL;
            vertices[i] = v;
        }
        for (int i = 0; i < n; i++) {
            if (vertices[i].parent == WeightedVertex.NIL) {
                prim(G, vertices, n, i);
            }
        }
        // add all final results of the algorithm to a new graph
        // representing the minimum spanning tree of the original graph
        WeightedGraph<T, E> MST = G.newInstance();
        for (int i = 0; i < n; i++) {
            WeightedVertex<T, E> x = vertices[i];
            if (x.parent != WeightedVertex.NIL) MST.addEdge(x.parent, i, x.key);
        }
        return MST;
    }

    /**
     * Perform prim's algorithm using a specific starting vertex.
     */
    public static <T extends VertexInterface, E extends Number> 
            void prim(WeightedGraph<T, E> G, 
                    WeightedVertex<T, E>[] vertices, int n, int r) {
        // set the weight of the source vertex to min value -inf
        vertices[r].setKey(WeightedVertex.NEGATIVE_INFINITY);
        // add all vertices to the priority queue and run prim algorithm
        // decreasing the weights of the stored vertices whenever we find
        // a smaller weight edge incident to them.
        PrimMinPriorityQueue<T, E> Q = new PrimMinPriorityQueue<>(vertices, n);
        while (!Q.isEmpty()) {
            WeightedVertex<T, E> u = Q.extractMin();
            // find the other vertices of u's adjacent edges
            for (WeightedVertex<T, E> w : G.adjEdges(u)) {
                // compare the graph edge's weight to the stored vertex's weight
                WeightedVertex<T, E> v = vertices[w.getVertex()];
                if (Q.inQueue(v) && w.compareTo(v) == -1) {
                    v.parent = u.getVertex();
                    Q.decreaseKey(v, w.key);
                }
            }
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
