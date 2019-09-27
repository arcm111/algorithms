public class ShortestPath {
    /**
     * Bellman-Ford algorithm.
     * @param G the weighted directed graph
     * @param s the source vertex
     * @return null if the graph contians negative weight cycles
     *		   Otherwise, it returns the predecessor subgraph
     */
    public static <E extends Number, T extends ShortestPathVertex<E>>
            boolean bellmanFordExtended(WeightedDirectedGraph<T, E> G, int s) {
        initializeSingleSource(G, s);
        for (int i = 0; i < G.V(); i++) {
            for (WeightedEdge<T, E> e : G.getEdges()) {
                ShortestPathVertex<E> u = e.getSourceVertex();
                ShortestPathVertex<E> v = e.getDstVertex();
                if (u.infinity == ShortestPathVertex.POSITIVE_INFINITY) {
                    continue;
                }
                relax(u, v, e.getWeight());
            }
        }
        for (WeightedEdge<T, E> e : G.getEdges()) {
            ShortestPathVertex<E> u = e.getSourceVertex();
            ShortestPathVertex<E> v = e.getDstVertex();
            if (!triangleInequality(u, v, e.getWeight())) {
                // negative weight cycle detected
                return false;
            }
        }
        return true;
    }

    public static <E extends Number, T extends ShortestPathVertex<E>>
            void DAGShortestPath(WeightedDirectedAcyclicGraph<T, E> G, 
                    int s) {
        initializeSingleSource(G, s);
        for (T u : DepthFirstSearch.topologicalSort(G)) {
            for (WeightedVertex<T, E> x : G.adjEdges(u)) {
                T v = x.vertex;
                relax(u, v, x.getKey());
            }
        }
    }

    /**
     * Initializes single source predecessors and distances to default.
     * @param G the graph to run shortest path algorithms on
     * @param s the source vertex
     * @return an array of the graph vertices with parent set to NIL and
     *		   distance (key) set to infinity (graph independent vertices)
     */
    @SuppressWarnings("unchecked")
    private static <E extends Number, T extends ShortestPathVertex<E>,
            W extends GraphInterface<T>>
            void initializeSingleSource(W G, int s) {
        for (int i = 0; i < G.V(); i++) {
            ShortestPathVertex<E> v = G.getVertex(i);
            v.parent = ShortestPathVertex.NIL;
            v.setDistance(ShortestPathVertex.POSITIVE_INFINITY);
        }
        G.getVertex(s).setDistance(ShortestPathVertex.ZERO);
    }

    /**
     * Relax an edge.
     * If {@code v.key > u.key + w} set {@code v.key = u.key + w}
     * @param u the parent vertex
     * @param v the vertex
     * @param w the weight of the edge (u, v)
     */
    private static <E extends Number, T extends VertexInterface>
            void relax(ShortestPathVertex<E> u, ShortestPathVertex<E> v, E w) {
        if (!triangleInequality(u, v, w)) {
            v.setDistance(u.sumDistances(w));
            v.parent = u.getVertex();
        }
    }

    /**
     * Performs triangle inequality.
     * @param u the parent vertex
     * @param v the child vertex
     * @param w the weight of the edge (u, v)
     * @return true if {@code v.key <= u.key + w(u, v)}
     *		   false otherwise
     * @throws IllegalArgumentException if w is null
     */
    private static <E extends Number, T extends ShortestPathVertex<E>>
            boolean triangleInequality(ShortestPathVertex<E> u,
                    ShortestPathVertex<E> v, E w) {
        if (w == null) {
            throw new IllegalArgumentException("w cannont be null");
        }
        return v.compareTo(u.sumDistances(w)) <= 0;
    }

    /**
     * Unit tests.
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        //
        //        -2     5
        //       0<------> 1
        //      ^| \   -3^ ^
        //    6/ |  \   /  |
        //    /  |8  \ /   |
        //   / 2 |    x    |
        //  4\<--+---x \   | 7
        //    \  |  / \ \-4|
        //    7\ | /   \ \ |
        //      vv/     \ v|
        //      2--------->3
        //            9
        //
        ShortestPathVertex<Integer>[] vertices =
                (ShortestPathVertex<Integer>[]) new ShortestPathVertex[5];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new ShortestPathVertex<Integer>(i);
        }
        // WeightedDirectedGraph requires the vertices to be supplied as
        // an argument to the constructor. Due to Java generics limitations,
        // it is not possible to create instances of parameterized generic
        // classes in java dynamically.
        WeightedDirectedGraph<ShortestPathVertex<Integer>, Integer> G =
                new WeightedDirectedGraph<ShortestPathVertex<Integer>, 
                        Integer>(vertices);
        G.addEdge(0, 1, 5);
        G.addEdge(0, 2, 8);
        G.addEdge(0, 3, -4);
        G.addEdge(1, 0, -2);
        G.addEdge(2, 1, -3);
        G.addEdge(2, 3, 9);
        G.addEdge(3, 1, 7);
        G.addEdge(3, 4, 2);
        G.addEdge(4, 0, 6);
        G.addEdge(4, 2, 7);
        System.out.println(G);
        System.out.println("Testing Bellman-Ford: ");
        // Should results in:
        //
        //            -2      
        //      (2)0<------1(4)
        //          \     ^  
        //           \   /   
        //            \ /-3   
        //             x     
        // (0)4\      / \      
        //      \    /   \-4 
        //     7 \  /     \  
        //        v/       v 
        //        2(7)      3(-2)
        //            
        //
        if (ShortestPath.bellmanFordExtended(G, 4) == true) {
            System.out.println("No negative cycles detected.");
            for (ShortestPathVertex<Integer> v : G.getVertices()) {
                System.out.println(v);
            }
        } else {
            System.out.println("Failed: the graph contains negative cycles");
        }
    }
}
