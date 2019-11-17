/**
 * Algorithm for finding maximum bipartite matching.
 * <p>It finds a maximum matching in a given bipartite graph using an
 * implementation of Ford-Fulkerson method, which runs on a flow-network 
 * based on the bipartite graph called <em>corresponding flow network</em> toc 
 * finds a maximum flow in the network, the value of the maximum flow is equal
 * to the cardinality of a maximum matching.
 * <p>A <em>matching</em> is a subset of graph edges such that for each vertex 
 * in the graph, <b>at most</b> one edge in the matching is incident on it;
 * no two edges in a matching connect to same vertex.
 * <p>A <em>maximum matching</em> is a matching of maximum cardinality; it
 * contains the largest number of edges among other matchings.
 * <p>A vertex is <em>matched</em> if there is an edge in the matching incident
 * on it, otherwise, it is <em>unmatched</em>.
 * <p>Running time is <em>O(EV)</em>.
 */
public class MaximumBipartiteMatching {
    /**
     * Runs the main algorithm and finds a maximum matching.
     * @param G the bipartite graph
     * @return a set of all edges in a maximum matching
     * @throws IllegalArgumentException if the given graph is not bipartite
     */
    public static <T extends VertexInterface, W extends GraphInterface<T>> 
            LinkedList<FlowNetworkEdge<Vertex, Integer>> 
                    findMaximumMatching(W G) {
        Bipartite bp = new Bipartite(G);
        if (!bp.isBipartite()) {
            throw new IllegalArgumentException("G is not bipartite");
        }
        int s = G.V();
        int t = G.V() + 1;
        FlowNetwork<Vertex, Integer> f = correspondingFlowNetwork(G, bp, s, t);
        FlowNetwork<Vertex, Integer> maxFlow = MaximumFlow.edmondsKarp(f, s, t);
        LinkedList<FlowNetworkEdge<Vertex, Integer>> maximumMatching = 
                new LinkedList<>();
        for (int uInd : bp.leftPartition()) {
            for (FlowNetworkEdge<Vertex, Integer> e : maxFlow.outEdges(uInd)) {
                if (e.hasFlow()) {
                    maximumMatching.add(e);
                }
            }
        }
        return maximumMatching;
    }

    /**
     * Creates a corresponding-flow-network from a bipartite graph.
     * It contains all vertices in the graph as well as two additional source
     * and sink vertices with indexes equal to |V| and |V|+1 respectively.
     * It has all the edges in the graph but directed from the left-partition
     * to the right-partition, in addition, it add new edges from the source
     * to each vertex in the left-partition and from all vertices in the
     * right-partition to the sink. All these edges have a unit weight capacity.
     *
     * @param G the bipartite graph
     * @param bp the bipartition of the graph
     * @param s the source vertex
     * @param t the sink vertex
     * @return the corresponding-flow-network
     */
    private static <T extends VertexInterface, W extends GraphInterface<T>> 
            FlowNetwork<Vertex, Integer> 
                    correspondingFlowNetwork(W G, Bipartite bp, int s, int t) {
        int n = G.V() + 2;
        Vertex[] vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i);
        }
        // copy only the edges incident on two different coloured vertices
        // from the bipartite graph
        FlowNetwork<Vertex, Integer> fnet = new FlowNetwork<>(vertices);
        for (int uInd : bp.leftPartition()) {
            T u = G.getVertex(uInd);
            for (T v : G.adj(u)) {
                int vInd = v.getVertex();
                if (bp.isBlue(vInd)) {
                    fnet.addEdge(uInd, vInd, 1);
                }
            }
        }
        // add new edges from source to L partition
        for (int v : bp.leftPartition()) {
            fnet.addEdge(s, v, 1);
        }
        // add new edges from R partition to t
        for (int v : bp.rightPartition()) {
            fnet.addEdge(v, t, 1);
        }
        return fnet;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        System.out.println("Testing maximum-bipartite-matching:");
        Vertex[] vertices = new Vertex[9];
        for (int i = 0; i < 9; i++) vertices[i] = new Vertex(i);
        Graph<Vertex> G = new Graph<>(vertices);
        G.addEdge(0, 1);
        G.addEdge(2, 1);
        G.addEdge(2, 5);
        G.addEdge(4, 3);
        G.addEdge(4, 5);
        G.addEdge(4, 7);
        G.addEdge(6, 5);
        G.addEdge(8, 5);
        System.out.println(G);
        LinkedList<FlowNetworkEdge<Vertex, Integer>> maxMatching = 
                MaximumBipartiteMatching.findMaximumMatching(G);
        System.out.println("|M|: " + maxMatching.size());
        for (FlowNetworkEdge<Vertex, Integer> e : maxMatching) {
            System.out.println(e);
        }
    }
}
