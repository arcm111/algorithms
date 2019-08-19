class Vertex implements VertexInterface {
    public final int v;

    public Vertex(Integer v) {
        this.v = v;
    }

    @Override
    public int getVertex() {
        return v;
    }

    @Override
    public String toString() {
        return Integer.toString(v);
    }
}
