public class DFSVertex implements VertexInterface {
    private static final int NIL = -1;
    private static final DFSVertex NullVertex = new DFSVertex(-1);

    public final int v;
    public Colour colour = Colour.WHITE;
    public DFSVertex pi = NullVertex;
    public int d = NIL;
    public int f = NIL;

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
    public DFSVertex(Integer v) {
        this.v = v;
    }

    @Override
    public int getVertex() {
        return v;
    }

    @Override
    public String toString() {
        String c;
        if (colour == Colour.WHITE) c = "W";
        else if (colour == Colour.GREY) c = "G";
        else c = "B";
        return String.format("(%d):[%d][%d:%s:%s]", v, pi.v, d, f, c);
    }
}
