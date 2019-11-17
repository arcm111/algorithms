/**
 * Computes shortest-paths between every two pairs of vertices in a given
 * weighted directed graph.
 * Methodically, for each vertex in the graph as a source vertex, compute
 * shortest-paths from this vertex to all other vertices.
 * <p>Uses adjacency matrices to store shortest paths.
 */
public class AllPairsShortestPath {
    /**
     * Slow algorithm.
     * <p>The basic idea of computing shortest-paths here is to expand shortest-
     * paths one edge at a time until we obtain optimal shortest paths.
     * That is starting with sortest-paths from i to j containing one edges 
     * (i,j), then adding an extra edge at a time until all shortest-paths 
     * are computed using |V| - 1 edges (a simple path between any two vertices 
     * in a graph contains at most |V| vertices). The expansion occurs during 
     * the relaxation process.
     * <p>It works simillarly to running Bellman-Ford |V| times in parallel.
     * <p>Bellman-ford starts with a signle shortest path and expands it one 
     * edge at a time |V| times until the shortest-path is optimal. This 
     * algorithm, however, uses list of source vertices containing all vertices 
     * in the graph, it expands all shortest-paths from the first source by only
     * one edge, then moves to next source in the list and repeat the process, 
     * until all shortest-paths from every source in the list have been expanded
     * by exactly one edge. Then it moves back to the beginning of the list
     * and repeats the process until it finds optimum shortest-paths from every
     * source to every other vertex in the graph. Source:
     * {@link https://cs.stackexchange.com/questions/115521/}.
     * <p>The running time is <em>O(V^4)</em>.
     *
     * @param G the graph to run the algorithm on
     * @return all-pairs shortest-paths as a matrix (two dimensional array)
     */
    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> ShortestPathVertex<E>[][] 
                    slowAllPairsShortestPaths(W G) {
        ShortestPathVertex<E>[][] W = getAdjacencyMatrix(G);
        ShortestPathVertex<E>[][] L = W;
        for (int i = 1; i < G.V() - 1; i++) {
            L = extendShortestPaths(L, W);
        }
        return L;
    }

    /**
     * Faster algorithm, but still slow.
     * <p>Exploits a property in shortest-paths expansion's process similar
     * to matrix multiplication. Realizing that {@code extendShortestPaths}
     * operation is multiplying the weights matrix W by itself (with different
     * operators; using [+] instead of x and [min] instead of +) |V| - 1 times.
     * We can acheive the same results, however, by repeatedly squaring W 
     * matrix ceiling(lg(|V| - 1) times. 
     * Considering that expanding shortest-paths more that |V| - 1 does not 
     * have an effect because we already reached optimal solutions, the extra 
     * multiplication of W -that may result from the impreceice number of
     * required squaring due to the ceiling operation- will not affect the 
     * final results.
     * <p>The running time is <em>O(V^3 lgV)</em>.
     *
     * @param G the graph to run the algorithm on
     * @return all-pairs shortest-paths as a matrix (two dimensional array)
     */
    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> ShortestPathVertex<E>[][] 
                    fasterAllPairsShortestPaths(W G) {
        int n = G.V();
        ShortestPathVertex<E>[][] W = getAdjacencyMatrix(G);
        ShortestPathVertex<E>[][] L = W;
        int m = 1;
        while (m < n - 1) {
            L = extendShortestPaths(L, L);
            m = 2 * m;
        }
        return L;
    }

    /**
     * Helper method for {@code slowAllPairsShortestPaths} and 
     * {@code fasterAllPairsShortestPaths} algorithms.
     * Extends shortest-paths already computed one more edge. It also
     * computes predecessor-subgraph.
     *
     * @param L the matrix containing shortest-paths using m-1 edges
     * @param W the edges' weights matrix
     * @return shortest-paths matrix using m edges
     */
    private static <E extends Number> ShortestPathVertex<E>[][] 
                    extendShortestPaths(ShortestPathVertex<E>[][] L,
                            ShortestPathVertex<E>[][] W) {
        int n = W[0].length;
        ShortestPathVertex<E>[][] Lprime = newMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Lprime[i][j].setDistance(ShortestPathVertex.POSITIVE_INFINITY);
                Lprime[i][j].setParent(ShortestPathVertex.NIL);
                for (int k = 0; k < n; k++) {
                    NumericKey<E> sum = L[i][k].plus(W[k][j]);
                    if (Lprime[i][j].compareTo(sum) >= 0) {
                        Lprime[i][j].setDistance(sum);
                        if (i == j) { // predecessor of same vertex is itself
                            Lprime[i][j].setParent(i);
                        } else if (k == j) { // path(ik)=path(ij); copy pred
                            Lprime[i][j].setParent(L[i][k].getParent());
                        } else { // path(ij) relaxed; copy path(kj) predecessor
                            Lprime[i][j].setParent(L[k][j].getParent());
                        }
                    }
                }
            }
        }
        return Lprime;
    }

    /**
     * Floyd-Warshall algorithm.
     * <p>Instead of expanding shortest-paths edge by edge, we compute shortest-
     * path(i,j) containing (1,2,...,k) intermediate vertices by splitting it 
     * into two paths: path(i,k) and path(k,j) containing {1,2,...,k-1}
     * intermediate vertices. The shortest-paths (i,k) and (k,j) has already
     * been computed and memoized in previous steps, therefore, we can use
     * these values to expand the shortest path(i,j) by relaxation.
     * <p>The running time is <em>O(V^3)</em>.
     *
     * @param G the graph to run the algorithm on
     * @return all-pairs shortest-paths as a matrix (two dimensional array)
     */
    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> ShortestPathVertex<E>[][] 
                    FloydWarshall(W G) {
        int n = G.V();
        ShortestPathVertex<E>[][] W = getAdjacencyMatrix(G);
        ShortestPathVertex<E>[][] D = W;
        for (int k = 0; k < n; k++) {
            ShortestPathVertex<E>[][] Dprime = newMatrix(n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (D[i][j].compareTo(D[i][k].plus(D[k][j])) >= 0) {
                        Dprime[i][j].setDistance(D[i][k].plus(D[k][j]));
                        if (i == j) {
                            Dprime[i][j].setParent(i);
                        } else if (k == j) {
                            Dprime[i][j].setParent(D[i][k].getParent());
                        } else {
                            Dprime[i][j].setParent(D[k][j].getParent());
                        }
                    } else {
                        Dprime[i][j].setDistance(D[i][j].getKey());
                        Dprime[i][j].setParent(D[i][j].getParent());
                    }
                }
            }
            D = Dprime;
        }
        return D;
    }

    /**
     * Johnson's algorithm.
     * <p>Reweights all negative edges into non-negative weighted edges using
     * a weighting function that preserves shortest-paths; that is a
     * shortest-path (i,j) using original weighting functions is also a
     * shortest-path using the new reweighting function. Then it performs
     * Dijkstra's algorithm for every source vertex in the graph.
     * Reweight function: new-weight(u,v) = old-weight(u,v) + 
     * old-shortest-path(u) - old-shortest-path(v)
     * <p>Reweighting function (produces non-negative weights):
     *     w'(u,v) = w(u,v) + h(u) - h(v)
     *     where h(x) is the sortest path to x found using bellman-ford
     * <p>The running time depends on the queue implementation in Dijkstra's
     * algorithm:
     *     - <em>O(VE lgV)</em> using priority queue.
     *     - <em>O(V^2 lgV + EV)</em> using a Fibonacci heap.
     *
     * @param G the graph to run the algorithm on
     * @return all-pairs shortest-paths as a matrix (two dimensional array)
     */
    @SuppressWarnings("unchecked")
    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> 
                    ShortestPathVertex<E>[][] Johnson(W G) {
        // create new weighted-directed-graph to run Bellman-Ford on
        ShortestPathVertex<E>[] vertices = // safe unchecked cast
                (ShortestPathVertex<E>[]) new ShortestPathVertex[G.V() + 1];
        for (int i = 0; i < G.V() + 1; i++) {
            vertices[i] = new ShortestPathVertex<E>(i);
        }
        WeightedDirectedGraph<ShortestPathVertex<E>, E> Gprime =
                new WeightedDirectedGraph<>(vertices);
        // copy all vertices and edges from the original graph.
        // add additional extra source vertex s = 0 and shift other vertices
        for (WeightedEdge<T, E> e : G.getEdges()) {
            int u = e.incidentFrom() + 1;
            int v = e.incidentTo() + 1;
            Gprime.addEdge(u, v, e.getWeight());
        }
        // add edges of wight 0 from s to all other vertices
        for (int i = 1; i < G.V() + 1; i++) {
            Gprime.addEdge(0, i, new NumericKey<E>(ShortestPathVertex.ZERO));
        }
        // run Bellman-Ford and throw exception if negative cycles
        if (!ShortestPath.BellmanFord(Gprime, 0)) {
            throw new IllegalArgumentException("negative cycle detected");
        }
        ShortestPathVertex<E>[][] D = newMatrix(G.V());
        // store shortest path calculated with Bellman-Ford
        NumericKey<E>[] h = // safe unchecked cast
                (NumericKey<E>[]) new NumericKey[G.V()];
        // create another weighted-directed-graph to run Dijkstra on
        ShortestPathVertex<E>[] WDGVertices = // safe unchecked cast
                (ShortestPathVertex<E>[]) new ShortestPathVertex[G.V()];
        for (int i = 1; i < Gprime.V(); i++) {
            h[i - 1] = Gprime.getVertex(i).getKey();
            WDGVertices[i - 1] = new ShortestPathVertex<E>(i - 1);
        }
        WeightedDirectedGraph<ShortestPathVertex<E>, E> WDG = 
                new WeightedDirectedGraph<>(WDGVertices);
        // copy vertices and edges from original graph to the new graph and
        // replace old weights with new ones using the reweighting function.
        for (WeightedEdge<T, E> e : G.getEdges()) {
            int u = e.incidentFrom();
            int v = e.incidentTo();
            NumericKey<E> w = new NumericKey<E>(e.getWeight());
            NumericKey<E> weight = w.plus(h[u]).minus(h[v]);
            WDG.addEdge(u, v, weight);
        }
        // run Dijkstra on the new graph for every source vertex in the graph
        // and store shortest-paths values in D matrix
        for (int u = 0; u < G.V(); u++) {
            ShortestPath.Dijkstra(WDG, u);
            for (int v = 0; v < WDG.V(); v++) {
                ShortestPathVertex<E> w = new ShortestPathVertex(v);
                w.setParent(WDG.getVertex(v).getParent());
                w.setDistance(WDG.getVertex(v).getKey().plus(h[v].minus(h[u])));
                D[u][v] = w;
            }
            // reset WDG graph
            for (ShortestPathVertex<E> v : WDG.getVertices()) {
                v.setParent(ShortestPathVertex.NIL);
                v.setDistance(ShortestPathVertex.POSITIVE_INFINITY);
            }
        }
        return D;
    }

    /**
     * Transitive closure.
     * <p>For each pair of vertices in a graph G determine if there exits a path
     * between these two vertices.
     * <p>It uses Floyd-Warshal but replaces arithmetic operators (+) and (min)
     * for logical operators (and) and (or) respectively.
     * <p>Running time is <em>O(V^3)</em>
     *
     * @param the graph to run the algorithm on
     * @return boolean matrix containing transitive closure values
     */
    public static <E extends VertexInterface, W extends DirectedGraph<E>> 
                    boolean[][] transitiveClosure(W G) {
        int n = G.V();
        boolean[][] T = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            T[i][i] = true;
            for (E v : G.adj(i)) {
                int j = v.getVertex();
                T[i][j] = true;
            }
        }
        for (int k = 0; k < n; k++) {
            boolean[][] Tk = new boolean[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Tk[i][j] = T[i][j] || (T[i][k] && T[k][j]);
                }
            }
            T = Tk;
        }
        return T;
    }


    /**
     * Retuns the weights of a graph edges as a weight matrix.
     * It also can store predecessors as satellite-data.
     * @param G the graph
     * @return the weight matrix
     */
    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> ShortestPathVertex<E>[][] 
                    getAdjacencyMatrix(W G) {
        ShortestPathVertex<E>[][] adjMatrix = newMatrix(G.V());
        for (int i = 0; i < G.V(); i++) {
            adjMatrix[i][i].setDistance(ShortestPathVertex.ZERO);
            adjMatrix[i][i].setParent(i);
            for (WeightedVertex<T, E> w : G.adjEdges(i)) {
                int j = w.getVertex();
                adjMatrix[i][j].setDistance(w.getWeight());
                adjMatrix[i][j].setParent(i);
            }
        }
        return adjMatrix;
    }

    /**
     * creates a new nxn empth matrix.
     * @param n the size of the square matrix
     * @return an empty matrix
     */
    @SuppressWarnings("unchecked")
    private static <E extends Number> ShortestPathVertex<E>[][] 
            newMatrix(int n) {
        ShortestPathVertex<E>[][] matrix = 
                (ShortestPathVertex<E>[][]) new ShortestPathVertex[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = (ShortestPathVertex<E>[]) new ShortestPathVertex[n];
            for (int j = 0; j < n; j++) {
                matrix[i][j] = new ShortestPathVertex<>(j);
                matrix[i][j].setParent(ShortestPathVertex.NIL);
                matrix[i][j].setDistance(ShortestPathVertex.POSITIVE_INFINITY);
            }
        }
        return matrix;
    }

    /**
     * Returns a string representation of a matrix.
     * @param matrix the matrix
     * @return string representation
     */
    private static <E extends Number> 
            String toString(ShortestPathVertex<E>[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            builder.append("source[" + i + "]: \n");
            for (int j = 0; j < matrix[0].length; j++) {
                builder.append("  ->[" + j + "]: " + matrix[i][j] + "\n");
            }
        }
        return builder.toString();
    }

    /**
     * Unit tests.
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // Testing slowAllPairsShortestPaths: 
        //
        //              1
        //            ^/ \^
        //     ______//   \\______
        //   3/      /     \      \4
        //   /     7/   8   \1     \
        //  0------x---------x----->2
        //   \^___/___________\    ^
        //  -4\  /      2     \\  /-5
        //     VV              \V/
        //     4--------------->3
        //              6
        //
        WeightedDirectedGraph<Vertex, Integer> G = 
                new WeightedDirectedGraph<>(Vertex.class, 5);
        G.addEdge(0, 1, 3);
        G.addEdge(0, 2, 8);
        G.addEdge(0, 4, -4);
        G.addEdge(1, 3, 1);
        G.addEdge(1, 4, 7);
        G.addEdge(2, 1, 4);
        G.addEdge(3, 0, 2);
        G.addEdge(3, 2, -5);
        G.addEdge(4, 3, 6);
        System.out.println(G);
        System.out.println("Adjacency matrix:");
        System.out.println(toString(getAdjacencyMatrix(G)));
        System.out.println("Testing Slow-All-Pairs-Shortest-Paths:");
        ShortestPathVertex<Integer>[][] res1 = slowAllPairsShortestPaths(G);
        System.out.println(toString(res1));
        System.out.println("Testing Faster-All-Pairs-Shortest-Paths:");
        ShortestPathVertex<Integer>[][] res2 = fasterAllPairsShortestPaths(G);
        System.out.println(toString(res2));
        System.out.println("Testing Floyd-Warshal:");
        ShortestPathVertex<Integer>[][] res3 = FloydWarshall(G);
        System.out.println(toString(res3));
        System.out.println("Testing Johnson:");
        ShortestPathVertex<Integer>[][] res4 = Johnson(G);
        System.out.println(toString(res4));

        // Testing Transitive Closure:
        //
        //  0    1
        //  ^   /^
        //  |  / |
        //  | /  |
        //  |V   V
        //  3--->2
        //
        System.out.println("Testing Transitive-Closure:");
        DirectedGraph<Vertex> DG = new DirectedGraph<>(Vertex.class, 4);
        DG.addEdge(1, 2);
        DG.addEdge(1, 3);
        DG.addEdge(2, 1);
        DG.addEdge(3, 0);
        DG.addEdge(3, 2);
        System.out.println(DG);
        boolean[][] tc = transitiveClosure(DG);
        for (int i = 0; i < tc.length; i++) {
            for (int j = 0; j < tc[0].length; j++) {
                if (tc[i][j]) System.out.print("1 ");
                else System.out.print("0 ");
            }
            System.out.println();
        }
    }
}
