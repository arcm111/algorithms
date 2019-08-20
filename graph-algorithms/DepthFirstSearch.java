/**
 * Depth-first-search, a graph searching algorithms which traverses a given
 * graph to obtain structural information.
 *
 * <p>It searches deeper into the graph whenever possible. It discovers edges
 * on the most recently discovered vertex v, once all edges of v explored, it
 * backtracks to explore edges of its ancesstor from which it was discovered.
 * <p>It works on directed and undirected graphs.
 * <p>If dfs discoveres a back edge during traversal, the graph is cyclic.
 * <p>Undirected graphs have no "Forward" and no "Cross" edges.
 * <p>While exploring the edge <em>(u, v)</em> the type of the edge is 
 * determined by the colour of <em>v</em>:
 * <ul>
 *	<li>WHITE: Tree edge.
 * 	<li>GREY:  Back edge.
 * 	<li>BLACK: Either Forward or Cross edge.
 * </ul>
 * <p>The running time is <em>O(V + E)</em>.
 */
public class DepthFirstSearch {
    // used to record discovery and fininsh times for every vertix.
    private static int time; 

    /**
     * Performs a DFS on the etirety of a given graph.
     * @param G the graph to perform DFS on.
     */
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

    /**
     * A recursive call for {@code process} which performs dfs on a
     * single connected component using a single source vertex.
     * @param G the graph to perform dfs on.
     * @param u the vertex to be used as a source.
     */
    private static <T extends DFSVertex, E extends GraphInterface<T>> 
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
     * Performs DFS on the entire graph non-recursively using {@code Stack}.
     * @param G the graph to perform DFS on.
     */
    public static <T extends DFSVertex, E extends GraphInterface<T>>
            void processNonRecursive(E G) {
        time = 0;
        for (T x : G.getVertices()) {
            if (x.colour == T.Colour.WHITE) {
                processNonRecursive(G, x);
            }
        }
    }

    /**
     * Performs DFS using a single source on a graph non-recursively 
     * using {@code Stack} for storing and processing vertices.
     * @param G the graph to perform DFS on.
     * @param s the source vertex.
     */
    public static <T extends DFSVertex, E extends GraphInterface<T>>
            void processNonRecursive(E G, T s) {
        Stack<T> stack = new Stack<>();
        stack.push(s);
        // each loop here discovers an entire connected component.
        while (!stack.isEmpty()) {
            T u = stack.pop();
            if (u.colour == T.Colour.WHITE) {
                time++;
                u.d = time;
                u.colour = T.Colour.GREY;
                stack.push(u);
                // revesing the order of the vertices in the
                // adjacency list of u to reproduce the results of
                // the above method {@code process} where we explore
                // the decendants of u in correct order. Here, the
                // order of decendant vertices is reversed after adding
                // them to the stack which is LIFO.
                for (T v : reverse(G.adj(u))) {
                    if (v.colour == T.Colour.WHITE) {
                        v.pi = u;
                        stack.push(v);
                    }
                }
            } else if (u.colour == T.Colour.GREY) {
                u.colour = T.Colour.BLACK;
                time++;
                u.f = time;
            }
        }
    }

    /** 
     * Reverses the order of vertices in the adjacency list.
     * @param itr the {@code LinkedList} iterator to reverse its elements.
     */
    private static <T extends DFSVertex> Iterable<T> reverse(Iterable<T> itr) {
        Stack<T> stack = new Stack<>();
        for (T item : itr) stack.push(item);
        return stack;
    }

    /**
     * Detects if a graph is cyclic or acyclic.
     * The graph is cyclic if it contains a back edge. In other words,
     * while exploring the edge (u, v), if v is grey, then the edge is
     * a back edge and the graph contains a cycle.
     * @param G the graph to be tested.
     */
    public static <T extends DFSVertex, E extends GraphInterface<T>>
            boolean isCyclic(E G) {
        for (T x : G.getVertices()) {
            if (x.colour == T.Colour.WHITE) {
                Stack<T> stack = new Stack<>();
                stack.push(x);
                while (!stack.isEmpty()) {
                    T u = stack.pop();
                    if (u.colour == T.Colour.WHITE) {
                        u.colour = T.Colour.GREY;
                        stack.push(u);
                        for (T v : G.adj(u)) {
                            if (v.colour == T.Colour.GREY) return true;
                            if (v.colour == T.Colour.WHITE) {
                                v.pi = u;
                                stack.push(v);
                            }
                        }
                    } else if (u.colour == T.Colour.GREY) {
                        u.colour = T.Colour.BLACK;
                    }
                }
            }
        }
        return false;
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

        G = new DirectedGraph<>(DFSVertex.class, V);
        G.addEdge(0, 1);
        G.addEdge(0, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 4);
        G.addEdge(2, 5);
        G.addEdge(3, 1);
        G.addEdge(4, 3);
        G.addEdge(5, 5);
        System.out.println("Before dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);
        DepthFirstSearch.processNonRecursive(G);
        System.out.println("After dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);

        G = new DirectedGraph<>(DFSVertex.class, V);
        G.addEdge(0, 1);
        G.addEdge(0, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 4);
        G.addEdge(2, 5);
        G.addEdge(3, 1);
        G.addEdge(4, 3);
        G.addEdge(5, 5);
        System.out.println(G.toString());
        System.out.print("The graph is: ");
        System.out.println(DepthFirstSearch.isCyclic(G) ? "cyclic" : "acyclic");

        // 0->1  2
        // | /| /|
        // vv vv v
        // 3<-4  5
        G = new DirectedGraph<>(DFSVertex.class, V);
        G.addEdge(0, 1);
        G.addEdge(0, 3);
        G.addEdge(1, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 4);
        G.addEdge(2, 5);
        G.addEdge(4, 3);
        System.out.println(G.toString());
        System.out.print("The graph is: ");
        System.out.println(DepthFirstSearch.isCyclic(G) ? "cyclic" : "acyclic");
    }
}
