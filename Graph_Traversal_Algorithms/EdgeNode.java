
/**
 *
 * @author Graham Zug
 */
public class EdgeNode {
    private final int _verticeTo;
    private final Edge _edge;
    private EdgeNode _next;
    
    public EdgeNode(int vertice, Edge edge){
        _verticeTo = vertice;
        _edge = edge;
    }

    public void setNext(EdgeNode _next) {
        this._next = _next;
    }

    public int getVerticeTo() {
        return _verticeTo;
    }

    public Edge getEdge() {
        return _edge;
    }

    public EdgeNode getNext() {
        return _next;
    }
    
    
}
