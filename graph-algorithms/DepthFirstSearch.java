public class DepthFirstSearch {
    private static int time;

    public static <T extends DFSVertex, E extends GraphInterface<T>> 
            void process(E G) {
        // vertices colour and parent(pi) are set to white and nil by default.
        time = 0;
        for (T u : G.getVertices()) {
            if (u.colour == T.Colour.WHITE) {
                visit(G, u);
            }
        }
    }

    public static <T extends DFSVertex, E extends GraphInterface<T>> 
            void visit(E G, T u) {
        time++;
        u.d = time;
        u.colour = T.Colour.GREY;
        for (T v : G.adj(u)) {
            if (v.colour == T.Colour.WHITE) {
                v.pi = u;
                visit(G, v);
            }
        }
        u.colour = T.Colour.BLACK;
        time++;
        u.f = time;
    }

    /**
     * Unit test.
     */
    public static void main(String[] args) {
        // 0->1  2
        // | ^| /|
        // v/ vv v
        // 3<-4  5<>
        int V = 6;
        DirectedGraph<DFSVertex> G = new DirectedGraph<>(DFSVertex.class, V);
        G.addEdge(0, 1);
        G.addEdge(0, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 4);
        G.addEdge(2, 5);
        G.addEdge(3, 1);
        G.addEdge(4, 3);
        G.addEdge(5, 5);
        System.out.println(G.toString());
        System.out.println("Before dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);
        DepthFirstSearch.process(G);
        System.out.println("After dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);
    }
}
