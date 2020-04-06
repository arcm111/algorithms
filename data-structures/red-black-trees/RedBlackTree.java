/**
 * Red-Black trees are (kind of) self-balancing binary search trees with one
 * extra bit of storage for colour that is used to balance the tree.
 * Red-Black trees ensures that any simple path from the root to a left is less
 * that twice as long as any other, therefore, they are aproximately balanced.
 * Red-Black trees properties:
 * - every node is either red or black
 * - the root is black
 * - every leaf(NIL) is black
 * - red nodes can have only black children
 * - all simple paths from a node to all descendent leaves have the same number
 *   of black nodes within them
 * Height of the tree is at most 2lg(n+1)
 */
public class RedBlackTree<T extends Comparable<T>> {
    private final static boolean RED = true;
    private final static boolean BLACK = false;
    private int n = 0; // number of nodes in the tree
    private final Node NIL = new Node(null);
    private Node root = NIL;

    /**
     * Left rotation is used to restore tree properties after performing a 
     * modifying operations such as insert or delete and also to reduce the
     * height of the tree by moving larger subtrees up and smaller ones down.
     * It changes the pointer structure of a node x and its right child y
     * without affecting nodes properties. 
     * It pivots around the link from x to y making y the new root of the 
     * subtree rooted at x, x becomes the left child of y and the left child
     * of y becomes the right child of x.
     * Right child of x can not be NIL.
     * Does not affect the order of the elements in the tree.
     * Running time is <em>O(1)</em>.
     * @param x the node to pivot around it and its right child
     * @throws IllegalArgumentException if x's right child is NIL
     */
    private void leftRotate(Node x) {
        if (x.right.equals(NIL)) {
            throw new IllegalArgumentException("right child can't be nil");
        }
        Node y = x.right;
        x.right = y.left;
        if (!y.left.equals(NIL)) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent.equals(NIL)) {
            this.root = y;
        } else if (x.parent.left.equals(x)) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    /**
     * Right rotation is used to restore tree properties after performing a 
     * modifying operations such as insert or delete and also to reduce the
     * height of the tree by moving larger subtrees up and smaller ones down.
     * It changes the pointer structure of a node x and its left child y
     * without affecting nodes properties. 
     * It pivots around the link from x to y making y the new root of the 
     * subtree rooted at x, x becomes the right child of y and the right child
     * of y becomes the left child of x.
     * Left child of x can not be NIL.
     * Does not affect the order of the elements in the tree.
     * Running time is <em>O(1)</em>.
     * @param x the node to pivot around it and its right child
     * @throws IllegalArgumentException if x's right child is NIL
     */
    private void rightRotate(Node x) {
        if (x.left.equals(NIL)) {
            throw new IllegalArgumentException("left child can't be nil");
        }
        Node y = x.left;
        x.left = y.right;
        if (!y.right.equals(NIL)) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent.equals(NIL)) {
            this.root = y;
        } else if (x.parent.right.equals(x)) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    /**
     * Inserts a new node into the tree.
     * Inserts the node in its correct location according to its key and colours
     * it red. then it runs {@code insertFixup} to perform colouring and 
     * rotations necessary to fix violations in tree properties thay may
     * have been caused by colouring the new node red.
     * Running time is <em>O(lgn)</em>.
     * @param key the key of the new node to be inserted
     * @throws IllegalArgumentException if the key is null
     */
    public void insert(T key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }
        Node z = new Node(key);
        Node y = NIL;
        Node x = root;
        while (!x.equals(NIL)) {
            y = x;
            if (z.key.compareTo(x.key) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y.equals(NIL)) {
            this.root = z;
        } else if (z.key.compareTo(y.key) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.colour = RED;
        insertFixup(z);
        n++;
    }

    /**
     * Restore tree properties that might have been violated after inserting 
     * a new red node.
     * Performs at most two rotations when it encounters a case 2 that evolves
     * to case 3.
     * Possible cases to handle when parent of z is left child:
     * - case (1) [both parent and uncle are red]: 
     *   colour them both black and grandparent red and move z up two levels.
     * - case (2) [parent is red, uncle is black and z is right child]: 
     *   left rotate parent and move z one level up.
     * - case (3) [parent is red, uncle is black and z is left child]: 
     *   right rotate grandparent and colour it red, colour parent black and 
     *   terminate.
     * If z's parent is right child same cases apply but with "right" and "left"
     * exchanged.
     * Running time is <em>O(lgn)</em> which occurs if case 1 keeps iterating.
     * @param z the newly inserted node
     */
    private void insertFixup(Node z) {
        // at the beginning of each iteration z and its parent are both red
        while (z.parent.colour == RED) {
            // z's parent is a left child
            if (z.parent.equals(z.parent.parent.left)) {
                Node y = z.parent.parent.right; // uncle
                if (y.colour == RED) {
                    // case 1
                    z.parent.colour = BLACK;
                    y.colour = BLACK;
                    z.parent.parent.colour = RED;
                    z = z.parent.parent;
                } else {
                    if (z.equals(z.parent.right)) {
                        // case 2
                        z = z.parent;
                        leftRotate(z);
                    }
                    // case 3
                    z.parent.colour = BLACK;
                    z.parent.parent.colour = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                // z's parent is a right child
                Node y = z.parent.parent.left; // uncle
                if (y.colour == RED) {
                    // case 1
                    z.parent.colour = BLACK;
                    y.colour = BLACK;
                    z.parent.parent.colour = RED;
                    z = z.parent.parent;
                } else {
                    if (z.equals(z.parent.left)) {
                        // case 2
                        z = z.parent;
                        rightRotate(z);
                    }
                    // case 3
                    z.parent.colour = BLACK;
                    z.parent.parent.colour = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        // fix property 2 when z becomes the root
        root.colour = BLACK;
    }

    /**
     * Check if a key is stored in the tree.
     * @param key the key to check its existance in the tree
     * @return whether the key is in the tree or not
     */
    public boolean contains(T key) {
        return !search(key).equals(NIL);
    }

    /**
     * Finds a node containing a specific key.
     * Running time is <em>O(lgn)</em>.
     * @param key the key to find its node
     * @return the node containing the key or NIL
     * @throws IllegalArgumentException if the key is null
     */
    private Node search(T key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }
        return search(root, key);
    }

    /**
     * Finds a node containing a specific key within a subtree.
     * Running time is <em>O(lgn)</em>.
     * @param x the subtree rooted at x
     * @param key the key to find its node within the subtree
     * @return the node containing the key or NIL
     */
    private Node search(Node x, T k) {
        if (x.equals(NIL) || x.key.compareTo(k) == 0) return x;
        if (k.compareTo(x.key) < 0) {
            return search(x.left, k);
        }
        return search(x.right, k);
    }

    /**
     * Removes a node containing a specific key from the tree.
     * Running time is <em>O(lgn)</em>.
     * @param key the key of the node to remove
     * @throws IllegalArgumentException if the key is null or is not in the tree
     */
    public void delete(T key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }
        Node x = search(key);
        if (x.equals(NIL)) {
            throw new IllegalArgumentException("element is not in the tree");
        }
        delete(x);
    }

    /**
     * Removes a given node from the tree.
     * If the node to be removed has only one child, we replace it with its 
     * only child and remove it from the tree. Otherwise, we find it's successor
     * in its subtree and use the successor to replace it. The successor node
     * does not have a left child "by definition" so we replace it with its
     * right child.
     * Running time is <em>O(lgn)</em>.
     */
    private void delete(Node z) {
        // the node either to be moved (to replace z if y is z's successor)
        // or removed from the tree (if y is z).
        Node y = z; 
        Node x; // the node to replace y
        boolean yOriginalColour = y.colour;
        if (y.left.equals(NIL)) {
            x = y.right;
            transplant(z, x);
        } else if (y.right.equals(NIL)) {
            x = y.left;
            transplant(z, x);
        } else {
            y = minimum(z.right); // z successor
            yOriginalColour = y.colour;
            x = y.right;
            if (y.parent.equals(z)) { // y is the right child of z
                // After x replaces y, we set parent of x to point to the parent
                // of y. Unless y is the direct child of z, because the parent
                // of y (z) will be removed from the tree so we set x.p to y. 
                // The assignment x.p = y.p occurs in the last line of the 
                // transplant(y, x) operation.
                // It seems pointless here, however, to assign x.p = y becuase
                // x did not move in the tree at this point and its parent was 
                // y before the assignment.
                x.parent = y;
            } else {
                transplant(y, x);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.colour = z.colour;
        }
        if (yOriginalColour == BLACK) {
            deleteFixup(x);
        }
        n--;
    }

    /**
     * Restore tree properties that might have been violated after deleting 
     * a node.
     * Deleting a red node does not violate tree properties becuase no 
     * black-heights is changed and no red nodes has been made adjacent.
     * Possible violations after removing a black node are: red root, two
     * adjacent red nodes, variation in black-heights.
     * Four cases are handeled (parent of x is left child of the grandparent):
     * - case 1 [sibling w is red]: parent of w is black (because it has a red
     *   child), we switch w and its parent colours and left rotate the parent.
     * - case 2 [both children of sibling w are black]: colour w red and move
     *   x up one level.
     * - case 3 [sibling w and its right child are black but left child is red]:
     *   right-rotate w and switch colours of w and its left child.
     * - case 4 [sibling w is black and its right child is red]: left-rotate 
     *   parent of w and switch colours of w and its parent and colour right 
     *   child of w black and terminate.
     * On termination set x colour to black.
     * If parent of x is right child of its parent we do the same but after
     * exchanging "left" with "right".
     * @param x the node that caused the potential violation
     */
    private void deleteFixup(Node x) {
        while (!x.equals(root) && x.colour == BLACK) {
            if (x.equals(x.parent.left)) {
                Node w = x.parent.right; // x's sibling
                if (w.colour == RED) { // case 1
                    // x's parent cannot be red because its right child w is red
                    // and x's movement during the delete operation did not 
                    // change the colour or position of its parent, therefore, 
                    // due to RBT properties parent of a red node cannot be red.
                    // because w is red, both of its children must be black
                    w.colour = BLACK;
                    x.parent.colour = RED;
                    leftRotate(x.parent);
                    // sibling of x is now w's left child which is black
                    // new w is now the left child of old w
                    // new w is black
                    w = x.parent.right;
                }
                // w colour is black here
                if (w.left.colour == BLACK && w.right.colour == BLACK) {
                    // case 2
                    w.colour = RED;
                    x = x.parent;
                } else { // at least one of w's children is red
                    if (w.right.colour == BLACK) { // case 3
                        // left child of w is red and its right child is black
                        w.left.colour = BLACK;
                        w.colour = RED;
                        // after right rotation w becomes the right child of
                        // its old left child which in turn becomes the
                        // sibling of x, so x.parent.right = (old) w.left
                        // and x.parent.right.right = w (red)
                        rightRotate(w);
                        // new w is the parent of old w which also (new w) was 
                        // the left child of old w before the right rotation
                        // new w's right child is the old w which is red now
                        // new w's colour is black now
                        w = x.parent.right;
                    }
                    // case 4
                    // w colour is black here and its right child is red and its
                    // left child is black;
                    w.colour = x.parent.colour;
                    x.parent.colour = BLACK;
                    w.right.colour = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.colour == RED) {
                    // case 1
                    w.colour = BLACK;
                    x.parent.colour = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.left.colour == BLACK && w.right.colour == BLACK) {
                    // case 2
                    w.colour = RED;
                    x = x.parent;
                } else { // at least one of w's children is red
                    if (w.left.colour == BLACK) {
                        // case 3
                        // right child of w is red
                        w.right.colour = BLACK;
                        w.colour = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    // case 4
                    // w colour is black here and its left child is red and the
                    // right child is black
                    w.colour = x.parent.colour;
                    x.parent.colour = BLACK;
                    w.left.colour = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.colour = BLACK;
    }

    /**
     * Replaces one node by another in the tree.
     * @param u the node to be replaced
     * @param v the replacement node
     */
    private void transplant(Node u, Node v) {
        if (u.parent.equals(NIL)) { // u is the root
            this.root = v;
        } else if (u.equals(u.parent.left)) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    /**
     * Finds the node with minimum key value within the subtree rooted at a 
     * given node.
     * Running time is <em>O(lgn)</em>.
     * @param x the root of the subtree in which we need to find the minimum
     * @return the node containing the smallest key in the subtree
     */
    private Node minimum(Node x) {
        while (!x.left.equals(NIL)) {
            x = x.left;
        }
        return x;
    }

    /**
     * Finds the smallest key in the tree.
     * @return the smallest key in the tree
     * @throws IndexOutOfBoundsException if the tree is empty
     */
    public T minimum() {
        if (n == 0) {
            throw new IndexOutOfBoundsException("tree is empty");
        }
        return minimum(root).key;
    }

    /**
     * Finds the node with maximum key value within the subtree rooted at a 
     * given node.
     * Running time is <em>O(lgn)</em>.
     * @param x the root of the subtree in which we need to find the maximum
     * @return the node containing the largest key in the subtree
     */
    private Node maximum(Node x) {
        while (!x.right.equals(NIL)) {
            x = x.right;
        }
        return x;
    }

    /**
     * Finds the largest key in the tree.
     * @return the largest key in the tree
     * @throws IndexOutOfBoundsException if the tree is empty
     */
    public T maximum() {
        if (n == 0) {
            throw new IndexOutOfBoundsException("tree is empty");
        }
        return maximum(root).key;
    }

    /**
     * Extracts the node with the smallest key from the tree.
     * Running time is <em>O(lgn)</em>.
     * @return smallest key in the tree
     * @throws IndexOutOfBoundsException if the tree is empty
     */
    public T extractMin() {
        if (n == 0) {
            throw new IndexOutOfBoundsException("tree is empty");
        }
        Node min = minimum(root);
        T key = min.key;
        delete(min);
        return key;
    }

    /**
     * Extracts the node with the largest key from the tree.
     * Running time is <em>O(lgn)</em>.
     * @return largest key in the tree
     * @throws IndexOutOfBoundsException if the tree is empty
     */
    public T extractMax() {
        if (n == 0) {
            throw new IndexOutOfBoundsException("tree is empty");
        }
        Node max = maximum(root);
        T key = max.key;
        delete(max);
        return key;
    }

    /**
     * Findes the node with the smallest key larger than x's key.
     * Running time is <em>O(lgn)</em>.
     * @param x the node to find its successor
     * @return the successor node
     * @throws IllegalArgumentException if x is NIL
     */
    private Node successor(Node x) {
        if (x.equals(NIL)) {
            throw new IllegalArgumentException("x can't be NIL!");
        }
        if (!x.right.equals(NIL)) {
            return minimum(x.right);
        }
        Node y = x.parent;
        while (!y.equals(NIL) && x.equals(y.right)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * Finds the next key larger than a given value.
     * @param key the key to find its successor
     * @return the successor
     * @throws IllegalArgumentException if the given key is not in the tree
     */
    public T successor(T key) {
        Node x = search(key);
        if (x.equals(NIL)) {
            throw new IllegalArgumentException("key not in tree: " + key);
        }
        Node y = successor(x);
        if (y.equals(NIL)) return null;
        return y.key;
    }

    /**
     * Findes the node with the largest key smaller than x's key.
     * Running time is <em>O(lgn)</em>.
     * @param x the node to find its predecessor
     * @return the predecessor node
     * @throws IllegalArgumentException if x is NIL
     */
    private Node predecessor(Node x) {
        if (x.equals(NIL)) {
            throw new IllegalArgumentException("x can't be NIL!");
        }
        if (!x.left.equals(NIL)) {
            return maximum(x.left);
        }
        Node y = x.parent;
        while (!y.equals(NIL) && x.equals(y.left)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * Finds the next key smaller than a given value.
     * @param key the key to find its predecessor
     * @return the predecessor
     * @throws IllegalArgumentException if the given key is not in the tree
     */
    public T predecessor(T key) {
        Node x = search(key);
        if (x.equals(NIL)) {
            throw new IllegalArgumentException("key not in tree: " + key);
        }
        Node y = predecessor(x);
        if (y.equals(NIL)) return null;
        return y.key;
    }

    /**
     * Red-Black tree's node class.
     */
    private class Node {
        public Node parent = NIL;
        public Node left = NIL;
        public Node right = NIL;
        public T key = null;
        public boolean colour = BLACK;

        public Node(T key) {
            this.key = key;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (this.getClass() != obj.getClass()) return false;
            Node that = (Node) obj;
            if (this.key == null && that.key == null) return true;
            if (this.key == null || that.key == null) return false;
            return this.key.compareTo(that.key) == 0;
        }
    }

    /**
     * Return the number of nodes in the tree.
     * @return number of nodes
     */
    public int size() {
        return n;
    }

    /**
     * Checks if the tree has no nodes.
     * @return true if empty tree, otherwise, false
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns a string representation of the tree.
     * @return the string representation of the tree
     */
    @Override
    public String toString() {
        return toString(root);
    }

    /**
     * Returns a string representation of a subtree rooted at a given node.
     * @param x the root of the subtree.
     * @return the string representation of the subtree
     */
    private String toString(Node x) {
        String c = x.colour ? "Red" : "Black";
        if (x.equals(NIL)) {
            if (x.equals(root)) return "Root[NIL]: " + c + "\n";
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String f = "[%s]: (%s) [%s - %s]\n";
        if (x.equals(root)) {
            f = "Root[%s]: (%s) [%s - %s]\n";
        }
        String left = x.left.equals(NIL) ? "NIL" : x.left.key.toString();
        String right = x.right.equals(NIL) ? "NIL" : x.right.key.toString();
        builder.append(String.format(f, x.key, c, left, right));
        builder.append(toString(x.left));
        builder.append(toString(x.right));
        return builder.toString();
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
        int[] elements = {2, 3, 4, 8, 6, 1, 5};
        for (int n : elements) {
            System.out.println("Insert[" + n + "]: ");
            rbt.insert(n);
            System.out.println(rbt);
        }
        System.out.println("Size: " + rbt.size());
        System.out.println("Minimum: " + rbt.minimum());
        System.out.println("Maximum: " + rbt.maximum());
        System.out.println("Successor of 6: " + rbt.successor(6));
        System.out.println("Predecessor of 6: " + rbt.predecessor(6));
        System.out.println();

        for (int n : elements) {
            System.out.println("Delete[" + n + "]: ");
            rbt.delete(n);
            System.out.println(rbt);
        }
        System.out.println("is empty: " + rbt.isEmpty());
        for (int n : elements) rbt.insert(n);
        System.out.println(rbt);
        while (!rbt.isEmpty()) {
            int min = rbt.extractMin();
            System.out.print(min + " ");
        }
        System.out.println();
    }
}
