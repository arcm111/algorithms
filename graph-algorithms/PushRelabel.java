/**
 * Push-relabel algorithm for computing maximum flow.
 * Preserves capacity constraint throughout the execution but not the 
 * flow conservation. It uses a preflow function which satisfies the capacity
 * constraint but allows the in flow to a vertex to exceeds its outflow.
 */
public class PushRelabel <T extends VertexInterface, E extends Number> {
    private final Preflow<E> preflow; // preflow function
    private final ResidualNetwork<E> rnet; // residual network
    private final int s; // source
    private final int t; // sink

    /**
     * Constructor.
     * @param net the flow network to compute the max flow for
     * @param s source vertex
     * @param t the sink vertex
     * @return a flow network with maximum flow
     */
    public PushRelabel(FlowNetwork<T, E> net, int s, int t) {
        PushRelabelVertex<E>[] vertices = createVerticesArray(net.V());
        this.preflow = new Preflow<E>(net);
        this.rnet = new ResidualNetwork<E>(net);
        this.s = s;
        this.t = t;
    }

    /**
     * compute maximum flow (staic).
     * @param net the flow network to compute the max flow for
     * @param s source vertex
     * @param t the sink vertex
     * @return a flow network with maximum flow
     */
    public static <T extends VertexInterface, E extends Number>
            FlowNetwork<T, E> maxFlow(FlowNetwork<T, E> net, int s, int t) {
        PushRelabel<T, E> pr = new PushRelabel<T, E>(net, s, t);
        Preflow<E> results = pr.getMaxFlow();
        System.out.println(results);
        // copy flow values from preflow to original network
        for (FlowNetworkEdge<PushRelabelVertex<E>, E> e : results.getEdges()) {
            net.setFlow(e.incidentFrom(), e.incidentTo(), e.getFlow());
        }
        return net;
    }

    /**
     * creats a {@code PushRelabelVertex} array.
     * @param n size of the array
     * @return vertex array
     */
    @SuppressWarnings("unchecked")
    private PushRelabelVertex<E>[] createVerticesArray(int n) {
        PushRelabelVertex<E>[] vertices = 
                (PushRelabelVertex<E>[]) new PushRelabelVertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new PushRelabelVertex<E>(i);
        }
        return vertices;
    }

    /**
     * Run the algorithm.
     * For each overflowing vertex either perform a push or a relabel operation,
     * repeat until all vertices except the source and the sink have no excess
     * flow remainint.
     */
    private Preflow<E> getMaxFlow() {
        initializePreflow(preflow, s);
        System.out.println("preflow: ");
        System.out.println(preflow);
        Queue<PushRelabelVertex<E>> Q = new Queue<>();
        for (int v : preflow.neighbourVertices(s)) {
            Q.enqueue(preflow.getVertex(v));
        }
        while (!Q.isEmpty()) {
            PushRelabelVertex<E> u = Q.dequeue();
            System.out.println("u: " + u);
            if (!isOverflowing(u)) continue;
            int uInd = u.getVertex();
            boolean relabel = true;
            for (int vInd : rnet.neighbours(uInd)) {
                PushRelabelVertex<E> v = preflow.getVertex(vInd);
                NumericKey<E> rc = rnet.residualCapacity(uInd, vInd);
                System.out.println("v: " + v + " [" + rc + "]");
                // we get neighbour vertices from residual network to 
                // execlude satuarted edges that has no residual capacity
                if (u.getHeight() > v.getHeight()) {
                    // if we found a vertex v with a lower height than u then
                    // we don't have to relabel u
                    relabel = false;
                }
                if (u.getHeight() == v.getHeight() + 1) {
                    push(uInd, vInd);
                    Q.enqueue(v);
                }
            }
            if (relabel) relabel(uInd);
            // if vertex u still have excess flow we push it back to the que
            if (isOverflowing(u)) Q.enqueue(u);
            System.out.println(preflow);
        }
        return preflow;
    }

    /**
     * Initializes preflow.
     * Sets all edges' flow to zero and all vertices' heights and excess
     * flow to zero. then it sets the height of source to |V| and sink to 0,
     * saturates all edges connected to source, and push excess flow equal to
     * these edges capacities to the vertices adjacent to source.
     * Also it's important to update the residual network's edges incident on 
     * source so that we can push back the remaining excess flow back to source
     * after obtaining maximum flow.
     */
    private void initializePreflow(Preflow<E> preflow, int s) {
        for (int i = 0; i < preflow.V(); i++) {
            PushRelabelVertex<E> v = preflow.getVertex(i);
            v.setHeight(0);
            v.setExcessFlow(new NumericKey<E>(NumericKey.ZERO));
        }
        for (FlowNetworkEdge<PushRelabelVertex<E>, E> e : preflow.getEdges()) {
            e.setFlow(new NumericKey<E>(NumericKey.ZERO));
        }
        preflow.getVertex(s).setHeight(preflow.V());
        for (int v = 0; v < preflow.V(); v++) {
            if (preflow.hasEdge(s, v)) {
                FlowNetworkEdge<PushRelabelVertex<E>, E> e = 
                        preflow.findEdge(s, v);
                NumericKey<E> ec = e.getCapacity();
                e.setFlow(ec);
                e.destinationVertex().setExcessFlow(ec);
                NumericKey<E> sc = e.sourceVertex().getExcessFlow().minus(ec);
                e.sourceVertex().setExcessFlow(sc);
                rnet.updateResidualCapacity(s, v, ec);
            }
        }
    }

    /**
     * push excess flow from one vertex to a another adjacent vertex.
     * If excess flow is less than the residual capacity of edge (u, v), 
     * it performs a saturating push. Otherwise, it performs a non-saturating 
     * push (by pushing only part of the excess flow equals to the residual 
     * capacity).
     * Applies if the edge (u, v) still has residual capacity and the height
     * of u is larger than the height of v by exactly 1. (if an vertex x in 
     * preflow has height larger than and adjacent vertex y by more than 1, 
     * the edge (x, y) does not exist in the residual network.
     * We push excess flow only downhill from a higher vertex to a lower vertex.
     *
     * @param u vertex to push excess flow from
     * @param v vertex to push excess flow to
     */
    private void push(int u, int v) {
        // min(u.e, cf(u, v))
        NumericKey<E> df = preflow.getVertex(u).getExcessFlow();
        if (rnet.residualCapacity(u, v).compareTo(df) == -1) {
            df = rnet.residualCapacity(u, v);
        }
        if (preflow.hasEdge(u, v)) {
            FlowNetworkEdge<PushRelabelVertex<E>, E> e = preflow.findEdge(u, v);
            e.setFlow(e.getFlow().plus(df));
        } else {
            FlowNetworkEdge<PushRelabelVertex<E>, E> e = preflow.findEdge(v, u);
            e.setFlow(e.getFlow().minus(df));
        }
        rnet.updateResidualCapacity(u, v, df);
        PushRelabelVertex<E> uv = preflow.getVertex(u);
        PushRelabelVertex<E> vv = preflow.getVertex(v);
        System.out.println("push: " + uv + " -> " + vv);
        uv.setExcessFlow(uv.getExcessFlow().minus(df));
        vv.setExcessFlow(vv.getExcessFlow().plus(df));
    }

    /**
     * Relabel a vertex (increase its height).
     * Sets the height of u to the minimum height of its adjacent vertices
     * puls one.
     * Applies if the height of u is less or equal to the height of each
     * of its adjacent vertices.
     * Source and sink never change their heights from |V| and 0 respectively.
     * @param u vertex to relabel
     */
    private void relabel(int u) {
        System.out.println("relabel: " + preflow.getVertex(u));
        long h = Long.MAX_VALUE;
        for (int v : rnet.neighbours(u)) {
            if (preflow.getVertex(v).getHeight() < h) {
                h = preflow.getVertex(v).getHeight();
            }
        }
        preflow.getVertex(u).setHeight(1 + h);
    }

    /**
     * Check if a vertex has excess flow.
     * Source and sink vertices never overflow by definition.
     */
    private boolean isOverflowing(PushRelabelVertex<E> v) {
        if (v.getVertex() == t) return false;
        return v.hasExcess();
    }
}
