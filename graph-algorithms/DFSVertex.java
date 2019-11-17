public class DFSVertex extends Vertex {
    public static final int NIL = -1;
    public static final DFSVertex NullVertex = new DFSVertex(-1);
    public Colour colour = Colour.WHITE; // vertex colour
    public DFSVertex pi = NullVertex; // parent vertex
    public int d = NIL; // discovery time
    public int f = NIL; // finishing time

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
        super(v);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof DFSVertex)) return false;
        if (obj == this) return true;
        
        final DFSVertex that = (DFSVertex) obj;
        return this.v == that.v;
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
