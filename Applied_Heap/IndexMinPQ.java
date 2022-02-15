/******************************************************************************
 *  Compilation:  javac IndexMinPQ.java
 *  Execution:    java IndexMinPQ
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.Math;




public class IndexMinPQ {
    private int currentN;        // maximum number of elements on PQ
    private int n;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Car[] cars;      // keys[i] = priority of i
    private int compareType; //tells whether tree height is based on car price or car mileage. 0 implies a price comparison. 1 implies a mileage comparison.

   
    public IndexMinPQ(int startingN, int compareType) {
        this.currentN = startingN;
        n = 0;
        cars = new Car[startingN + 1];    
        pq   = new int[startingN + 1];
        qp   = new int[startingN + 1];                   
        for (int i = 0; i <= startingN; i++)
            qp[i] = -1;
        this.compareType = compareType;
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     *
     * @param  i an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < currentN}
     */
    public boolean contains(int i) {
        if (i < 0 || i >= this.currentN) throw new IllegalArgumentException();
        return qp[i] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return n;
    }

    /**
     * Associates key with index {@code i}.
     *
     * @param  i an index
     * @param car the key to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < currentN}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
     */
    public void insert(int i, Car car, HashNode[] carsByVIM, PQHashNode[] carHeaps, boolean bigTree, boolean isPriceTree) {
        if (i < 0) throw new IllegalArgumentException();
        if(i == currentN){
            Car[] storageArray = new Car[currentN];
            currentN = currentN * 2;
        for(int j = 0; j < storageArray.length; j++){
                storageArray[j] = cars[j];
            }
        cars = new Car[currentN];
        for(int j = 0; j < storageArray.length; j++){
                cars[j] = storageArray[j];
            }
            int[] integerStorageArray = new int[pq.length];
        for(int j = 0; j < pq.length; j++){
                integerStorageArray[j] = pq[j];
            }
        qp = new int[currentN];
        for(int j = 0; j < integerStorageArray.length; j++){
                qp[j] = integerStorageArray[j];
            }
        for (int j = storageArray.length; j < qp.length; j++)
            qp[j] = -1;
        pq = new int[currentN];
        for(int j = 0; j < integerStorageArray.length; j++){
                pq[j] = integerStorageArray[j];
            }
            for(int j = 0; j < integerStorageArray.length; j++){
                integerStorageArray[j] = qp[j];
            }
        }
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        if(bigTree){
        int theHasher = 0;
        
        int forHandlingAbnormallyShortVIMS = 5;
        if (car.getVINnumber().length() < 5){
            forHandlingAbnormallyShortVIMS = car.getVINnumber().length();
        }
        for(int k = 0; k < forHandlingAbnormallyShortVIMS; k++){
            theHasher = (int) (theHasher + car.getVINnumber().charAt(k) * Math.pow(36.0, (double) k));
        }
        theHasher = theHasher % carsByVIM.length;
        boolean didInsert = false;
        HashNode theHashNode = carsByVIM[theHasher];
        
        while(!didInsert){
        if(theHashNode.getInfo().equals("Open") && theHashNode.isHasNext() == false){
            theHashNode.setInfo(car.getVINnumber());
            theHashNode.setPosition(i);
            theHashNode.setNext(new HashNode());
            didInsert = true;
        } else
            theHashNode = theHashNode.getNext();
        }
        String make;
        String model;
        if(car.getMake().length() < 2){
            make = car.getMake();
        } else {
            make = car.getMake().charAt(0) + "" + car.getMake().charAt(1);
        }
        if(car.getModel().length() < 3){
            model = car.getModel();
        } else {
            model = car.getModel().charAt(0) + "" + car.getModel().charAt(1) + "" + car.getModel().charAt(2);
        }
        String makeAndModel = make + model;
        theHasher = 0;
        for(int k = 0; k < makeAndModel.length(); k++){
            theHasher = (int) (theHasher + makeAndModel.charAt(k) * Math.pow(26.0, (double) k));
        }
        theHasher = theHasher % carHeaps.length;
        
        didInsert = false;
        PQHashNode thePQHashNode = carHeaps[theHasher];
        while(!didInsert){
        if(thePQHashNode.getInfo().equals("Open") && thePQHashNode.isHasNext() == false){
            thePQHashNode.setInfo(car.getMake() + car.getModel());
            if(isPriceTree){
            thePQHashNode.setTheHeap(new IndexMinPQ(20, 0));
            thePQHashNode.getTheHeap().insert(i, car, carsByVIM, carHeaps, false, isPriceTree);
            } else {
            thePQHashNode.setTheHeap(new IndexMinPQ(20, 1));
            thePQHashNode.getTheHeap().insert(i, car, carsByVIM, carHeaps, false, isPriceTree);
            }
            thePQHashNode.setNext(new PQHashNode());
            thePQHashNode.setHasNext(true);
            didInsert = true;
        } else if(thePQHashNode.getInfo().equals(car.getMake() + car.getModel())){
          
            thePQHashNode.getTheHeap().insert(i, car, carsByVIM, carHeaps, false, isPriceTree);
            didInsert = true;
        }else
            thePQHashNode = thePQHashNode.getNext();
        }
        }
        
        n++;
        qp[i] = n;
        pq[n] = i;
        cars[i] = car;
        swim(n);
    }

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Car minCar() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return cars[pq[1]];
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];
        exch(1, n--);
        sink(1);
        assert min == pq[n+1];
        qp[min] = -1;        // delete
        cars[min] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed
        return min;
    }

    /**
     * Returns the key associated with index {@code i}.
     *
     * @param  i the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < currentN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public Car carOf(int i) {
        if (i < 0 || i >= currentN) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return cars[i];
    }

   
    public void changeKey(int i, Car car) {
        if (i < 0 || i >= currentN) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        cars[i] = car;
        swim(qp[i]);
        sink(qp[i]);
    }

    /**
     * Remove the key associated with index {@code i}.
     *
     * @param  i the index of the key to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < currentN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void delete(int i) {
        if (i < 0 || i >= currentN) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        cars[i] = null;
        qp[i] = -1;
    }


   /***************************************************************************
    * General helper functions.
    ***************************************************************************/
    private boolean greater(int i, int j) {
        if(compareType == 0){
            return cars[pq[i]].getPrice() > cars[pq[j]].getPrice();
        }
        if(compareType == 1){
            return cars[pq[i]].getMileage() > cars[pq[j]].getMileage();
        }
        throw new IllegalArgumentException("You aren't supposed to get here! The compare type is wrong somehow.");
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }


   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }



   

    
}

