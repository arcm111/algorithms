public class WeightedDirectedAcyclicGraph<T extends VertexInterface, 
        E extends Number> extends WeightedDirectedGraph<T, E> {
    public WeightedDirectedAcyclicGraph(Class<T> C, int V) {
        super(C, V);
    }

    public WeightedDirectedAcyclicGraph(T[] vertices) {
        super(vertices);
    }

    @Override
    public void addEdge(int u, int v, E w) {
        validateVertex(u);
        validateVertex(v);
        if (u == v) {
            throw new IllegalArgumentException("self loop error");
        }
        if (DepthFirstSearch.areConnected(this, v, u)) {
            String err = "adding edge (%d, %d) will create a cycle";
            throw new IllegalArgumentException(String.format(err, u, v));
        }
        adj[u].add(vertices[v]);
        WeightedVertex<T, E> x = new WeightedVertex<>(vertices[v], u);
        if (w == null) {
            x.setWeight(WeightedVertex.POSITIVE_INFINITY);
        } else {
            x.setWeight(w);
        }
        adjVertices[u].add(x);
        this.E++;
    }

    public static void main(String[] args) {
        // testing graph construction
        //
        //     3
        //  0---->1  
        //  |     |^  
        //  |     | \7
        // 5|    3|  2
        //  |     | /9
        //  V     VV
        //  4<----3
        //     2
        //
        Vertex[] vertices = new Vertex[5];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(i);
        }
        WeightedDirectedAcyclicGraph<Vertex, Integer> G = 
                new WeightedDirectedAcyclicGraph<>(vertices);
        G.addEdge(0, 1, 3);
        G.addEdge(0, 4, 5);
        G.addEdge(1, 3, 3);
        G.addEdge(2, 1, 7);
        G.addEdge(2, 3, 9);
        G.addEdge(3, 4, 2);
        System.out.println(G);

        // Testing cycles detection
        G.addEdge(4, 1, 4);
    }
}
