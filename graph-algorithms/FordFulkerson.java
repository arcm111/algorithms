/**
 * Ford-Fulkerson method.
 * It is a method rather than algorithm because the way of determining the
 * augmenting paths is not specified.
 * Keeps augmenting flow along an augmenting path until we can't find any more 
 * augmenting paths from source to sink. When there are no more augmenting
 * paths available, the flow in the netword have reached its maximum value
 * according to min-cut max-flow theorem.
 * If capacity values are irrational then the algorithm might not terminate.
 */
public abstract class FordFulkerson {
    /**
     * Runs Ford-Fulkerson method.
     * @param fn the flow network
     * @param s the source
     * @param t the sink
     * @return a flow network with maximum flow
     */
    public <T extends VertexInterface, E extends Number> 
            FlowNetwork<T, E> maxFlow(FlowNetwork<T, E> fn, int s, int t) {
        System.out.println("Flow network: ");
        System.out.println(fn);
        ResidualNetwork<E> rn = new ResidualNetwork<>(fn);
        System.out.println("Residual network: ");
        System.out.println(rn);
        FlowNetworkPath<E> p = findAugmentingPath(rn, s, t);
        while (p != null) {
            System.out.println("Path: " + p);
            // find residual capacity of augmenting path p
            NumericKey<E> cp = new NumericKey<>(NumericKey.POSITIVE_INFINITY);
            // find the critical edge on p which capacity is the lowest
            for (FlowNetworkPath.Edge<E> e : p.getEdges()) {
                if (e.getCapacity().compareTo(cp) == -1) {
                    cp = e.getCapacity();
                }
            }
            System.out.println("path residual capacity: " + cp);
            // augment flow along p by cp
            for (FlowNetworkPath.Edge<E> x : p.getEdges()) {
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
            // find another augmenting path
            p = findAugmentingPath(rn, s, t);
        }
        return fn;
    }

    /**
     * Finds and augmenting path in the residual network.
     * Requires implementaion.
     * "abstract" "static" modifiers are not allowed together in java.
     * @param net the residual network
     * @param s the source
     * @param t the sink
     * @return an augmenting path from s to t
     */
    abstract <E extends Number> FlowNetworkPath<E> 
            findAugmentingPath(ResidualNetwork<E> net, int s, int t);
}
