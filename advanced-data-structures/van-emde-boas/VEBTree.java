import java.util.List;
import java.util.ArrayList;

/**
 * Van-Emde-Boas Tree.
 * van-Emde-Boas trees support priority-queue operations (INSERT, REMOVE)
 * and few other dynamic-set operations (SUCCESSOR, PREDECESSOR, MEMBER) in
 * O(lglgu) worst-case running time, where u is the universe size.
 * However, the keys must be integers ranging from 0 to u - 1, and 
 * duplicates keys are not allowed.
 * To store an element in a vEB tree, it is broken into most significant
 * bits {@code high(x)} and least significant bits {@code low(x)} in 
 * terms of universe size u of that tree. The former tells us which
 * vEB tree in the cluster array to choose and the latter tells us where to
 * store the element in that cluster.
 * The height of the tree is lglgu and the branching factor at each level is
 * square root of the universe size of the previous level.
 * The minimum element of each vEB tree is stored in {@code min} which does
 * not appear in any of the vEB trees that the cluster array points to.
 * Space requirement is O(u).
 */
public class VEBTree {
    public static final int NIL = -1;
    private final VEBNode root;

    /**
     * The constructer. Creates and initializes the tree.
     *
     * @param u the universe size.
     * @throws IllegalArgumentException if u is not valid.
     */
    public VEBTree(int u) {
        if (!isValidUniverseSize(u)) {
            throw new IllegalArgumentException("u is not valid");
        }
        this.root = new VEBNode(u);
    }

    /**
     * Check if the universe size is valid.
     * Universe size u is only valid if: u is greater or equal to 2,
     * and u is a power of 2.
     *
     * @param u the universe size to be tested.
     */
    private boolean isValidUniverseSize(int u) {
        if (u < 2) return false;
        while (u % 2 == 0) {
            u = u / 2;
        }
        if (u != 1) return false;
        // Another way to do it using a bitwise trick:
        // {@link https://codereview.stackexchange.com/a/172853}
        // return u >= 2 && (u-- & u) == 0;
        return true;
    }

    /**
     * Find the smallest element in the tree.
     * 
     * @param v the tree to find its minimum element.
     * @return the minimum {@code min}.
     */
    private int minimum(VEBNode v) {
        return v.min;
    }
    
    /**
     * Find the largest element in the tree.
     * 
     * @param v the tree to find its maximum element.
     * @return the maximum {@code max}.
     */
    private int maximum(VEBNode v) {
        return v.max;
    }

    /**
     * Check if tree v contains the element x or not.
     *
     * @param v the tree to search in.
     * @param x the element to look for.
     */
    private boolean member(VEBNode v, int x) {
        if (x == v.min || x == v.max) return true;
        if (v.u == 2) return false;
        return member(v.cluster[v.high(x)], v.low(x));
    }

    /**
     * Finds the successor of an integer x within the node v.
     * There are three cases that are used to determin the successor:
     * 1) v is a leaf. In this case x can only have a successor if 
     * x == 0 and {@code max} == 1.
     * 2) x is less than {@code min} <em>(this case occurs when x's
     * successor does not exist in its cluster, therefore, we search one
     * of the x's non-empty neighbouring clusters using a recursive
     * {@code successor} call on that cluster. Because x does not exist in
     * in the neighbour cluster, x can be less than {@code min})</em> 
     * in which case, {@code min} would be the successor of x.
     * 3) {@code u} is greater than 2 and x is greater than {@code min}. In
     * this case check if the successor exists in the same cluster as x and
     * return it if exists. Otherwise, determine which neighbour cluster
     * contains the successor using the information stored in {@code summary}
     * and find the successor.
     *
     * @param v the tree to find the successor in.
     * @param x the element we need to find its successor.
     * @return the successor of x if exists or NIL.
     */
    private int successor(VEBNode v, int x) {
        if (v.u == 2) {
            if (x == 0 && v.max == 1) return 1;
            return NIL;
        } else if (v.min != NIL && x < v.min) {
            return v.min;
        } else {
            // check if the maximum element of x's cluster is greater
            // than x. If true, this means that x's successor exists
            // in the same cluster as x.
            int maxLow = maximum(v.cluster[v.high(x)]);
            if (maxLow != NIL && v.low(x) < maxLow) {
                int offset = successor(v.cluster[v.high(x)], v.low(x));
                return v.index(v.high(x), offset);
            } else {
                // successor is not in same cluster, find the cluster
                // that contains the predecessor "must be one of the right
                // siblings".
                int succCluster = successor(v.summary, v.high(x));
                if (succCluster == NIL) return NIL;
                int offset = minimum(v.cluster[succCluster]);
                return v.index(succCluster, offset);
            }
        }
    }

    /**
     * Finds the predecessor of an integer x within the node v.
     * There are three cases that are used to determin the predecessor:
     * 1) v is a leaf. In this case x can only have a predecessor if 
     * x == 1 and {@code min} == 0.
     * 2) x is greater than {@code max} <em>(this case occurs when x's
     * predecessor does not exist in its cluster, therefore, we search one
     * of the x's non-empty neighbouring clusters using a recursive
     * {@code predecessor} call on that cluster. Because x does not exist in
     * in the neighbour cluster, x can be greater than {@code max})</em> 
     * in which case, {@code max} would be the predecessor of x.
     * 3) {@code u} is greater than 2 and x is less than {@code max}. In
     * this case check if the predecessor exists in the same cluster as x and
     * return it if exists. Otherwise, determine which neighbour cluster
     * contains the predecessor using the information stored in {@code summary}
     * and find the predecessor.
     *
     * @param v the tree to find the predecessor in.
     * @param x the element we need to find its predecessor.
     * @return the predecessor of x if exists or NIL.
     */
    private int predecessor(VEBNode v, int x) {
        if (v.u == 2) {
            if (x == 1 && v.min == 0) return 0;
            return NIL;
        } else if (v.max != NIL && x > v.max) {
            return v.max;
        } else {
            // check if the minimum element of x's cluster is less
            // than x. If true, this means that x's predecessor exists
            // in the same cluster as x.
            int minLow = minimum(v.cluster[v.high(x)]);
            if (minLow != NIL && v.low(x) > minLow) {
                int offset = predecessor(v.cluster[v.high(x)], v.low(x));
                return v.index(v.high(x), offset);
            } else {
                // predecessor is not in same cluster, find the cluster
                // that contains the predecessor "must be one of the left
                // siblings".
                int predCluster = predecessor(v.summary, v.high(x));
                if (predCluster == NIL) {
                    if (v.min != NIL && x > v.min) return v.min;
                    return NIL;
                }
                int offset = maximum(v.cluster[predCluster]);
                return v.index(predCluster, offset);
            }
        }
    }

    /**
     * Inserts an element x to an empty vEB tree v.
     *
     * @param v the empty tree.
     * @param x the elemtn to be inserted.
     */
    private void emptyTreeInsert(VEBNode v, int x) {
        v.min = x;
        v.max = x;
    }

    /**
     * Inserts an element x to a vEB tree v.
     * If v is empty, just set {@code min} and {@code max} to x.
     * Otherwise, if x is less than {@code min} or x is larger than
     * {@code max}, then update {@code min} or {@code max} value 
     * to x. Also, if v is not a leaf and the cluster where x needs to
     * be inserted is empty, then insert the cluster number in {@code summary} 
     * and empty insert x into that cluster. But if that cluster where x
     * should be inserted is not empty, then just insert x.
     *
     * @param v the empty tree.
     * @param x the elemtn to be inserted.
     */
    private void insert(VEBNode v, int x) {
        if (v.min == NIL) {
            emptyTreeInsert(v, x);
        } else {
            if (x < v.min) {
                // if x is less than min then set x as the new min, but
                // we don't want to lose the original min, so we swap it with x
                // and insert it into the tree instead of x.
                int tmp = v.min;
                v.min = x;
                x = tmp;
            }
            if (v.u > 2) {
                if (minimum(v.cluster[v.high(x)]) == NIL) {
                    insert(v.summary, v.high(x));
                    emptyTreeInsert(v.cluster[v.high(x)], v.low(x));
                } else {
                    insert(v.cluster[v.high(x)], v.low(x));
                }
            }
            if (x > v.max) {
                v.max = x;
            }
        }
    }

    /**
     * Deletes an element x from the vEB tree v.
     * Case   I: v has only one element, delete that element.
     * Case  II: v is a base-case vEB tree and has two elements, delete
     *		either element and update {@code min}, {@code max}.
     * Case III: v is not base-case vEB tree and has more than two elements.
     *		If x is the {@code min} element of the tree, we can't delete 
     *		it because it does not exist any where in the tree, 
     *		we just need to find the next smallest element in the cluster
     *		and set it as the new {@code min}, then we delete that
     *		element from the tree (we make it the new x).
     *		Delete x from its cluster and then:
     *		Case III-a: x was the last element of its cluster, and the
     *			cluster is now empty. We remove this cluster number 
     *			from {@code summary}.
     *			If x was the {@code max} element of the tree v, we find 
     *			the new maximum element of v and update {@code max}. However,
     *			if all clusters of v become empty after deleting x, then the
     *			only element remaining in v now is {@code min}, and therefore,
     *			we set {@code max} equals to {@code min}.
     *		Case III-b: x was not the last element of its cluster but x was
     *			the {@code max} element of the v. We find the new maximum
     *			element of the cluster and set it and the new {@code max}.
     *
     * @param v the tree in which x needs to be deleted.
     * @param x the element to be deleted.
     */
    private void delete(VEBNode v, int x) {
        if (v.min == v.max) {
            // v contains only one element
            v.min = NIL;
            v.max = NIL;
        } else if (v.u == 2) {
            // v is a base-case vEB tree and contains two elements.
            if (x == 0) {
                v.min = 1;
            } else {
                v.min = 0;
            }
            v.max = v.min;
        } else {
            // v is not a base-case vEB tree and has more than one element.
            if (x == v.min) {
                int firstCluster = minimum(v.summary);
                x = v.index(firstCluster, minimum(v.cluster[firstCluster]));
                v.min = x;
            }
            delete(v.cluster[v.high(x)], v.low(x));
            if (minimum(v.cluster[v.high(x)]) == NIL) {
                // x was the last element in its cluster before deleting it.
                delete(v.summary, v.high(x));
                if (x == v.max) {
                    // the deleted element x was the max element of v.
                    int summaryMax = maximum(v.summary);
                    if (summaryMax == NIL) {
                        // tree v is now either empty or has only the 
                        // min element.
                        v.max = v.min;
                    } else {
                        // tree v has more than one element, find the new max
                        // and replace it with the old max value (which = x).
                        int offset = maximum(v.cluster[summaryMax]);
                        v.max = v.index(summaryMax, offset);
                    }
                }
            } else if (x == v.max) {
                // x was not the last element of its cluster but it was
                // the max element of that cluster.
                v.max = v.index(v.high(x), maximum(v.cluster[v.high(x)]));
            }
        }
    }

    public int minimum() {
        return minimum(root);
    }
    
    public int maximum() {
        return maximum(root);
    }

    public boolean member(int x) {
        return member(root, x);
    }

    public int successor(int x) {
        return successor(root, x);
    }

    public int predecessor(int x) {
        return predecessor(root, x);
    }

    public void insert(int x) {
        insert(root, x);
    }

    public void delete(int x) {
        delete(root, x);
    }

    /**
     * Unit Test helper function. Returns all members within a vEB tree.
     *
     * @param v the tree to find its members.
     */
    private List<Integer> members(VEBNode v) {
        if (v.min == NIL) return null;
        List<Integer> arr = new ArrayList<>();
        arr.add(v.min);
        if (v.u == 2) {
            if (v.min != v.max) arr.add(v.max);
        } else {
            for (int i = 0; i < v.size(); i++) {
                List<Integer> elems = members(v.cluster[i]);
                if (elems == null) continue;
                for (int j : elems) {
                    arr.add(v.index(i, j));
                }
            }
        }
        return arr;
    }

    public void members() {
        System.out.println("members: ");
        for (int x : members(root)) System.out.println(x);
    }

    public static void main(String[] args) {
        VEBTree veb = new VEBTree(16);
        veb.insert(2);
        veb.insert(3);
        veb.insert(4);
        veb.insert(5);
        veb.insert(7);
        veb.insert(14);
        veb.insert(15);
        veb.members();
        System.out.println("Minimum element: " + veb.minimum());
        System.out.println("Maximun element: " + veb.maximum());
        System.out.println("Successor of 3: " + veb.successor(3));
        System.out.println("Successor of 7: " + veb.successor(7));
        System.out.println("Successor of 15: " + veb.successor(15));
        System.out.println("Predecessor of 5: " + veb.predecessor(5));
        System.out.println("Predecessor of 14: " + veb.predecessor(14));
        System.out.println("Predecessor of 2: " + veb.predecessor(2));
        veb.delete(7);
        System.out.println("7 deleted.");
        veb.delete(3);
        System.out.println("3 deleted.");
        veb.members();
    }
}
