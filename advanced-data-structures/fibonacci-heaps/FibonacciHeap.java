import java.util.ArrayList;
import java.util.List;

public class FibonacciHeap<T extends Comparable<T>> {
    private static final boolean debug = false;
    public int n;
    public Node<T> min;

    public FibonacciHeap() {
        this.n = 0;
        this.min = null;
    }

    private int maxDegree(int n) {
        return (int) Math.floor(Math.log(n) / Math.log(2));
    }

    private static class Node<T extends Comparable<T>> {
        public T key;
        public int degree = 0;
        public Node<T> p = null;
        public Node<T> child = null;
        public Node<T> right = null;
        public Node<T> left = null;
        public boolean mark = false;
        public boolean isNegativeInfinity = false;

        public Node(T key) {
            this.key = key;
        }

        public boolean lessThan(Node<T> that) {
           if (that == null) throw new IllegalArgumentException("null arg");
           if (this.isNegativeInfinity) return true;
           return this.key.compareTo(that.key) < 0;
        }
        
        public boolean moreThan(Node<T> that) {
           if (that == null) throw new IllegalArgumentException("null arg");
           if (this.isNegativeInfinity) return false;
           return this.key.compareTo(that.key) > 0;
        }
        
        public boolean equals(Node<T> that) {
            if (that == null) return false;
            if (that == this) return true;
            return this.key.equals(that.key);
        }

        @Override
        public String toString() {
            //println("x = " + key);
            StringBuilder builder = new StringBuilder();
            builder.append("node[k:" + key + "][" + degree + "]");
            if (mark) {
                builder.append("[x]");
            } else {
                builder.append("[-]");
            }
            builder.append('\n');
            if (p != null) builder.append("parent: " + p.key);
            else builder.append("parent: null");
            if (left != null) builder.append(" - left: " + left.key);
            else builder.append(" - left: null");
            if (right != null) builder.append(" - right: " + right.key);
            else builder.append(" - right: null");
            if (child != null) builder.append(" - child: " + child.key);
            else builder.append(" - child: null");
            builder.append('\n');
            builder.append('\n');
            //println("children = " + degree);
            Node<T> next = child;
            for (int i = degree; i > 0; i--) {
                builder.append(next);
                next = next.right;
            }
            return builder.toString();
        }
    }

    private void insert(Node<T> x) {
        x.degree = 0;
        x.p = null;
        x.child = null;
        x.mark = false;
        if (min == null) {
            x.left = x;
            x.right = x;
            this.min = x;
        } else {
            x.left = min;
            x.right = min.right;
            min.right.left = x;
            min.right = x;
            if (x.lessThan(min)) {
                this.min = x;
            }
        }
        n++;
    }

    public static <K extends Comparable<K>> FibonacciHeap<K> union(FibonacciHeap<K> h1, FibonacciHeap<K> h2) {
        FibonacciHeap<K> h = new FibonacciHeap<>();
        h.min = h1.min;
        // (h2.min.left) ---> h1.min ---> h1.min.left <--> h2.min ---> h2.min.left ---> (h1.min)
        Node<K> tmp = h1.min.left;
        h1.min.left = h2.min.left;
        h2.min.left = tmp;
        h1.min.left.right = h2.min;
        h2.min.left.right = h1.min;
        if (h1.min == null || (h2.min != null && h2.min.lessThan(h1.min))) {
            h.min = h2.min;
        }
        h.n = h1.n + h2.n;
        return h;
    }

    public Node<T> extractMin() {
        Node<T> z = min;
        if (z != null) {
            Node<T> x = z.child;
            for (int i = z.degree; i > 0; i--) {
                Node<T> next = x.right;

                // add x to the root list
                x.left = min;
                x.right = min.right;
                min.right = x;
                min.right.left = x;

                x.p = null;
                x = next;
            }
            // remove z from the root list 
            z.right.left = z.left;
            z.left.right = z.right;
            z.degree = 0;
            n--;

            if (z.equals(z.right)) {
                this.min = null;
            } else {
                this.min = z.right;
                consolidate();
            }
        }
        return z;
    }

    private void consolidate() {
        //Node[] aux = (Node[]) new Object[maxDegree(n)];
        println("n = " + n);
        println("Max Degree = " + maxDegree(n));
        List<Node<T>> aux = new ArrayList<>();
        //for (int i = 0; i < aux.length; i++) aux[i] = null;
        for (int i = 0; i <= maxDegree(n) + 1; i++) {
            aux.add(i, null);
        }
        Node<T> w = min;
        //while (!w.right.equals(w)) {
        for (int i = n; i > 0; i--) {
            println("w: " + w.key);
            Node<T> x = w;
            int d = x.degree;
            println("x.degree = " + d);
            //while (aux[d] != null) {
            while (aux.get(d) != null) {
                println("x: " + x.key);
                //Node<T> y = (Node<T>) aux[d];
                println("d = " + d);
                println("aux[d] = " + aux.get(d));
                Node<T> y = aux.get(d);
                if (x.moreThan(y)) {
                    println(x.key + " < " + y.key);
                    Node<T> tmp = x;
                    x = y;
                    y = tmp;
                }
                println(x.key + " : " + y.key);
                link(y, x);
                //aux[d] = null;
                aux.set(d, null);
                d++;
            }
            //aux[d] = x;
            aux.set(d, x);
            w = w.right;
        }
        this.min = null;
        //for (int i = 0; i < aux.length; i++) {
        for (int i = 0; i < aux.size(); i++) {
            //Node<T> t = aux[i];
            Node<T> t = aux.get(i);
            if (t != null) {
                if (min == null) {
                    this.min = t;
                } else {
                    t.left = min;
                    t.right = min.right;
                    min.right.left = t;
                    min.right = t;
                    if (t.lessThan(min)) {
                        this.min = t;
                    }
                }
            }
        }
    }

    /**
     * Make node y a child of node x.
     * @param x parent node.
     * @param y child node.
     */
    private void link(Node<T> y, Node<T> x) {
        if (x == null && y == null) {
            println("linking: null + null");
        } else if (x == null) {
            println("linking: " + x.key + " + null");
        } else if (y == null) {
            println("linking: null + " + y.key);
        } else {
            println("linking: " + x.key + " + " + y.key);
        }
        // remove y from the root list
        y.left.right = y.right;
        y.right.left = y.left;
        n--;
        // if x has no children make y the only child of x
        if (x.child == null) {
            y.right = y;
            y.left = y;
            x.child = y;
        // otherwise make y a sibling of x.child
        } else {
            // [x.child] ---[y]--- [x.child.right]
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        x.degree++;
        y.p = x;
        y.mark = false;
    }

    /**
     * Decreases the value of the key of a given node x to value k.
     * If k is less than the parents key value, x is cut and added
     * to the root list, then we cascade cut all marked parents on
     * the way up the tree.
     * It also updates the min node to x if k is less than min.key.
     *
     * @param x the node to decrease its key value.
     * @param k the new key value.
     * @throws IllegalArgumentException if the new key value k is larger
     *         than the old key value x.key.
     */
    public void decreaseKey(Node<T> x, T k) {
        if (k.compareTo(x.key) > 0) {
            throw new IllegalArgumentException("new key > current key");
        }
        println("decreasing key: " + x.key + " -> " + k);
        println(x.toString());
        x.key = k;
        println(x.toString());
        Node<T> y = x.p;
        if (y != null && x.lessThan(y)) {
            cut(x, y);
            cascadeCut(y);
        }
        if (x.lessThan(min)) {
            this.min = x;
        }
    }

    /**
     * removes a node x from its parent y and adds it to the root list
     * after clearing the mark and paent attributes of node x.
     *
     * @param x The child node.
     * @param y	The parent node.
     */
    private void cut(Node<T> x, Node<T> y) {
        if (x == null || y == null) {
            String err = "parent and child can't be null: x=" + x + " y=" + y;
            throw new IllegalArgumentException(err);
        }
        // remove x from child list of y
        if (x.right.equals(x)) {
            y.child = null;
        } else {
            if (y.child.equals(x)) {
                y.child = x.right;
            }
            x.left.right = x.right;
            x.right.left = x.left;
        }
        y.degree--;
        // add x to the root list
        x.left = min;
        x.right = min.right;
        min.right.left = x;
        min.right = x;
        x.p = null;
        x.mark = false;
        n++;
    }

    /**
     * Called after cutting a node, it makes sure that all marked 
     * parents are also cut while recursing up the tree.
     * It stops once it finds an unmarked parent.
     *
     * @param y the node to cascade on 
     */
    private void cascadeCut(Node<T> y) {
        Node<T> z = y.p;
        if (z != null) {
            if (y.mark == false) {
                y.mark = true;
            } else {
                cut(y, z);
                cascadeCut(z);
            }
        }
    }

    /**
     * Delete a node from the fibonacci tree.
     * @param x the node to be deleted.
     * @throws IllegalArgumentException if x is null.
     */
    private void delete(Node<T> x) {
        if (x == null) throw new IllegalArgumentException("node is null");
        x.isNegativeInfinity = true;
        decreaseKey(x, x.key);
        extractMin();
    }

    private static void print(String msg) {
        if (debug) System.out.print(msg);
    }

    private static void println(String msg) {
        if (debug) System.out.println(msg);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("min: " + min.key);
        builder.append('\n');
        builder.append("n: " + n);
        builder.append('\n');
        builder.append("root list: ");
        builder.append('\n');
        Node<T> next = min;
        for (int i = n; i > 0; i--) {
            builder.append("root:");
            builder.append('\n');
            builder.append(next);
            next = next.right;
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        FibonacciHeap<Integer> fib = new FibonacciHeap<>();
        Node<Integer> node53 = new Node<>(53);
        Node<Integer> node42 = new Node<>(42);
        fib.insert(node42);
        fib.insert(node53);
        fib.insert(new Node<Integer>(23));
        fib.insert(new Node<Integer>(6));
        fib.insert(new Node<Integer>(1));
        System.out.println("min: " + fib.min.key);
        Node<Integer> m = fib.extractMin();
        System.out.println("extracted min: " + m.key);
        System.out.println();
        fib.insert(new Node<Integer>(8));
        System.out.println(fib);
        fib.decreaseKey(node53, 2);
        System.out.println(fib);
        fib.delete(node42);
        System.out.println(fib);
    }
}
