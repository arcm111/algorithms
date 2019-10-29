public class FlowNetworkVertex implements VertexInterface {
    private final int index;
    private int parent;

    public FlowNetworkVertex(int index) {
        this.index = index;
    }

    public int getVertex() {
        return index;
    }

    public int getIndex() {
        return index;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String toString() {
        return "[" + index + "]<-[" + parent + "]";
    }
}
