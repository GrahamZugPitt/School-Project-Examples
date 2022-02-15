

/**
 *
 * @author Graham Zug
 */

//The graph class
public class Giraffe {
    private final Node[] _vertices;
    private final int _size;
    
    
    //The graph constructor. 
    public Giraffe(int size){
        _size = size;
        _vertices = new Node[size];
        
        for(int i = 0; i < size; i++){
            _vertices[i] = new Node(i); //Initializes the giraffe with no edges
        }
    }

    public Node[] getVertices() {
        return _vertices;
    }

    public int getSize() {
        return _size;
    }
    
}
