public class PushRelabel <T extends VertexInterface, E extends Number> {
    private final Preflow<E> preflow;
    private final ResidualNetwork<E> residualNetwork;
    private final int s;
    private final int t;

    public PushRelabel(FlowNetwork<T, E> net, int s, int t) {
        PushRelabelVertex<E>[] vertices = createVerticesArray(net.V());
        this.preflow = new Preflow<E>(net);
        this.residualNetwork = new ResidualNetwork<E>(net);
        this.s = s;
        this.t = t;
    }

    public static <T extends VertexInterface, E extends Number>
            FlowNetwork<T, E> maxFlow(FlowNetwork<T, E> net, int s, int t) {
        PushRelabel<T, E> pr = new PushRelabel<T, E>(net, s, t);
        Preflow<E> results = pr.getMaxFlow();
        System.out.println(results);
        for (FlowNetworkEdge<PushRelabelVertex<E>, E> e : results.getEdges()) {
            int u = e.incidentFrom();
            int v = e.incidentTo();
            net.setFlow(u, v, e.getFlow());
        }
        return net;
    }

    @SuppressWarnings("unchecked")
    private PushRelabelVertex<E>[] createVerticesArray(int n) {
        PushRelabelVertex<E>[] vertices = 
                (PushRelabelVertex<E>[]) new PushRelabelVertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new PushRelabelVertex<E>(i);
        }
        return vertices;
    }

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
            if (!u.isOverflowing()) continue;
            int uInd = u.getVertex();
            boolean relabel = true;
            for (int vInd : preflow.neighbourVertices(u)) {
                PushRelabelVertex<E> v = preflow.getVertex(vInd);
                if (u.getHeight() != v.getHeight() + 1) continue;
                if (!residualNetwork.residualCapacity(uInd, vInd).isZero()) {
                    push(uInd, vInd);
                    Q.enqueue(v);
                    relabel = false;
                }
            }
            if (relabel) relabel(uInd);
            if (u.isOverflowing() && u.getVertex() != t) Q.enqueue(u);
            System.out.println(preflow);
        }
        return preflow;
    }

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
            }
        }
    }

    private void push(int u, int v) {
        // min(u.e, cf(u, v))
        NumericKey<E> df = preflow.getVertex(u).getExcessFlow();
        if (residualNetwork.residualCapacity(u, v).compareTo(df) == -1) {
            df = residualNetwork.residualCapacity(u, v);
        }
        if (preflow.hasEdge(u, v)) {
            FlowNetworkEdge<PushRelabelVertex<E>, E> e = preflow.findEdge(u, v);
            e.setFlow(e.getFlow().plus(df));
        } else {
            FlowNetworkEdge<PushRelabelVertex<E>, E> e = preflow.findEdge(v, u);
            e.setFlow(e.getFlow().minus(df));
        }
        residualNetwork.updateResidualCapacity(u, v, df);
        PushRelabelVertex<E> uv = preflow.getVertex(u);
        PushRelabelVertex<E> vv = preflow.getVertex(v);
        uv.setExcessFlow(uv.getExcessFlow().minus(df));
        vv.setExcessFlow(vv.getExcessFlow().plus(df));
        System.out.println("push: " + uv + " -> " + vv);
    }

    private void relabel(int u) {
        System.out.println("relabel: " + preflow.getVertex(u));
        long h = Long.MAX_VALUE;
        for (int v : preflow.neighbourVertices(u)) {
            if (preflow.getVertex(v).getHeight() < h) {
                h = preflow.getVertex(v).getHeight();
            }
        }
        preflow.getVertex(u).setHeight(1 + h);
    }
}
