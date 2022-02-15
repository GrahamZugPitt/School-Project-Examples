/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Graham Zug
 */
public class HashNode {
    private String info;
    private int position;
    private HashNode next;
    private boolean hasNext = false;
    
    public HashNode(String info, int position){
        this.info = info;
        this.position = position;
        this.next = null;
        this.hasNext = true;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
        public HashNode(){
        this.info = "Open";
        this.position = -1;
        this.next = null;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public HashNode getNext() {
        return next;
    }

    public void setNext(HashNode next) {
        this.next = next;
    }
}
