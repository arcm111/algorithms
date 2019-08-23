public interface GraphInterface <T extends VertexInterface> {
    public int V();
    public int E();
    public Iterable<T> adj(T x);
    public void addEdge(int x, int y);
    public void validateVertex(int v);
    public T getVertex(int v);
    public Iterable<T> getVertices();
    public String toString();
}
