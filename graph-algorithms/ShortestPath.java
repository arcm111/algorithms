/**
 * Single-path shortest-path algorithms.
 */
public class ShortestPath {
    /**
     * Bellman-Ford algorithm.
     * <p>Single source shortest path algorithm.
     * <p>Allows negative weight edges but no negative weight cycles.
     * <p>Can detect negative weight cycles.
     * <p>Relaxes each edge V - 1 times.
     * <p>By the path-relaxation property, at the end of the algorithm, all
     * paths from the source s to all other vertices are shortest paths. This
     * is because for any path p(s, v0, v1, ..., vk, v) all edges in the path
     * ((s,v0), (v0,v1), ..., (vk,v)) would have been relaxed at least once
     * when the algorithm terminates.
     * <p>Running time is <em>O(EV)</em>.
     *
     * @param G the weighted directed graph
     * @param s the source vertex
     * @return null if the graph contians negative weight cycles
     *		   Otherwise, it returns the predecessor subgraph
     */
    public static <E extends Number, T extends ShortestPathVertex<E>>
            boolean BellmanFord(WeightedDirectedGraph<T, E> G, int s) {
        initializeSingleSource(G, s);
        for (int i = 0; i < G.V(); i++) {
            for (WeightedEdge<T, E> e : G.getEdges()) {
                T u = e.getSourceVertex();
                T v = e.getDstVertex();
                relax(u, v, e.getKey());
            }
        }
        for (WeightedEdge<T, E> e : G.getEdges()) {
            T u = e.getSourceVertex();
            T v = e.getDstVertex();
            if (!triangleInequality(u, v, e.getKey())) {
                // negative weight cycle detected
                return false;
            }
        }
        return true;
    }

    /**
     * DAG-Shortest-Path Single source Shortest Path algorithm.
     * <p>Works only on directed weighted acyclic graphs which (being acyclic)
     * is a requirement in itself for topological sorting used in the algorithm.
     * <p>Negative edges are allowed but no positive or negative cycles.
     * <p>Relaxes each edge only once.
     * <p>Running time <em>O(V + E)</em>
     *
     * @param G the graph to perform the algorithm on
     * @param s source vertex to use to find shortest paths to other vertices
     */
    public static <E extends Number, T extends ShortestPathVertex<E>>
            void DAGShortestPath(WeightedDirectedAcyclicGraph<T, E> G, 
                    int s) {
        initializeSingleSource(G, s);
        for (T u : DepthFirstSearch.topologicalSort(G)) {
            for (WeightedVertex<T, E> w : G.adjEdges(u)) {
                T v = w.vertex;
                relax(u, v, w.getKey());
            }
        }
    }

    /**
     * Dijkstra algorithm.
     * <p>Maintains a set S of vertices whom shortest-paths from the source 
     * have already been determined. The algorithm repeatedly selects a vertex
     * not in S and with minimum shortest-path estimate, adds it to S, then
     * relaxes all its outgoing edges.
     * <p>Negative edges are not allowed.
     * <p>Uses a proiority queue for selecting vertices with minimum weights.
     * <p>Running time depends on the queue implementation:
     *    - using a simple array = <em>O(V^2 + E)</em>
     *    - using minimum-heap priority-queue = <em>O(ElogV)</em>
     *    - using Fibonacci-heap = <em>O(VlgV + E)</em>
     *
     * @param G the graph to perform the algorithm on
     * @param s source vertex to use to find shortest paths to other vertices
     */
    public static <E extends Number, T extends ShortestPathVertex<E>> 
                void Dijkstra(WeightedDirectedGraph<T, E> G, int s) {
        initializeSingleSource(G, s);
        MinPriorityQueue<T> Q = new MinPriorityQueue<>(G.getVertices(), G.V());
        while (!Q.isEmpty()) {
            T u = Q.extractMin();
            for (WeightedVertex<T, E> w : G.adjEdges(u)) {
                T v = w.vertex;
                relax(u, v, w.getKey());
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
    private static <E extends Number, 
            W extends GraphInterface<? extends ShortestPathVertex<E>>>
                    void initializeSingleSource(W G, int s) {
        for (int i = 0; i < G.V(); i++) {
            ShortestPathVertex<E> v = G.getVertex(i);
            v.setParent(ShortestPathVertex.NIL);
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
    private static <E extends Number>
            void relax(ShortestPathVertex<E> u, ShortestPathVertex<E> v, 
                    NumericKey<E> w) {
        // if (u.getKey().infinity == ShortestPathVertex.POSITIVE_INFINITY) {
        //	return;
        //}
        if (!triangleInequality(u, v, w)) {
            v.setDistance(u.getKey().plus(w));
            v.setParent(u.getVertex());
        }
    }

    /**
     * Tests triangle inequality.
     * @param u the parent vertex
     * @param v the child vertex
     * @param w the weight of the edge (u, v)
     * @return true if {@code v.key <= u.key + w(u, v)}
     *		   false otherwise
     * @throws IllegalArgumentException if w is null
     */
    private static <E extends Number>
            boolean triangleInequality(ShortestPathVertex<E> u,
                    ShortestPathVertex<E> v, NumericKey<E> w) {
        if (w.infinity == NumericKey.ZERO) {
            return v.getKey().compareTo(u.getKey()) <= 0;
        } else if (w.infinity != NumericKey.NONE) {
            throw new IllegalArgumentException("weight cannont be null");
        }
        return v.getKey().compareTo(u.getKey().plus(w)) <= 0;
    }

    /**
     * Unit tests.
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // Testing Bellman-Ford
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
        // results:
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

        // Testing DAG-Shortest-Path
        if (ShortestPath.BellmanFord(G, 4) == true) {
            System.out.println("No negative cycles detected.");
            for (ShortestPathVertex<Integer> v : G.getVertices()) {
                System.out.println(v);
            }
        } else {
            System.out.println("Failed: the graph contains negative cycles");
        }

        System.out.println("Testing DAG-Shortest-Path: ");
        ShortestPathVertex<Integer>[] arr = 
                (ShortestPathVertex<Integer>[]) new ShortestPathVertex[6];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new ShortestPathVertex<Integer>(i);
        }
        WeightedDirectedAcyclicGraph<ShortestPathVertex<Integer>, Integer> DAG =
                new WeightedDirectedAcyclicGraph<>(arr);
        DAG.addEdge(0, 1, 5);
        DAG.addEdge(0, 2, 3);
        DAG.addEdge(1, 2, 2);
        DAG.addEdge(1, 3, 6);
        DAG.addEdge(2, 3, 7);
        DAG.addEdge(2, 4, 4);
        DAG.addEdge(2, 5, 2);
        DAG.addEdge(3, 4, -1);
        DAG.addEdge(3, 5, 1);
        DAG.addEdge(4, 5, -2);
        System.out.println(DAG);
        DAGShortestPath(DAG, 1);
        for (ShortestPathVertex<Integer> v : DAG.getVertices()) {
            System.out.println(v);
        }

        // Testing Dijkstra
        //
        //           1
        //       0------->1
        //      ^^       ^^
        //   10/ |3    9/ |6
        //    /  |     /  |
        //   / 7 |    /   |
        //  4\<--+---x    |  
        //    \  |  / \   |
        //    5\ |2/   \  |4
        //      vv/     \ v
        //      2-------->3
        //           2
        //
        System.out.println("Testing Dijkstra:");
        G = new WeightedDirectedGraph<ShortestPathVertex<Integer>, 
                Integer>(vertices);
        G.addEdge(0, 1, 1);
        G.addEdge(0, 2, 2);
        G.addEdge(1, 3, 4);
        G.addEdge(2, 0, 3);
        G.addEdge(2, 1, 9);
        G.addEdge(2, 3, 2);
        G.addEdge(3, 1, 6);
        G.addEdge(3, 4, 7);
        G.addEdge(4, 0, 10);
        G.addEdge(4, 2, 5);
        System.out.println(G);
        Dijkstra(G, 4);
        for (ShortestPathVertex<Integer> v : G.getVertices()) {
            System.out.println(v);
        }
        // results:
        //
        //        (8)  1   (9)
        //         0------->1
        //         ^         
        //         |          
        //         |         
        //         |3        
        // (0)4\   |           
        //      \  |         
        //      5\ |          
        //        v|         
        //        2-------->3
        //       (5)   2   (7)
        //
    }
}
