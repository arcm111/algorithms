public class EdmondsKarp extends FordFulkerson {
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
        return null;
    }

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
