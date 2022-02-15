
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Graham Zug
 */
public class NetworkAnalysis {

    
            
    public static void main(String[] args) throws IOException {
        Giraffe theGiraffe = makeTheGiraffe(args[0]);
        
        
        int userInput = -1;
        while(userInput != 5){
            System.out.println();
                try{
                    userInput = beginning();
                    System.out.println();
                    if(userInput == 0){
                        printTheGiraffee(theGiraffe);
                    }
                    if (userInput == 1){
                        shortestPath(theGiraffe, -1, -1,  -1, 0);
                    }
                    if (userInput == 2){
                        copperOnly(theGiraffe);
                    }
                    if (userInput == 3){
                        minimumSpanningTree(theGiraffe);
                    }
                    if (userInput == 4){
                        staysConnected(theGiraffe);
                    }
            System.out.println();
                } catch(Exception e){
                    System.out.println("Invalid input! Did you enter something that is not a number or a point outside the graph?");
                }
            }
        System.out.println("Goodbye!");
        System.exit(0);
    }
    
    private static Giraffe makeTheGiraffe(String fileName) throws IOException {
                File edges = giraffeFile(fileName);
                BufferedReader reader = giraffeReader(edges);
                String line = reader.readLine();
                int size = Integer.parseInt(line);
                Giraffe giraffe = new Giraffe(size);
                int j = 0;
                line=reader.readLine();
                while(line != null) {
                    
                    String node1 = "";
                    String node2 = "";
                    String wireType = "";
                    String bandwidth = "";
                    String lengthInMeters = "";
                    int i = 0;
                    
                    while(line.charAt(i) != ' '){
                        node1 = node1 + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ' '){
                        node2 = node2 + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ' '){
                        wireType = wireType + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ' '){
                        bandwidth = bandwidth + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(i < line.length()){
                        lengthInMeters = lengthInMeters + line.charAt(i);
                        i++;
                    }
                    int nodeOne = Integer.parseInt(node1);
                    int nodeTwo = Integer.parseInt(node2);
                    
                    Edge edge = new Edge(wireType, Integer.parseInt(bandwidth), Integer.parseInt(lengthInMeters));
                    
                    if(giraffe.getVertices()[nodeOne].getEdgeNode() == null){
                        giraffe.getVertices()[nodeOne].setEdgeNode(new EdgeNode(nodeTwo, edge));
                    } else {
                        EdgeNode edgeNode = giraffe.getVertices()[nodeOne].getEdgeNode();
                        while(edgeNode.getNext() != null){
                            edgeNode = edgeNode.getNext();
                        }
                        edgeNode.setNext(new EdgeNode(nodeTwo, edge));
                    }
                    
                    if(giraffe.getVertices()[nodeTwo].getEdgeNode() == null){
                        giraffe.getVertices()[nodeTwo].setEdgeNode(new EdgeNode(nodeOne, edge));
                    } else {
                        EdgeNode edgeNode = giraffe.getVertices()[nodeTwo].getEdgeNode();
                        while(edgeNode.getNext() != null){
                            edgeNode = edgeNode.getNext();
                        }
                        edgeNode.setNext(new EdgeNode(nodeOne, edge));
                    }
                    
                    line=reader.readLine();
                }
            return giraffe;    
    }
    
    
    private static File giraffeFile(String fileName) throws FileNotFoundException{
         File edges = new File(fileName);
         BufferedReader reader;
         reader = new BufferedReader(new FileReader(edges));
         return edges;
    }
    private static BufferedReader giraffeReader(File edges) throws FileNotFoundException{
         BufferedReader reader;
         reader = new BufferedReader(new FileReader(edges));
         return reader;
    }
    
    private static void printTheGiraffee(Giraffe theGiraffe){
        for(int i = 0; i < theGiraffe.getVertices().length; i++){
                System.out.print(i + " -> ");
                if(theGiraffe.getVertices()[i].getEdgeNode() != null){
                    EdgeNode temp = theGiraffe.getVertices()[i].getEdgeNode();
                    while(temp.getNext() != null){
                        System.out.print(temp.getVerticeTo());
                        System.out.print(" -> ");
                        temp = temp.getNext();
                    }
                    System.out.print(temp.getVerticeTo());
                    System.out.println();
                    
                }
                
        }
    }
    
    public static void minimumSpanningTree(Giraffe theGiraffe){
        System.out.println();
        System.out.println("The tree is:");
        boolean[] inTheGraph = new boolean[theGiraffe.getVertices().length];
        int theNode = -1;
        int theNextNode = -1;
        for(int i = 1; i < theGiraffe.getVertices().length; i++){
            inTheGraph[i] = false;
        }
        inTheGraph[0] = true;
        for(int j = 0; j < theGiraffe.getVertices().length - 1; j++){
            double shortestEdge = Double.MAX_VALUE;
            for(int i = 0; i < theGiraffe.getVertices().length; i++){
                if(inTheGraph[i] == true && theGiraffe.getVertices()[i].getEdgeNode() != null){
                    EdgeNode edgeNode = theGiraffe.getVertices()[i].getEdgeNode();
                    while(edgeNode.getNext() != null){
                    if(!inTheGraph[edgeNode.getVerticeTo()] && shortestEdge > shortestPath(theGiraffe, i, edgeNode.getVerticeTo(), -1, 3)){
                        shortestEdge = shortestPath(theGiraffe, i, edgeNode.getVerticeTo(), -1, 3);
                        theNode = i;
                        theNextNode = edgeNode.getVerticeTo();
                    }
                    edgeNode = edgeNode.getNext();
                    }
                    if(!inTheGraph[edgeNode.getVerticeTo()] && shortestEdge > shortestPath(theGiraffe, i, edgeNode.getVerticeTo(), -1, 3)){
                        shortestEdge = shortestPath(theGiraffe, i, edgeNode.getVerticeTo(), -1, 3);
                        theNode = i;
                        theNextNode = edgeNode.getVerticeTo();
                    }
                }
            } 
            System.out.println(theNode + " -> " + theNextNode);
            inTheGraph[theNextNode] = true;
        }    
    }
    
    public static void staysConnected(Giraffe theGiraffe){
         for(int i = 0; i < theGiraffe.getVertices().length; i++){
                    for(int j = i; j < theGiraffe.getVertices().length; j++){
                        for(int k = 1; k < theGiraffe.getVertices().length; k++){
                            if(k != i && k != j)
                                if(shortestPath(theGiraffe, i, j, k, 2) == -1.0){
                                    System.out.println("The graph will fail if some pair of vertices fails. Two such vertices are: " + i + " " + j);
                                    return;
                                }
                            }
                        }
                    }
         System.out.println("The graph will not fail even if two arbitrary vertices are removed!");
                }
    
    public static void copperOnly(Giraffe giraffe){
        String connected = "The graph is copper-only connected and remains connected considering only copper cables.";
        String notConnected = "The graph is not copper connected and does not remain connected considering only copper cables.";
        for(int i = 1; i < giraffe.getVertices().length; i++){
            if(shortestPath(giraffe, 0, i, -1, 1) == -1.0){
                System.out.print(notConnected);
                return;
            }
        }
        System.out.println(connected);
    }
    
    public static double shortestPath(Giraffe giraffe, int node1, int node2, int node3, int mode){
        double lengthOfPath = 0;
        int[] getHereBy = new int[giraffe.getVertices().length];
        double[] distances = new double[giraffe.getVertices().length];
        boolean[] visited = new boolean[giraffe.getVertices().length];
        for(int i = 0; i < distances.length; i++){
            distances[i] = Double.MAX_VALUE;
            getHereBy[i] = -1;
        }
        Scanner s = new Scanner(System.in); 
        int curVertice;
        int goalVertice;
        int bandwidth = Integer.MAX_VALUE;
        switch (mode) {
            default:
                System.out.println("What vertice are you COMING FROM?");
                goalVertice = s.nextInt();
                System.out.println("What vertice are you GOING TO?");
                curVertice = s.nextInt();
                System.out.println();
                System.out.println("The path is:");
                System.out.println();
                break;
            case 1:
                goalVertice = node1;
                curVertice = node2;
                break;
            case 2:
                curVertice = 0;
                goalVertice = node3;
                break;
            case 3:
                goalVertice = node2;
                curVertice = node1;
        }
        distances[curVertice] = 0;
        getHereBy[curVertice] = -2;
        visited[curVertice] = true;
        
        while(visited[goalVertice] == false){
            if(giraffe.getVertices()[curVertice].getEdgeNode() != null){
                EdgeNode edgeNode = giraffe.getVertices()[curVertice].getEdgeNode();
                while(edgeNode.getNext() != null){
                    if((edgeNode.getEdge().getLatency() + distances[curVertice]) < distances[edgeNode.getVerticeTo()]){
                        if(mode == 0 || mode == 3 || (mode == 1 && edgeNode.getEdge().getWireType().equals("copper")) || (mode == 2 && edgeNode.getVerticeTo() != node1 && edgeNode.getVerticeTo() != node2)){
                        distances[edgeNode.getVerticeTo()] = edgeNode.getEdge().getLatency() + distances[curVertice];
                        getHereBy[edgeNode.getVerticeTo()] = curVertice;
                        }
                    }
                    edgeNode = edgeNode.getNext();
                }
                if((edgeNode.getEdge().getLatency() + distances[curVertice]) < distances[edgeNode.getVerticeTo()]){
                    if(mode == 0 ||  mode == 3 || (mode == 1 && edgeNode.getEdge().getWireType().equals("copper")) || (mode == 2 && edgeNode.getVerticeTo() != node1 && edgeNode.getVerticeTo() != node2)){
                    distances[edgeNode.getVerticeTo()] = edgeNode.getEdge().getLatency() + distances[curVertice];
                    getHereBy[edgeNode.getVerticeTo()] = curVertice;
                    }
                }
            }
                int foundValuePosition = -1;
                double foundValue = Double.MAX_VALUE;
                for(int i = 0; i < distances.length; i++){
                    if (distances[i] < foundValue && visited[i] == false){
                            foundValue = distances[i];
                            foundValuePosition = i;
                    }
                }
                    if(foundValuePosition == -1){
                        return -1.0;
                    }
                    visited[foundValuePosition] = true;
                    curVertice = foundValuePosition;
                    
            }
        
        if(mode == 0 ||  mode == 3){
        int theNode = curVertice;
        int theNextNode = getHereBy[theNode];
        while(theNextNode != -2){
            EdgeNode edgeNode = giraffe.getVertices()[theNode].getEdgeNode();
            while(edgeNode.getVerticeTo() != theNextNode){
                edgeNode = edgeNode.getNext();
            }
            lengthOfPath = lengthOfPath + edgeNode.getEdge().getLatency();
            if(edgeNode.getEdge().getBandwidth() < bandwidth){
                bandwidth = edgeNode.getEdge().getBandwidth();
            }
            if(mode == 0)
                System.out.println(theNode + " -> " + theNextNode);
            theNode = theNextNode;
            theNextNode = getHereBy[theNode];
        }
        if(mode == 0){
            System.out.println();
            System.out.println("With a maximum bandwidth of " + bandwidth + " megabits per second.");
            }
        }
        return lengthOfPath;
        
        }
    
    public static int beginning(){
            System.out.println();
            System.out.println("What do you wish to do?");
            System.out.println();
            System.out.println("0. Print the graph.");
            System.out.println("1. Find the lowest latency path between any two points, and the bandwidth available along that path.");
            System.out.println("2. Determine whether or not the graph is copper-only connected.");
            System.out.println("3. Find the lowest average latency spanning tree for the graph.");
            System.out.println("4. Determine whether or not the graph would remain connected if any two vertices in the graph were to fail.");
            System.out.println("5. Quit.");
            
            Scanner s = new Scanner(System.in); 
            return s.nextInt();
    }
}
