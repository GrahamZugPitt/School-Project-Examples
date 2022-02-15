/**
 *
 * @author Graham Zug
 */

//The edge class
public class Edge {
    private String _wireType;
    private int _bandwidth;
    private int _lengthInMeters;
    private double _latency;
   //The edge constructor 
    public Edge(String wireType, int bandwidth, int lengthInMeters){
        _wireType = wireType;
        _bandwidth = bandwidth;
        _lengthInMeters = lengthInMeters;
        if(wireType.equals("copper")){
            _latency = (double) lengthInMeters/230000000.0;
        }
        if(wireType.equals("optical")){
            _latency = (double) lengthInMeters/200000000.0;
        }
    }
    //getters and setters

    public double getLatency() {
        return _latency;
    }

    public void setLatency(double _latency) {
        this._latency = _latency;
    }
    
    
    
    public String getWireType() {
        return _wireType;
    }

    public void setWireType(String wireType) {
        this._wireType = wireType;
    }

    public int getBandwidth() {
        return _bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this._bandwidth = bandwidth;
    }

    public int getLengthInMeters() {
        return _lengthInMeters;
    }

    public void setLengthInMeters(int lengthInMeters) {
        this._lengthInMeters = lengthInMeters;
    }
    
    
    
}
