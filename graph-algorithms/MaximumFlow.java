/**
 * Finds maximum flow from source to sink in a given flow network.
 */
public class MaximumFlow {
    /**
     * Edmonds-Karp algorithm.
     * Running time <em>O(VE^2)</em>
     * @param net flow network to find maximum flow for
     * @param s source
     * @param t sink
     * @return the flow network with maximum flow
     */
    public static <T extends VertexInterface, E extends Number> 
            FlowNetwork<T, E>
                    edmondsKarp(FlowNetwork<T, E> net, int s, int t) {
        EdmondsKarp ek = new EdmondsKarp();
        return ek.maxFlow(net, s, t);
    }

    /**
     * Push-Relabel algorithm.
     * Running time <em>O(V^2 E)</em>
     * @param net flow network to find maximum flow for
     * @param s source
     * @param t sink
     * @return the flow network with maximum flow
     */
    public static <T extends VertexInterface, E extends Number>
            FlowNetwork<T, E>
                    pushRelabel(FlowNetwork<T, E> net, int s, int t) {
        return PushRelabel.maxFlow(net, s, t);
    }

    /**
     * Relabel-to-Front algorithm.
     * Running time <em>O(V^3)</em>
     * @param net flow network to find maximum flow for
     * @param s source
     * @param t sink
     * @return the flow network with maximum flow
     */
    public static <T extends VertexInterface, E extends Number>
            FlowNetwork<T, E>
                    relabelToFront(FlowNetwork<T, E> net, int s, int t) {
        return RelabelToFront.maxFlow(net, s, t);
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
        FlowNetwork<Vertex, Integer> f = MaximumFlow.edmondsKarp(fn, 0, 5);
        System.out.println(f);

        System.out.println("Testing push-relabel: ");
        f = MaximumFlow.pushRelabel(fn, 0, 5);
        System.out.println(f);

        System.out.println("Testing relabel-to-front: ");
        f = MaximumFlow.relabelToFront(fn, 0, 5);
        System.out.println(f);
    }
}
