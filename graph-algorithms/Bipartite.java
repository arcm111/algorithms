/**
 * Finds out if a given graph is bipartite or not.
 * Works on both directed and undirected graphs.
 * <p><em>bipartite graph</em> is a graph whose vertices can be partitioned into
 * two disjoint independent(no two vertices in a set are adjacent) sets and
 * all edges in the graph goes between them.
 * <p>If the graph is not bipartite then it has an odd-length cycle, the 
 * opposite is also true.
 * <p>It can detect odd cycles.
 * <p>Running time is <em>O(E + V)</em> because it uses dfs.
 */
public class Bipartite {
    public static enum BipartiteColour {
        RED,
        BLUE,
        NONE
    }
    private final BipartiteVertex[] vertices;
    private final LinkedList<Integer> leftPartition; // red vertices
    private final LinkedList<Integer> rightPartition; // blue vertices
    private final boolean bipartite;
    private LinkedList<Integer> oddCycle;

    /**
     * Constructor.
     * It takes both directed and undirected graphs as an argument but converts
     * them to an undirected graph so that we can run the colouring algorithm
     * @param G the input graph to be tested as bipartite or not
     */
    @SuppressWarnings("unchecked")
    public <T extends VertexInterface, W extends GraphInterface<T>> 
            Bipartite(W G) {
        this.leftPartition = new LinkedList<Integer>();
        this.rightPartition = new LinkedList<Integer>();
        // create bipartite vertices array
        this.vertices = new BipartiteVertex[G.V()];
        for (int i = 0; i < G.V(); i++) {
            vertices[i] = new BipartiteVertex(i);
        }
        // create an undirected-graph copy from the original graph
        Graph<BipartiteVertex> G2 = new Graph<>(vertices);
        for (int i = 0; i < G.V(); i++) {
            T u = G.getVertex(i);
            for (T v : G.adj(u)) {
                G2.addEdge(u.getVertex(), v.getVertex());
            }
        }
        // run dfs to colour vertices
        if (colourGraph(G2)) {
            createPartitions(G2);
            bipartite = true;
        } else {
            bipartite = false;
        }
    }

    /**
     * Sort the graph vertices into two disjoint and independent sets depending
     * on their colour.
     * @param G the graph after running the 2-colouring algorithm on it
     * @throws IllegalStateException if it finds a non-coloured vertex
     */
    private void createPartitions(Graph<BipartiteVertex> G) {
        for (BipartiteVertex v : G.getVertices()) {
            if (v.bcolour == BipartiteColour.RED) {
                leftPartition.add(v.getVertex());
            } else if (v.bcolour == BipartiteColour.BLUE) {
                rightPartition.add(v.getVertex());
            } else {
                throw new IllegalStateException("uncoloured vertex found");
            }
        }
    }

    /**
     * Colour the graph vertices into either red or blue using dfs.
     * If it finds two adjacent vertices with the same colour then the graph
     * is not bipartite. In that case it also finds an odd-length cycle.
     * @param G the graph to run the algorithm on
     */
    protected boolean colourGraph(Graph<BipartiteVertex> G) {
        for (int i = 0; i < G.V(); i++) {
            BipartiteVertex s = G.getVertex(i);
            if (s.colour == BipartiteVertex.Colour.WHITE) {
                if (s.bcolour == BipartiteColour.NONE) {
                    s.bcolour = BipartiteColour.RED;
                }
                if (dfs(G, s) == false) return false;
            }
        }
        return true;
    }

    /**
     * Helper method for {@code colourGraph}.
     * Runs dfs to colour the vertices.
     * @param G the graph
     * @param s the source vertex
     * @return true if the graph is bipartite, otherwise, false
     */
    private boolean dfs(Graph<BipartiteVertex> G, BipartiteVertex s) {
        Stack<BipartiteVertex> stack = new Stack<>();
        stack.push(s);
        while (!stack.isEmpty()) {
            BipartiteVertex u = stack.pop();
            int uInd = u.getVertex();
            if (u.colour == BipartiteVertex.Colour.WHITE) {
                u.colour = BipartiteVertex.Colour.GREY;
                stack.push(u);
                for (BipartiteVertex v : G.adj(u)) {
                    if (v.colour == BipartiteVertex.Colour.WHITE) {
                        v.bcolour = oppositeColour(u.bcolour);
                        v.pi = u;
                        stack.push(v);
                    } else if (v.bcolour == u.bcolour) {
                        this.oddCycle = traceCycle(u, v);
                        return false;
                    }
                }
            } else if (u.colour == BipartiteVertex.Colour.GREY) {
                u.colour = BipartiteVertex.Colour.BLACK;
            }
        }
        return true;
    }

    /**
     * Generates an odd-length cycle bases on same-coloured adjacent vertices
     * found during the execution of the colouring algorithm.
     * @param u the parent vertex
     * @param v the child vertex
     */
    private LinkedList<Integer> 
            traceCycle(BipartiteVertex u, BipartiteVertex v) {
        LinkedList<Integer> cycle = new LinkedList<>();
        cycle.add(v.getVertex());
        for (BipartiteVertex z = u; z != null && !z.equals(v); z = z.pi) {
            cycle.add(z.getVertex());
        }
        return cycle;
    }

    /**
     * Returns the opposite colour of a given bipartite colour.
     * @param c the colour to get its opposite
     * @throws IllegalArgumentException if the colour is NONE
     * @return the opposite colour
     */
    private BipartiteColour oppositeColour(BipartiteColour c) {
        if (c == BipartiteColour.NONE) {
            throw new IllegalArgumentException("Invalid colour");
        }
        if (c == BipartiteColour.RED) return BipartiteColour.BLUE;
        return BipartiteColour.RED;
    }

    public Iterable<Integer> leftPartition() {
        return leftPartition;
    }

    public Iterable<Integer> rightPartition() {
        return rightPartition;
    }

    public LinkedList<Integer> getOddCycle() {
        return oddCycle;
    }

    public boolean isBipartite() {
        return bipartite;
    }

    public boolean isRed(int v) {
        return vertices[v].bcolour == BipartiteColour.RED;
    }

    public boolean isBlue(int v) {
        return vertices[v].bcolour == BipartiteColour.BLUE;
    }

    /**
     * Helper class.
     * Implements DFSVertex as the base class.
     * Adds an extra property; the bipartite colour.
     */
    private class BipartiteVertex extends DFSVertex {
        private BipartiteColour bcolour;
        private BipartiteVertex pi;

        public BipartiteVertex(Integer v) {
            super(v);
            this.colour = BipartiteVertex.Colour.WHITE;
            this.bcolour = BipartiteColour.NONE;
        }

        public boolean equals(BipartiteVertex that) {
            return this.getVertex() == that.getVertex();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("v:[" + getVertex() + "], ");
            if (pi != null) {
                builder.append("pi:[" + pi.getVertex() + "], ");
            }
            switch(colour) {
                case GREY:
                    builder.append("dfs-colour:(GREY), ");
                    break;
                case BLACK:
                    builder.append("dfs-colour:(BLACK), ");
                    break;
                case WHITE:
                    builder.append("dfs-colour:(WHITE), ");
                    break;
            }
            switch(bcolour) {
                case RED:
                    builder.append("b-colour:(RED)");
                break;
                case BLUE:
                    builder.append("b-colour:(BLUE)");
                break;
                case NONE:
                    builder.append("b-colour:(NONE)");
                break;
            }
            return builder.toString();
        }
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // Testing bipartite undirected graph
        // Graph in Figure 26.8 (a) p.733
        System.out.println("Testing on a bipartite graph:");
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
        Bipartite bp = new Bipartite(G);
        if (bp.isBipartite()) {
            System.out.println("Graph is bipartite.");
            System.out.print("L: ");
            for (int i : bp.leftPartition()) System.out.print(i + " ");
            System.out.println();
            System.out.print("R: ");
            for (int i : bp.rightPartition()) System.out.print(i + " ");
            System.out.println();
        } else {
            System.out.println("Graph is not bipartite!");
            System.out.println(bp.getOddCycle());
        }
        System.out.println();

        // Testing non-bipartite undirected graph
        //
        //        0
        //       / \
        //      1   2
        //      \   /
        //       3-4
        //
        System.out.println("Testing on a non-bipartite graph:");
        vertices = new Vertex[5];
        for (int i = 0; i < 5; i++) vertices[i] = new Vertex(i);
        G = new Graph<>(vertices);
        G.addEdge(0, 1);
        G.addEdge(0, 2);
        G.addEdge(1, 3);
        G.addEdge(2, 4);
        G.addEdge(3, 4);
        System.out.println(G);
        bp = new Bipartite(G);
        if (bp.isBipartite()) {
            System.out.println("Graph is bipartite.");
            System.out.print("L: ");
            for (int i : bp.leftPartition()) System.out.print(i + " ");
            System.out.println();
            System.out.print("R: ");
            for (int i : bp.rightPartition()) System.out.print(i + " ");
            System.out.println();
        } else {
            System.out.println("Graph is not bipartite!");
            System.out.println(bp.getOddCycle());
        }
        System.out.println();

        // Testing bipartite directed graph
        // same as the first undirected graph but with edges going one way only
        System.out.println("Testing on a bipartite directed graph:");
        vertices = new Vertex[9];
        for (int i = 0; i < 9; i++) vertices[i] = new Vertex(i);
        DirectedGraph<Vertex> DG = new DirectedGraph<>(vertices);
        DG.addEdge(0, 1);
        DG.addEdge(2, 1);
        DG.addEdge(2, 5);
        DG.addEdge(4, 3);
        DG.addEdge(4, 5);
        DG.addEdge(4, 7);
        DG.addEdge(6, 5);
        DG.addEdge(8, 5);
        System.out.println(DG);
        bp = new Bipartite(DG);
        if (bp.isBipartite()) {
            System.out.println("Graph is bipartite.");
            System.out.print("L: ");
            for (int i : bp.leftPartition()) System.out.print(i + " ");
            System.out.println();
            System.out.print("R: ");
            for (int i : bp.rightPartition()) System.out.print(i + " ");
            System.out.println();
        } else {
            System.out.println("Graph is not bipartite!");
            System.out.println(bp.getOddCycle());
        }
        System.out.println();
    }
}
