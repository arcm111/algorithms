public class BTree<T extends Comparable<T>, V> {
	private final int MIN_DEGREE; // the minimum degree of the tree t
	private Node<T, V> root; // the root node of the tree
	private int num; // the number of keys in the tree
	private int height; // the height of the tree

	public BTree(int t) {
		this.MIN_DEGREE = t;
		this.root = new Node<>(t);
	}

	private V get(T k) {
		return search(root, k);
	}

	private V search(Node<T, V> x, T k) {
		int i = 0;
		while (i < x.n && greater(k, x.key(i))) {
			i++;
		}
		if (i < x.n && equals(k, x.key(i))) {
			return x.key(i).val;
		} else if (x.isLeaf) {
			return null;
		}
		return search(x.child(i), k);
	}

	public void put(T key, V value) {
		if (key == null) throw new IllegalArgumentException("Invalid key");
		insert(new Key<T, V>(key, value));
		num++;
	}

	/**
	 * Insert a key k into the tree.
	 * If the root is full then create a new empty node as the new root and
	 * make the old root its child and split.
	 */
	private void insert(Key k) {
		Node r = root;
		if (r.n == 2 * MIN_DEGREE - 1) {
			Node<T, V> s = new Node<>(MIN_DEGREE);
			this.root = s;
			s.isLeaf = false;
			s.setChild(0, r);
			splitChild(s, 0);
			height++;
			insertNonfull(s, k);
		} else {
			insertNonfull(r, k);
		}
	}

	public void insertNonfull(Node x, Key k) {
		int i = x.n - 1;
		if (x.isLeaf) {
			while (i >= 0 && k.lessThan(x.key(i))) {
				x.setKey(i + 1, x.key(i));
				i--;
			}
			x.setKey(i + 1, k);
			x.n++;
		} else {
			while (i >= 0 && k.lessThan(x.key(i))) {
				i--;
			}
			i++;
			if (x.child(i).n == 2 * MIN_DEGREE - 1) {
				splitChild(x, i);
				if (k.greaterThan(x.key(i))) {
					i++;
				}
			}
			insertNonfull(x.child(i), k);
		}
	}

	/**
	 * Split a full child that has 2t-1 nodes to two t-1 children
	 * and move the middle (t)th key up to its parent
	 */
	private void splitChild(Node x, int i) {
		//println(String.format("Splitting (%d) child of x[%d] ...", i, n));
		//println("x[i]: " + x.child(i).toString());
		Node<T, V> z = new Node<>(MIN_DEGREE);
		Node<T, V> y = x.child(i);
		z.isLeaf = y.isLeaf;
		z.n = MIN_DEGREE - 1;
		// copy the rightmost (t - 1) keys from y to z
		for (int j = 0; j < MIN_DEGREE - 1; j++) {
			z.setKey(j, y.key(MIN_DEGREE + j));
		}
		// copy the rightmost (t) children from y to z
		if (!y.isLeaf) {
			for (int j = 0; j <= MIN_DEGREE - 1; j++) {
				z.setChild(j, y.child(MIN_DEGREE + j));
			}
		}
		y.n = MIN_DEGREE - 1;
		// shift all right siblings of y one step to the right
		// to make room for z to be inserted next to y
		rightShiftChildren(x, i + 1);
		x.setChild(i + 1, z);
		// shift all keys including and on the right of the ith key of x
		// one step to the right
		// to make room for the (t)th key of y to be inserted
		rightShiftKeys(x, i);
		x.setKey(i, y.key(MIN_DEGREE - 1));
		//println("y: " + y.toString());
		//println("z: " + z.toString());
		x.n++;
	}

	public boolean delete(T key) {
		// if the root has no keys, replace it with its only child
		if (root.n == 0) {
			println("root is empty. replacing with only child");
			this.root = root.child(0);
		}
		if (deleteNonEmpty(root, key)) {
			num--;
			return true;
		}
		return false;
	}

	private boolean deleteNonEmpty(Node<T, V> x, T k) {
		println("Deleting[" + k + "]: " + x.toString());
		int i = 0;
		while (i < x.n && greater(k, x.key(i))) {
			i++;
		}
		println("n: " + x.n + ", i: " + i);
		if (i < x.n && equals(k, x.key(i))) {
			// if the requested key is found at i check if the node x is
			// a leaf or not and delete accordingly.
			println("Found key at index: " + i);
			if (x.isLeaf) {
				// just delte the key if x is a leaf
				println("Node x is a leaf: Deleting the key [" + k + "]");
				leftShiftKeys(x, i);
				x.n--;
				println("Deleted: " + x.toString());
			} else {
				// Otherwise, check left and right children of the key
				// we borrow a key from right or left child to fill the
				// gap created after deleting the key k located between them
				println("x is not a leaf");
				Node<T, V> y = x.child(i);
				if (y.n >= MIN_DEGREE) {
					// if left child has at least t - 1 keys, replace k with
					// its predecessor
					Key<T, V> predecessor = extractPredecessor(x, i);
					println("Replacing k with predecessor " + predecessor.key); 
					x.setKey(i, predecessor);
					x.n--;
					println("Deleted: " + x.toString());
				} else {
					Node<T, V> z = x.child(i + 1);
					if (z.n >= MIN_DEGREE) {
						// otherwise if the right child has at least t - 1 keys
						// replace k with its successor
						Key<T, V> successor = extractSuccessor(x, i + 1);
						println("Replacing k with successor " + successor.key); 
						x.setKey(i, successor);
						x.n--;
						println("Deleted: " + x.toString());
					} else {
						// if both left and right children has less than t - 1
						// keys then merge these children
						println("both y and z has less than t - 1, merging...");
						mergeChildren(x, i, i + 1);
						if (deleteNonEmpty(y, k)) {
							println("Deleted: " + x.toString());
						} else {
							return false;
						}
					}
				}
			}
		} else {
			// else if key k was not found in node x, recurse.
			if (!x.isLeaf) {
				// k is greater than x[i] child so we recurse to
				// the child x[i + 1] to the right of k
				Node<T, V> y = x.child(i);
				if (y.n <= MIN_DEGREE - 1) {
					if (i - 1 >= 0 && x.child(i - 1).n >= MIN_DEGREE) {
						int j = i - 1;
						// if left child of y exists then the parent key
						// is the one to the left of k
						Key parentKey = x.key(j);
						Node<T, V> z = x.child(j);
						println("x before swap: " + x.toString());
						println("y before swap: " + y.toString());
						println("z before swap: " + z.toString());
						println("Swapping with left sibling: " + j);
						Key siblingKey = z.key(z.n - 1);
						Node<T, V> siblingChild = z.child(z.n);
						z.setKey(z.n - 1, null);
						z.setChild(z.n, null);
						z.n--;
						print("Swapping parent key: " + parentKey.key);
						println(" with sibling key: " + siblingKey.key);
						x.setKey(j, siblingKey);
						// shift all keys of y one step to right to make 
						// room for the parent key to be added at beginning
						rightShiftKeys(y);
						// shift all children of y one step to right to make 
						// room for the sibling child to be added at beginning
						rightShiftChildren(y);
						y.n++;
						println("Right shifted: " + y.toString());
						y.setKey(0, parentKey);
						y.setChild(0, siblingChild);
						println("x after swap: " + x.toString());
						println("y after swap: " + y.toString());
						println("z after swap: " + z.toString());
					} else if (i + 1 < x.n && x.child(i + 1).n >= MIN_DEGREE) {
						int j = i + 1;
						println("Swapping with right sibling: " + j);
						Key parentKey = x.key(i);
						Node<T, V> z = x.child(j);
						Key siblingKey = z.key(0);
						Node<T, V> siblingChild = z.child(0);
						// shift all keys of z one step to left
						// after extracting the first key to fill the gap
						leftShiftKeys(z);
						// shift all children of z one step to left
						// after extracting the first child to fill the gap
						leftShiftChildren(z);
						z.n--;
						print("Swapping parent key: " + parentKey.key);
						println(" with sibling key: " + siblingKey.key);
						x.setKey(i, siblingKey);
						y.setKey(y.n, parentKey);
						y.setChild(y.n + 1, siblingChild);
						y.n++;
					} else {
						if (i - 1 >= 0) {
							mergeChildren(x, i - 1, i);
							// the key to detele is not in the left sibling
							// after merging y with left child z = x[i - 1]
							i--;
							println("After merge: " + x.toString());
						} else if (i + 1 < x.n) {
							mergeChildren(x, i, i + 1);
						} else {
							println("ERROR: x has only one child!");
							return false;
						}
					}
				}
				println("Recursting to x[" + i + "] child ...");
				return deleteNonEmpty(x.child(i), k);
			} else {
				// if x is a leaf then k is not in the tree because
				// x has no children
				println("Delete failed: Key was not found " + k);
				return false;
			}
		}
		return true;
	}

	private void mergeChildren(Node<T, V> x, int i, int j) {
		println("Merging x[" + i + "] and x[" + j + "]: ");
		Node<T, V> y = x.child(i);
		Node<T, V> z = x.child(j);
		println(y.toString());
		println(z.toString());
		Key k = x.key(i);
		int m = y.n;
		// add k to y keys
		y.setKey(m, k);
		m++;
		// merget z keys into y
		for (int q = 0; q < z.n; q++) {
			y.setKey(m + q, z.key(q));
		}
		// merget z children into y
		for (int q = 0; q <= z.n; q++) {
			y.setChild(m + q, z.child(q));
		}
		// delete key k from x and shift keys on the right
		// one step to the left
		leftShiftKeys(x, i);
		// delete z from x and shift children on the right 
		// one step to the left
		leftShiftChildren(x, i + 1);
		x.n--;
		// number of keys of y increased by z keys and k
		// should be 2 * MIN_DEGREE - 1
		y.n += z.n + 1;
	}

	public Key<T, V> extractPredecessor(Node<T, V> x, int i) {
		Node<T, V> last = x.child(i);
		while (!x.isLeaf) {
			x = x.child(x.n);
		}
		Key<T, V> predecessor = x.key(x.n - 1);
		deleteNonEmpty(x, predecessor.key);
		return predecessor;
	}

	public Key<T, V> extractSuccessor(Node<T, V> x, int i) {
		Node<T, V> last = x.child(i);
		while (!x.isLeaf) {
			x = x.child(0);
		}
		Key<T, V> successor = x.key(0);
		deleteNonEmpty(x, successor.key);
		return successor;
	}

	public int size() {
		return num;
	}
	 
	public int height() {
		return height;
	}

	private boolean greater(T k, Key<T, V> x) {
		return k.compareTo(x.key) > 0;
	}

	private boolean lesser(T k, Key<T, V> x) {
		return k.compareTo(x.key) < 0;
	}

	private boolean equals(T k, Key<T, V> x) {
		return k.compareTo(x.key) == 0;
	}

	private void leftShiftKeys(Node<T, V> x) {
		leftShiftKeys(x, 0);
	}

	private void leftShiftKeys(Node<T, V> x, int fromIndex) {
		for (int i = fromIndex; i < x.n - 1; i++) {
			x.setKey(i, x.key(i + 1));
		}
		x.setKey(x.n - 1, null);
	}

	private void leftShiftChildren(Node<T, V> x) {
		leftShiftChildren(x, 0);
	}

	private void leftShiftChildren(Node<T, V> x, int fromIndex) {
		for (int i = fromIndex; i < x.n; i++) {
			x.setChild(i, x.child(i + 1));
		}
		x.setChild(x.n, null);
	}

	private void rightShiftKeys(Node<T, V> x) {
		rightShiftKeys(x, 0);
	}

	private void rightShiftKeys(Node<T, V> x, int fromIndex) {
		for (int j = x.n - 1; j >= fromIndex; j--) {
			x.setKey(j + 1, x.key(j));
		}
		x.setKey(fromIndex, null);
	}

	private void rightShiftChildren(Node<T, V> x) {
		rightShiftChildren(x, 0);
	}

	private void rightShiftChildren(Node<T, V> x, int fromIndex) {
		for (int j = x.n; j >= fromIndex; j--) {
			x.setChild(j + 1, x.child(j));
		}
		x.setChild(fromIndex, null);
	}

	private static void println(String s) {
		System.out.println(s);
	}

	private static void println() {
		System.out.print('\n');
	}

	private static void print(String s) {
		System.out.print(s);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Nodes: " + num + "\n");
		builder.append(root.toString());
		return builder.toString();
	}

	public static void main(String[] args) {
		BTree<String, String> st = new BTree<String, String>(4); 
        //st.put("www.cs.princeton.edu", "128.112.136.12");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu",    "128.112.128.15");
        st.put("www.yale.edu",         "130.132.143.21");
        st.put("www.simpsons.com",     "209.052.165.60");
        st.put("www.apple.com",        "17.112.152.32");
        st.put("www.amazon.com",       "207.171.182.16");
        st.put("www.ebay.com",         "66.135.192.87");
        st.put("www.cnn.com",          "64.236.16.20");
        st.put("www.google.com",       "216.239.41.99");
        st.put("www.nytimes.com",      "199.239.136.200");
        st.put("www.microsoft.com",    "207.126.99.140");
        st.put("www.dell.com",         "143.166.224.230");
        st.put("www.slashdot.org",     "66.35.250.151");
        st.put("www.espn.com",         "199.181.135.201");
        st.put("www.weather.com",      "63.111.66.11");
        st.put("www.yahoo.com",        "216.109.118.65");


        println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
        println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
        println("simpsons.com:      " + st.get("www.simpsons.com"));
        println("apple.com:         " + st.get("www.apple.com"));
        println("ebay.com:          " + st.get("www.ebay.com"));
        println("dell.com:          " + st.get("www.dell.com"));
        println();
        println("size:    " + st.size());
        println("height:  " + st.height());
        println(st.toString());
        println();

		st.delete("www.nytimes.com");
		println(st.toString());

		st.delete("www.cs.princeton.edu");
		println(st.toString());

		st.delete("www.microsoft.com");
		println(st.toString());

		st.delete("www.espn.com");
		println(st.toString());

		st.delete("www.slashdot.org");
		println(st.toString());

		st.delete("www.weather.com");
		println(st.toString());

		st.delete("www.yale.edu");
		println(st.toString());

		st.delete("www.simpsons.com");
		println(st.toString());

		st.delete("www.princeton.edu");
		println(st.toString());

		st.delete("www.cnn.com");
		println(st.toString());

        println("ebay.com:          " + st.get("www.ebay.com"));
	}
}
