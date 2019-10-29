public class MaximumFlow {
    public static <T extends VertexInterface, E extends Number> 
            FlowNetwork<T, E>
                    EdmondsKarp(FlowNetwork<T, E> net, int s, int t) {
        FordFulkerson implementation = new FordFulkerson() {
            @Override
            protected <T extends VertexInterface, E extends Number>
                    FlowNetworkPath<T, E> findAugmentingPath(
                            FlowNetwork<T, E> net, int s, int t) {
                BFSVertex[] bfsVertices = new BFSVertex[net.V()];
                for (int i = 0; i < net.V(); i++) {
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
                    int ind = u.getVertex();
                    for (FlowNetworkEdge<T, E> e : net.neighbours(ind)) {
                        BFSVertex v;
                        if (e.getCapacity().compareTo(new NumericKey<E>(NumericKey.ZERO)) == 0) {
                            System.out.println(e);
                            throw new IllegalArgumentException();
                        }
                        if (e.incidentFrom() == ind) { // outdegree edge
                            v = bfsVertices[e.incidentTo()];
                        } else { // indegree edge
                            v = bfsVertices[e.incidentFrom()];
                        }
                        if (v.colour == BFSVertex.Colour.WHITE) {
                            v.colour = BFSVertex.Colour.GREY;
                            v.pi = u;
                            // if we reached the sink return the path
                            if (v.getVertex() == t) {
                                Stack<FlowNetworkEdge<T, E>> stack = 
                                        new Stack<>();
                                BFSVertex cur = v;
                                while (cur.getVertex() != s) {
                                    stack.push(net.findEdge(cur.pi.getVertex(),
                                            cur.getVertex()));
                                    cur = cur.pi;
                                }
                                FlowNetworkPath<T, E> path =
                                        new FlowNetworkPath<T, E>();
                                for (FlowNetworkEdge<T, E> x : stack) {
                                    path.addEdge(x);
                                }
                                return path;
                            }
                            Q.enqueue(v);
                        }
                    }
                    u.colour = BFSVertex.Colour.BLACK;
                }
                return null;
            }
        };
        return implementation.maxFlow(net, s, t);
    }

    /**
     * Ford-Fulkerson method.
     * It is a method rather than algorithm because the way of determining the
     * augmenting paths is not specified.
     */
    public abstract static class FordFulkerson {
        /**
         * Dertemines the maximum acheivable flow in a flow network.
         * @param fn the flow network
         * @param s the source
         * @param t the sink
         * @return a flow network with maximum flow
         */
        public <T extends VertexInterface, E extends Number> FlowNetwork<T, E>
                maxFlow(FlowNetwork<T, E> fn, int s, int t) {
            System.out.println("Flow network: ");
            System.out.println(fn);
            FlowNetwork<T, E> rn = fn.residualNetwork();
            System.out.println("Residual network: ");
            System.out.println(rn);
            FlowNetworkPath<T, E> p = findAugmentingPath(rn, s, t);
            while (p != null) {
                System.out.println("Path: " + p);
                NumericKey<E> cp = 
                        new NumericKey<>(NumericKey.POSITIVE_INFINITY);
                for (FlowNetworkEdge<T, E> e : p.getEdges()) {
                    if (e.getCapacity().compareTo(cp) == -1) {
                        cp = e.getCapacity();
                    }
                }
                System.out.println("path residual capacity: " + cp);
                for (FlowNetworkEdge<T, E> x : p.getEdges()) {
                    int u = x.incidentFrom();
                    int v = x.incidentTo();
                    if (fn.hasEdge(u, v)) {
                        FlowNetworkEdge<T, E> e = fn.findEdge(u, v);
                        e.setFlow(e.getFlow().plus(cp));
                        rn.updateResidualCapacity(u, v, cp);
                    } else {
                        FlowNetworkEdge<T, E> e = fn.findEdge(v, u);
                        e.setFlow(e.getFlow().minus(cp));
                        rn.updateResidualCapacity(v, u, cp);
                    }
                }
                System.out.println(fn);
                System.out.println(rn);
                p = findAugmentingPath(rn, s, t);
            }
            return fn;
        }

        /**
         * Finds and augmenting path in the residual network.
         * Requires implementaion.
         * @param net the residual network
         * @param s the source
         * @param t the sink
         * @return an augmenting path from s to t
         */
        protected abstract <T extends VertexInterface, E extends Number>
                FlowNetworkPath<T, E> 
                        findAugmentingPath(FlowNetwork<T, E> net, int s, int t);
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        Vertex[] vertices = new Vertex[6];
        for (int i = 0; i < 6; i++) vertices[i] = new Vertex(i);
        FlowNetwork<Vertex, Integer> fn = new FlowNetwork<>(vertices);
        fn.addEdge(0, 1, 16);
        fn.addEdge(0, 2, 13);
        fn.addEdge(1, 3, 12);
        fn.addEdge(2, 1, 4);
        fn.addEdge(2, 4, 14);
        fn.addEdge(3, 2, 9);
        fn.addEdge(3, 5, 20);
        fn.addEdge(4, 3, 7);
        fn.addEdge(4, 5, 4);
        System.out.println(fn);
        FlowNetwork<Vertex, Integer> maxFlow = EdmondsKarp(fn, 0, 5);
        System.out.println(maxFlow);
    }
}
