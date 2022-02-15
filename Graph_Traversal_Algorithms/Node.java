
/**
 *
 * @author Graham Zug
 */
public class Node {
    private EdgeNode _edgeNode;
    private int _verticeNumber;
 
   Node(int verticeNumber){
       _verticeNumber = verticeNumber;
   }

    public void setEdgeNode(EdgeNode _edgeNode) {
        this._edgeNode = _edgeNode;
    }

    public EdgeNode getEdgeNode() {
        return _edgeNode;
    }

    public int getVerticeNumber() {
        return _verticeNumber;
    }
    
    public Node(EdgeNode edge){
        _edgeNode = edge;
    }
}
