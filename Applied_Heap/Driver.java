/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author Graham Zug
 */
public class Driver {
    public static void main(String[] args) throws IOException {
        IndexMinPQ carsByPrice = new IndexMinPQ(20, 0);
        IndexMinPQ carsByMileage = new IndexMinPQ(20, 1);
        HashNode[] carsByVIM = new HashNode[997];
        
        for(int i = 0; i < carsByVIM.length; i++){
            carsByVIM[i] = new HashNode();
        }
        
        PQHashNode[] makesAndModelsByPrice = new PQHashNode[997];
        
        for(int i = 0; i < makesAndModelsByPrice.length; i++){
            makesAndModelsByPrice[i] = new PQHashNode();
        }
        
        PQHashNode[] makesAndModelsByMileage = new PQHashNode[997];
        
        for(int i = 0; i < makesAndModelsByMileage.length; i++){
            makesAndModelsByMileage[i] = new PQHashNode();
        }
        
        File cars = new File("cars.txt");
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(cars));
                String line=reader.readLine();
                line=reader.readLine();
                int j = 1;
                while(line != null) {
                    String vin = "";
                    String make = "";
                    String model = "";
                    String price = "";
                    String mileage = "";
                    String color = "";
                    int i = 0;
                    while(line.charAt(i) != ':'){
                        vin = vin + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ':'){
                        make = make + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ':'){
                        model = model + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ':'){
                        price = price + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(line.charAt(i) != ':'){
                        mileage = mileage + line.charAt(i);
                        i++;
                    }
                    i++;
                    while(i < line.length()){
                        color = color + line.charAt(i);
                        i++;
                    }
                    carsByPrice.insert(j, new Car(vin, make, model, Integer.parseInt(price), Integer.parseInt(mileage), color), carsByVIM, makesAndModelsByPrice, true, true);
                    carsByMileage.insert(j, new Car(vin, make, model, Integer.parseInt(price), Integer.parseInt(mileage), color), carsByVIM, makesAndModelsByMileage, true, false);
                    j++;
                    line=reader.readLine();
                    } 
                
        String input = "";
        boolean exiter = false;
        while(!exiter){
            System.out.println("What do you wish to do?");
            System.out.println("0. Exit");
            System.out.println("1. Add a car");
            System.out.println("2. Update a car");
            System.out.println("3. Remove a specific car");
            System.out.println("4. Retrieve the lowest price car");
            System.out.println("5. Retrieve the lowest mileage car");
            System.out.println("6. Retrieve the lowest price car of a specified make and model");
            System.out.println("7. Retrieve the lowest mileage car of a specified make and model");
            System.out.println();
            Scanner s = new Scanner(System.in); 
            input = s.nextLine();
            if(input.equals("0"))
                exiter = true;
            if(input.equals("1")){
                System.out.println();
                System.out.println("What is the car's VIN number?");
                String vin = s.nextLine();
                System.out.println("What is the car's make?");
                String make = s.nextLine();
                System.out.println("What is the car's model?");
                String model = s.nextLine();
                System.out.println("What is the car's price?");
                String price = s.nextLine();
                System.out.println("What is the car's mileage?");
                String mileage = s.nextLine();
                System.out.println("What is the car's color?");
                String color = s.nextLine();
                System.out.println();
                carsByPrice.insert(j, new Car(vin, make, model, Integer.parseInt(price), Integer.parseInt(mileage), color), carsByVIM, makesAndModelsByPrice, true, true);
                carsByMileage.insert(j, new Car(vin, make, model, Integer.parseInt(price), Integer.parseInt(mileage), color), carsByVIM, makesAndModelsByMileage, true, false);
                j++;
            } else if (input.equals("2")){
                System.out.println();
                System.out.println("What is the car's VIN number?");
                String vin = s.nextLine();
                
                int theHasher = 0;
                int forHandlingAbnormallyShortVIMS = 5;
                if (vin.length() < 5){
                     forHandlingAbnormallyShortVIMS = vin.length();
                 }
                for(int k = 0; k < forHandlingAbnormallyShortVIMS; k++){
                    theHasher = (int) (theHasher + vin.charAt(k) * Math.pow(36.0, (double) k));
                 }
                
                theHasher = theHasher % carsByVIM.length;
                boolean didFind = false;
                boolean cantFind = false;
                HashNode theHashNode = carsByVIM[theHasher];
        
                while(!didFind && !cantFind){
                if(theHashNode.getInfo().equals("Open") && theHashNode.isHasNext() == false){
                    System.out.println("No car in our data has such a VIM number!");
                    cantFind = true;
                } else if(theHashNode.getInfo().equals(vin)){
                    System.out.println("What do you wish to update?");
                    input = "0";
                    while(!input.equals("1") && !input.equals("2") && !input.equals("3")){  
                    System.out.println("1. Price");
                    System.out.println("2. Mileage");
                    System.out.println("3. Color");
                    System.out.println();
                    input = s.nextLine();
                    if(!input.equals("1") && !input.equals("2") && !input.equals("3")){
                        System.out.println("Please enter a valid number");
                        System.out.println();
                    }
                    }
                    Car car = new Car(carsByPrice.carOf(theHashNode.getPosition()));
                    System.out.println();
                    if(input.equals("1")){
                        System.out.println("What is the new price?");
                        input = s.nextLine();
                        car.setPrice(Integer.parseInt(input));
                    } else if(input.equals("2")){
                        System.out.println("What is the new mileage?");
                        input = s.nextLine();
                        car.setMileage(Integer.parseInt(input));
                    } else if(input.equals("3")){
                        System.out.println("What is the new color?");
                        input = s.nextLine();
                        car.setColor(input);
                    }
                    carsByPrice.changeKey(theHashNode.getPosition(), car);
                    carsByMileage.changeKey(theHashNode.getPosition(), car);
                    didFind = true;
                } else
                    theHashNode = theHashNode.getNext();
                }  
                
                if(didFind){
                    Car car = new Car(carsByPrice.carOf(theHashNode.getPosition()));
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
                    theHasher = theHasher % makesAndModelsByPrice.length;
                    didFind = false;
                    PQHashNode thePQHashNode = makesAndModelsByPrice[theHasher];
                    PQHashNode thePQHashNode2 = makesAndModelsByMileage[theHasher];
                    while(!didFind){
                    if(thePQHashNode.getInfo().equals(car.getMake() + car.getModel())){
                        thePQHashNode.getTheHeap().changeKey(theHashNode.getPosition(), new Car(car));
                        thePQHashNode2.getTheHeap().changeKey(theHashNode.getPosition(), new Car(car));
                        didFind = true;
                        System.out.println();
                    }else
                    thePQHashNode = thePQHashNode.getNext();
                    }
                          
                }
                
                
                
                
            } else if (input.equals("3")){
                System.out.println();
                System.out.println("What is the car's VIN number?");
                String vin = s.nextLine();
                
                int theHasher = 0;
                int forHandlingAbnormallyShortVIMS = 5;
                if (vin.length() < 5){
                     forHandlingAbnormallyShortVIMS = vin.length();
                 }
                for(int k = 0; k < forHandlingAbnormallyShortVIMS; k++){
                    theHasher = (int) (theHasher + vin.charAt(k) * Math.pow(36.0, (double) k));
                 }
                
                theHasher = theHasher % carsByVIM.length;
                boolean didFind = false;
                boolean cantFind = false;
                Car car = null;
                HashNode theHashNode = carsByVIM[theHasher];
                while(!didFind && !cantFind){
                if(theHashNode.getInfo().equals("Open") && theHashNode.isHasNext() == false){
                    System.out.println("No car in our data has such a VIM number!");
                    cantFind = true;
                } else if(theHashNode.getInfo().equals(vin)){
                    car = new Car((carsByPrice.carOf(theHashNode.getPosition())));
                    System.out.println();
                    theHashNode.setInfo("Open");
                    carsByPrice.delete(theHashNode.getPosition());
                    carsByMileage.delete(theHashNode.getPosition());
                    didFind = true;
                } else
                    theHashNode = theHashNode.getNext();
                }
                if(didFind){
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
                    theHasher = theHasher % makesAndModelsByPrice.length;
                    didFind = false;
                    PQHashNode thePQHashNode = makesAndModelsByPrice[theHasher];
                    PQHashNode thePQHashNode2 = makesAndModelsByMileage[theHasher];
                    while(!didFind){
                    if(thePQHashNode.getInfo().equals(car.getMake() + car.getModel())){
                        thePQHashNode.getTheHeap().delete(theHashNode.getPosition());
                        thePQHashNode2.getTheHeap().delete(theHashNode.getPosition());
                        didFind = true;
                        System.out.println("The car has been removed");
                        System.out.println();
                    }else
                    thePQHashNode = thePQHashNode.getNext();
                    }
                          
                }
            } else if (input.equals("4")){
                System.out.println();
                System.out.println("The current lowest price car is a " + carsByPrice.minCar().getColor() + " " + carsByPrice.minCar().getMake() + " " + carsByPrice.minCar().getModel() + ".");
                System.out.println("It has a VIN number of " + carsByPrice.minCar().getVINnumber() + ", it has a price of " + carsByPrice.minCar().getPrice() + ", and it has " + carsByPrice.minCar().getMileage() + " miles on it.");
                System.out.println();
            } else if (input.equals("5")){
                System.out.println();
                System.out.println("The current lowest mileage car is a " + carsByMileage.minCar().getColor() + " " + carsByMileage.minCar().getMake() + " " + carsByMileage.minCar().getModel() + ".");
                System.out.println("It has a VIN number of " + carsByMileage.minCar().getVINnumber() + ", it has a price of " + carsByMileage.minCar().getPrice() + ", and it has " + carsByMileage.minCar().getMileage() + " miles on it.");
                System.out.println();
                
            } else if (input.equals("6")){
                System.out.println();
                System.out.println("What is the car's make?");
                String make = s.nextLine();
                System.out.println("What is the car's model?");
                String model = s.nextLine();
                
                Car car = new Car("Brick", make, model, 0, 0, "fast");
                int theHasher = 0;
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
                    theHasher = theHasher % makesAndModelsByPrice.length;
                    boolean didFind = false;
                    PQHashNode thePQHashNode = makesAndModelsByPrice[theHasher];
                    
                    while(!didFind){
                    if(thePQHashNode.getInfo().equals(car.getMake() + car.getModel()) && !thePQHashNode.getTheHeap().isEmpty()){
                        car = thePQHashNode.getTheHeap().minCar();
                        System.out.println("The current lowest price " + car.getMake() + " " + car.getModel() + " " + "is " + car.getColor() + ".");
                        System.out.println("It has a VIN number of " + car.getVINnumber() + ", it has a price of " + car.getPrice() + ", and it has " + car.getMileage() + " miles on it.");
                        System.out.println();
                        didFind = true;
                    }else if (thePQHashNode.isHasNext()){
                    thePQHashNode = thePQHashNode.getNext();
                    } else 
                            System.out.println("No car with such a make or model found!");
                            System.out.println();
                            didFind = true;
                    }
                
                
            } else if (input.equals("7")){
                System.out.println();
                System.out.println();
                System.out.println("What is the car's make?");
                String make = s.nextLine();
                System.out.println("What is the car's model?");
                String model = s.nextLine();
                
                Car car = new Car("Brick", make, model, 0, 0, "fast");
                int theHasher = 0;
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
                    theHasher = theHasher % makesAndModelsByPrice.length;
                    boolean didFind = false;
                    PQHashNode thePQHashNode = makesAndModelsByMileage[theHasher];
                    
                    while(!didFind){
                    if(thePQHashNode.getInfo().equals(car.getMake() + car.getModel()) && !thePQHashNode.getTheHeap().isEmpty()){
                        car = thePQHashNode.getTheHeap().minCar();
                        System.out.println("The current lowest mileage " + car.getMake() + " " + car.getModel() + " " + "is " + car.getColor() + ".");
                        System.out.println("It has a VIN number of " + car.getVINnumber() + ", it has a price of " + car.getPrice() + ", and it has " + car.getMileage() + " miles on it.");
                        System.out.println();
                        didFind = true;
                    }else if (thePQHashNode.isHasNext()){
                    thePQHashNode = thePQHashNode.getNext();
                    } else 
                            System.out.println("No car with such a make or model found");
                            System.out.println();
                            didFind = true;
                    }
                
            } else if (!input.equals("0")){
            System.out.println();
            System.out.println("Invalid Entry");
            System.out.println();
            }
             
        }
        System.out.println();
        System.out.println("Goodbye!");
    
    }
}
