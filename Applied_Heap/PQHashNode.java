/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Graham Zug
 */
public class PQHashNode {
    private String info;
    private IndexMinPQ theHeap;
    private PQHashNode next;
    private boolean hasNext = false;
    
    public PQHashNode(){
        this.info = "Open";
        this.theHeap = null;
        this.next = null;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    
    public PQHashNode(String info, IndexMinPQ heap){
        this.info = "Open";
        this.theHeap = heap;
        this.next = new PQHashNode();
        this.hasNext = true;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public IndexMinPQ getTheHeap() {
        return theHeap;
    }

    public void setTheHeap(IndexMinPQ theHeap) {
        this.theHeap = theHeap;
    }

    public PQHashNode getNext() {
        return next;
    }

    public void setNext(PQHashNode next) {
        this.next = next;
    }
    
    
}
