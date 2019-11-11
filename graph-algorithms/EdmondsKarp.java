/**
 * Edmonds-Karp algorithm.
 * An implementation of Ford-Fulkerson method using BFS for finding augmenting 
 * paths in the residual-network. The augmenting paths are the shortest paths
 * from source to sink in the residual-network where edges have unit weight.
 * Running time <em>O(VE^2)</em>
 */
public class EdmondsKarp extends FordFulkerson {
    /**
     * Finds an augmenting path in residual-network using BFS.
     * @param rnet the residual-network
     * @param s source vertex
     * @param t sink vertex
     * @return an augmenting path or null if nothing was found
     */
    @Override
    protected <E extends Number> FlowNetworkPath<E> 
            findAugmentingPath(ResidualNetwork<E> rnet, int s, int t) {
        BFSVertex[] bfsVertices = new BFSVertex[rnet.V()];
        for (int i = 0; i < rnet.V(); i++) {
            BFSVertex x = new BFSVertex(i);
            x.colour = BFSVertex.Colour.WHITE;
            x.pi = BFSVertex.NullVertex;
            bfsVertices[i] = x;
        }
        BFSVertex source = bfsVertices[s];
        source.colour = BFSVertex.Colour.GREY;
        Queue<BFSVertex> Q = new Queue<>();
        Q.enqueue(source);
        while (!Q.isEmpty()) {
            BFSVertex u = Q.dequeue();
            int uInd = u.getVertex();
            for (int vInd: rnet.neighbours(uInd)) {
                BFSVertex v = bfsVertices[vInd];
                if (v.colour == BFSVertex.Colour.WHITE) {
                    v.colour = BFSVertex.Colour.GREY;
                    v.pi = u;
                    // if we reached the sink return the path
                    if (v.getVertex() == t) {
                        return getPath(rnet, v, s);
                    }
                    Q.enqueue(v);
                }
            }
            u.colour = BFSVertex.Colour.BLACK;
        }
        // no augmenting paths were found
        return null;
    }

    /**
     * Constructs a path using the tail vertex and its predecessors.
     * The tail vertex is the sink.
     * @param rn the residual-network where the path exists
     * @param x the tail vertex of the path
     * @param s the source vertex which is the head of the path
     * @return the constructed path
     * @throws IllegalArgumentException if source vertex is not on the path
     */
    private static <E extends Number> FlowNetworkPath<E> 
            getPath(ResidualNetwork<E> rn, BFSVertex x, int s) {
        Stack<FlowNetworkPath.Edge<E>> stack = new Stack<>();
        for (BFSVertex cur = x; cur.getVertex() != s; cur = cur.pi) {
            // if we reached a vertex with null parent, it means
            // the source vertex s does not exist in the path
            if (cur.getVertex() == BFSVertex.NIL) {
                throw new IllegalArgumentException();
            }
            int u = cur.pi.getVertex();
            int v = cur.getVertex();
            NumericKey<E> rc = rn.residualCapacity(u, v);
            stack.push(new FlowNetworkPath.Edge<E>(u, v, rc));
        }
        FlowNetworkPath<E> path = new FlowNetworkPath<>();
        for (FlowNetworkPath.Edge<E> z : stack) {
            path.addEdge(z);
        }
        return path;
    }
}
