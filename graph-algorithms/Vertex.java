public class Vertex implements VertexInterface {
    private static final int NIL = -1;
    private static final int INFINITY = Integer.MAX_VALUE;
    private static final Vertex NullVertex = new Vertex(-1);

    public final int v;
    public Colour colour = Colour.WHITE;
    public int d = INFINITY;
    public Vertex pi = NullVertex;

    public static enum Colour {
        WHITE,
        GREY,
        BLACK
    }

    public Vertex(int v) {
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
        String dis = (d == INFINITY) ? "inf" : Integer.toString(d);
        return String.format("(%d):[%d]:[%s]:[%s]", v, pi.v, dis, c);
    }
}
