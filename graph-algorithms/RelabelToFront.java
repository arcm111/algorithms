import java.util.Iterator;

/**
 * Relabel-To-Front algorithm for computing maximum flow.
 * <p>An optimized version of push-relabel algorithm, which acheives an 
 * asymptotically faster running time by maintaining a topologically sorted
 * list of vertices in the admissible-network with zero exccess-flow vertices
 * preceding other vertices in the list.
 * <p>It moves flow locally between adjacent vertices under the guidance of an
 * admissible-network (containing admissible edges) maintained by relabel 
 * operations.
 * <p>Contrary to push-relabel algorithm which randomly push or relabel 
 * overflowing vertices, relabel-to-front algorithm discharge excess-flow of 
 * each vertex until it has no more excess-flow left and then move it back to 
 * the beginning of the maintained list of vertices, preserving the topological 
 * sorting order of the list.
 * <p><em>Topological sorting of vertices in the admissible network</em> means 
 * that for each admissible edge (u,v) in the admissible network, vertex u 
 * appears before vertex v in the list.
 * <p>Push operations does not create new admissible edges.
 * <p>Relabel operation of u creates new admissible edges leaving u but does
 * not, however, create any new admissible edges entering u. Therefore, moving
 * relabeled vertices to the front of the list maintains the topological
 * sorting order because we know that for all other vertices in the list, 
 * there exits no vertex v such that (v,u) is an admissible edge, and therefore,
 * no vertex can precede u in the list.
 * <p>An edge (u,v) is <em>admissible</em> if it has a non-zero residual 
 * capacity and u.h = v.h + 1. If u is overflowing we push excess flow to v.
 * If u is overflowing but there are no admissible-edges incident on u, however,
 * we relabel u.
 * Running time <em>O(V^3)</em>.
 */
public class RelabelToFront <T extends VertexInterface, E extends Number>
       extends PushRelabel<T, E> {
    /**
     * Constructor.
     * @param net the flow network to compute the max flow for
     * @param s source vertex
     * @param t the sink vertex
     * @return a flow network with maximum flow
     */
    public RelabelToFront(FlowNetwork<T, E> net, int s, int t) {
        super(net, s, t);
    }

    /**
     * Run the algorithm.
     * For each overflowing vertex in the topologically sorted list of vertices
     * discharge the vertex and move it back to the front of the list.
     */
    @Override
    protected Preflow<E> getMaxFlow() {
        initializePreflow(preflow, s);
        System.out.println("preflow: ");
        System.out.println(preflow);
        // Create a topologically sorted list of vertices.
        // Initially all pairs of vertices have heights differece of |V| or 0,
        // therefore there are no admissible-edges and the list is sorted.
        DoubleLinkedList L = new DoubleLinkedList();
        for (int i = 0; i < n; i++) {
            L.addNode(preflow.getVertex(i));
        }
        Node uNode = L.head;
        while (uNode != null) {
            PushRelabelVertex<E> u = uNode.vertex;
            long oldHeight = u.getHeight();
            int uInd = u.getVertex();
            discharge(uInd);
            if (u.getHeight() > oldHeight) {
                L.moveToFront(uNode);
            }
            uNode = uNode.next;
        }
        return preflow;
    }

    /**
     * Pushes all excess flow of a vertex through admissible edges to 
     * neighbour vertices.
     * If the vertex is still overflowing but there are no admissible edges,
     * then it relabels it to create new admissible edges. Then it repeats
     * the process until the vertex has no more excess flow left.
     */
    private void discharge(int uInd) {
        PushRelabelVertex<E> u = preflow.getVertex(uInd);
        Iterator<Integer> iter = rnet.neighbours(uInd).iterator();
        System.out.println("u: " + u);
        while (u.hasExcess()) {
            if (!iter.hasNext()) {
                relabel(uInd);
                // reset position to first neighbour vertex
                iter = rnet.neighbours(uInd).iterator();
            } else {
                int vInd = iter.next();
                PushRelabelVertex<E> v = preflow.getVertex(vInd);
                NumericKey<E> rc = rnet.residualCapacity(uInd, vInd);
                System.out.println("v: " + v + " [" + rc + "]");
                // we do not need to test (u, v) residual capacity because
                // the residual network already does that for us and return
                // only neighbour vertices that share a non-saturated edge
                // with u
                if (u.getHeight() == v.getHeight() + 1) {
                    push(uInd, vInd);
                }
            }
            System.out.println(preflow);
        }
    }

    private class Node {
        public Node prev;
        public Node next;
        public PushRelabelVertex<E> vertex;
    }

    private class DoubleLinkedList {
        public Node head;
        private Node tail;

        public void addNode(PushRelabelVertex<E> v) {
            Node x = new Node();
            x.vertex = v;
            x.prev = tail;
            if (tail != null) {
                tail.next = x;
            }
            if (head == null) {
                this.head = x;
                this.tail = x;
            }
        }

        public void moveToFront(Node x) {
            x.prev.next = x.next;
            x.next.prev = x.prev;
            x.prev = null;
            x.next = head;
            this.head = x;
        }
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
        FlowNetwork<Vertex, Integer> f = RelabelToFront.maxFlow(fn, 0, 5);
        System.out.println(f);
    }
}
