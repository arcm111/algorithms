public class AllPairsShortestPath {
    private static <E extends Number, T extends ShortestPathVertex<E>>
            NumericKey<E>[][] extendShortestPaths(NumericKey<E>[][] L,
                    NumericKey<E>[][] W) {
        int n = W[0].length;
        NumericKey<E>[][] Lprime = newMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Lprime[i][j].setKey(NumericKey.POSITIVE_INFINITY);
                for (int k = 0; k < n; k++) {
                    if (Lprime[i][j].compareTo(L[i][k].plus(W[k][j])) == 1) {
                        Lprime[i][j] = L[i][k].plus(W[k][j]);
                    }
                }
            }
        }
        return Lprime;
    }

    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> NumericKey<E>[][] 
                    slowAllPairsShortestPaths(W G) {
        NumericKey<E>[][] W = getAdjacencyMatrix(G);
        NumericKey<E>[][] L = W;
        for (int i = 1; i < G.V() - 1; i++) {
            L = extendShortestPaths(L, W);
        }
        return L;
    }

    public static <E extends Number, T extends VertexInterface, 
            W extends WeightedDirectedGraph<T, E>> NumericKey<E>[][] 
                    getAdjacencyMatrix(W G) {
        NumericKey<E>[][] adjMatrix = newMatrix(G.V());
        for (int i = 0; i < G.V(); i++) {
            adjMatrix[i][i].setKey(NumericKey.ZERO);
            for (WeightedVertex<T, E> w : G.adjEdges(i)) {
                int j = w.getVertex();
                adjMatrix[i][j].setKey(w.getWeight());
            }
        }
        return adjMatrix;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Number> NumericKey<E>[][] newMatrix(int n) {
        NumericKey<E>[][] matrix = (NumericKey<E>[][]) new NumericKey[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = (NumericKey<E>[]) new NumericKey[n];
            for (int j = 0; j < n; j++) {
                matrix[i][j] = new NumericKey(NumericKey.POSITIVE_INFINITY);
            }
        }
        return matrix;
    }

    private static <E extends Number> 
            String toString(NumericKey<E>[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            builder.append("source[" + i + "]: \n");
            for (int j = 0; j < matrix[0].length; j++) {
                builder.append("  ->[" + j + "]: " + matrix[i][j] + "\n");
            }
        }
        return builder.toString();
    }

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
        NumericKey<Integer>[][] results = slowAllPairsShortestPaths(G);
        System.out.println(toString(results));
    }
}
