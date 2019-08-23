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
 *   <li>WHITE: Tree edge.
 *   <li>GREY:  Back edge.
 *   <li>BLACK: Either Forward or Cross edge.
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
        // reset properties of all vertices in the graph.
        for (DFSVertex v : G.getVertices()) {
            v.colour = DFSVertex.Colour.WHITE;
            v.pi = DFSVertex.NullVertex;
            v.d = DFSVertex.NIL;
            v.f = DFSVertex.NIL;
        }
        // perform dfs on non-visited vertices.
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
        // reset properties of all vertices in the graph.
        for (DFSVertex v : G.getVertices()) {
            v.colour = DFSVertex.Colour.WHITE;
            v.pi = DFSVertex.NullVertex;
            v.d = DFSVertex.NIL;
            v.f = DFSVertex.NIL;
        }
        // perform non-recursive dfs on non-visited vertices.
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

    public static <T extends VertexInterface, E extends GraphInterface<T>>
            DirectedGraph<DFSVertex> cloneGraph(E G) {
        DirectedGraph<DFSVertex> DG = 
            new DirectedGraph<>(DFSVertex.class, G.V());
        for (T u : G.getVertices()) {
            for (T v : G.adj(u)) DG.addEdge(u.getVertex(), v.getVertex());
        }
        return DG;
    }

    /**
     * Check if two vertices are connected within a graph.
     * <p>It makes a copy and converts the given graph into a 
     * {@code DirectedGraph} to avoid modifying the original graph.
     * <p>Running time for making a graph copy is <em>O(V + E)</em> worst
     * case and the running time to check if the vertices are connected is
     * also <em>O(V + E)</em> worst case. Therefore, the overall running
     * time is <em>O(V + E)</em> worst case.
     * @Return boolean whether there exists a path from x to y.
     */
    public static <T extends VertexInterface, E extends DirectedGraph<T>>
            boolean areConnected(E G, int x, int y) {
        DirectedGraph<DFSVertex> DG = cloneGraph(G);
        return areConnected(DG, DG.getVertex(x), DG.getVertex(y));
    }

    /**
     * Helper method for the above method {@code areConnected}.
     * <p>It checks if there is a path from the vertex x to vertex y only,
     * and not the other way around.
     * <p>Running time <em>O(V + E)</em> worst case.
     * @param G the graph to perform DFS on.
     * @param x the first vertex.
     * @param y the second vertex.
     */
    private static <T extends DFSVertex, E extends DirectedGraph<T>>
            boolean areConnected(E G, T x, T y) {
        Stack<T> stack = new Stack<>();
        stack.push(x);
        // each loop here discovers an entire connected component.
        while (!stack.isEmpty()) {
            T u = stack.pop();
            if (u.colour == T.Colour.WHITE) {
                u.colour = T.Colour.GREY;
                stack.push(u);
                for (T v : G.adj(u)) {
                    if (v.equals(y)) return true;
                    if (v.colour == T.Colour.WHITE) {
                        v.pi = u;
                        stack.push(v);
                    }
                }
            } else if (u.colour == T.Colour.GREY) {
                u.colour = T.Colour.BLACK;
            }
        }
        return false;
    }

    /**
     * Topologically "reversed" sorts a directed acyclic graph dag.
     * <p>It performs dfs on the graph and every time it finishes a 
     * vertex it adds that vertex to the front of a linked-list. The resulting
     * linked-list contains all the graph vertices in a reversed typologically
     * sorted order.
     * @param DAG the dag graph to sort.
     */
    public static <T extends VertexInterface> Iterable<T> 
            topologicalSort(DirectedAcyclicGraph<T> DAG) {
        LinkedList<T> sorted = new LinkedList<>();
        DirectedGraph<DFSVertex> G = cloneGraph(DAG);
        for (DFSVertex s : G.getVertices()) {
            if (s.colour == DFSVertex.Colour.WHITE) {
                Stack<DFSVertex> stack = new Stack<>();
                stack.push(s);
                while (!stack.isEmpty()) {
                    DFSVertex u = stack.pop();
                    if (u.colour == DFSVertex.Colour.WHITE) {
                        u.colour = DFSVertex.Colour.GREY;
                        stack.push(u);
                        for (DFSVertex v : G.adj(u)) {
                            if (v.colour == DFSVertex.Colour.WHITE) {
                                v.pi = u;
                                stack.push(v);
                            }
                        }
                    } else if (u.colour == DFSVertex.Colour.GREY) {
                        u.colour = DFSVertex.Colour.BLACK;
                        // once finished with a vertex add it to the
                        // front of the linked list.
                        sorted.add(DAG.getVertex(u.getVertex()));
                    }
                }
            }
        }
        return sorted;
    }

    /**
     * Unit test.
     */
    public static void main(String[] args) {
        // Testing dfs process (recursive).
        //
        // 0->1  2
        // | ^| /|
        // v/ vv v
        // 3<-4  5<>
        DirectedGraph<DFSVertex> DG = new DirectedGraph<>(DFSVertex.class, 6);
        DG.addEdge(0, 1);
        DG.addEdge(0, 3);
        DG.addEdge(1, 4);
        DG.addEdge(2, 4);
        DG.addEdge(2, 5);
        DG.addEdge(3, 1);
        DG.addEdge(4, 3);
        DG.addEdge(5, 5);

        DirectedGraph<DFSVertex> G = DG.copy();
        System.out.println(G.toString());
        System.out.println("Before dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);
        DepthFirstSearch.process(G);
        System.out.println("After dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);

        // Testing dfs processNonRecursive (non-recursive).
        G = DG.copy();
        System.out.println("Before dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);
        DepthFirstSearch.processNonRecursive(G);
        System.out.println("After dfs:");
        for (DFSVertex v : G.getVertices()) System.out.println(v);

        // Testing isCyclic with a cyclic graph.
        G = DG.copy();
        System.out.println(G.toString());
        System.out.print("The graph is: ");
        System.out.println(DepthFirstSearch.isCyclic(G) ? "cyclic" : "acyclic");
        
        // Testing isCyclic with acyclic graph.
        //
        // 0->1  2
        // | /| /|
        // vv vv v
        // 3<-4  5
        DG = new DirectedGraph<>(DFSVertex.class, 6);
        DG.addEdge(0, 1);
        DG.addEdge(0, 3);
        DG.addEdge(1, 3);
        DG.addEdge(1, 4);
        DG.addEdge(2, 4);
        DG.addEdge(2, 5);
        DG.addEdge(4, 3);

        G = DG.copy();
        System.out.println(G.toString());
        System.out.print("The graph is: ");
        System.out.println(DepthFirstSearch.isCyclic(G) ? "cyclic" : "acyclic");

        // Testing areConnected
        G = DG.copy();
        System.out.print("5-->2: ");
        System.out.println(DepthFirstSearch.areConnected(G, 5, 2) ? "connected"
            : "not connected");
        System.out.print("2-->4: ");
        System.out.println(DepthFirstSearch.areConnected(G, 2, 4) ? "connected"
            : "not connected");

        // Testing Topological sort.
        //
        // 0<--1   2-->3   4       [0 = shoes    5 = undershorts] 
        // ^^      |   |           [1 = socks    6 = pants      ]
        // | \     |   |           [2 = shirt    7 = belt       ]
        // |  \    v   v           [3 = tie      8 = jacket     ]
        // 5-->6-->7<--8           [4 = watch                   ]
        //
        // Dag from figure 22.7 p.613 in CLRS book.
        //
        // Produces (reversed):
        // 5-->6-->4-->2-->3-->8-->7-->1-->0
        // which corresponds to:
        //
        //      ___________________________
        //     | __________________        |
        //     ||                  V       v
        // 5-->6   4   2-->3-->8-->7   1-->0
        // |           |___________^       ^
        // |_______________________________|
        //
        // which is topologically sorted.
        System.out.println("Reversed topological sort: ");
        DirectedAcyclicGraph<DFSVertex> DAG = 
                new DirectedAcyclicGraph<>(DFSVertex.class, 9);
        DAG.addEdge(1, 0);
        DAG.addEdge(2, 3);
        DAG.addEdge(2, 7);
        DAG.addEdge(3, 8);
        DAG.addEdge(5, 6);
        DAG.addEdge(5, 0);
        DAG.addEdge(6, 0);
        DAG.addEdge(6, 7);
        DAG.addEdge(8, 7);
        int i = 0;
        for (DFSVertex v : topologicalSort(DAG)) {
            i++;
            System.out.print(v.getVertex());
            if (i < 9) System.out.print("-->");
        }
        System.out.println();
    }
}
