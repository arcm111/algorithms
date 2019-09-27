public class WeightedDirectedAcyclicGraph<T extends VertexInterface, 
        E extends Number> extends DirectedGraph<T> {
    private LinkedList<WeightedVertex<T, E>>[] adjVertices; // out-degree edges

    public WeightedDirectedAcyclicGraph(Class<T> C, int V) {
        super(C, V);
        initAdjVertices(V);
    }

    public WeightedDirectedAcyclicGraph(T[] vertices) {
        super(vertices);
        initAdjVertices(V);
    }

    @SuppressWarnings("unchecked")
    private void initAdjVertices(int n) {
        this.adjVertices = 
                (LinkedList<WeightedVertex<T, E>>[]) new LinkedList[n];
        for (int i = 0; i < n; i++) {
            adjVertices[i] = new LinkedList<>();
        }
    }
    
    @Override
    public void addEdge(int u, int v) {
        addEdge(u, v, null);
    }

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
            x.setKey(WeightedVertex.POSITIVE_INFINITY);
        } else {
            x.setKey(w);
        }
        adjVertices[u].add(x);
        this.E++;
    }

    public Iterable<WeightedVertex<T, E>> adjEdges(T u) {
        return adjVertices[u.getVertex()];
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
