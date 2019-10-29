public class BFSVertex extends Vertex {
    public static final int NIL = -1;
    public static final BFSVertex NullVertex = new BFSVertex(NIL);
    private static final int INFINITY = Integer.MAX_VALUE;
    public Colour colour = Colour.WHITE;
    public int d = INFINITY;
    public BFSVertex pi = NullVertex;

    public static enum Colour {
        WHITE,
        GREY,
        BLACK
    }

    /**
     * Constructor. The parameter <em>v</em> must be of type {@code Integer}
     * so that {@code Graph} class can instantiate the vertices through its
     * constructor {@code Graph(class<T> C, int V)}. This requirement is due
     * to this line:
     * {@code c.getDeclaredConstructor(Integer.class).newInstance(i)} in
     * the above mentioned constructor.
     * @param v the vertex value.
     */
    public BFSVertex(Integer v) {
        super(v);
    }

    @Override
    public String toString() {
        String c;
        if (colour == Colour.WHITE) c = "W";
        else if (colour == Colour.GREY) c = "G";
        else c = "B";
        String dis = (d == INFINITY) ? "inf" : Integer.toString(d);
        return String.format("(%d):[%d][%s:%s]", v, pi.v, dis, c);
    }
}
