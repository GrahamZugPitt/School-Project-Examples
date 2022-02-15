/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Graham Zug
 */
public class Car {
    private String VINnumber;
    private String make;
    private String model;
    private int price;
    private int mileage;
    private String color;
    
    public Car(){
        this.VINnumber = "Unspecified";
        this.make = "Unspecified";
        this.model = "Unspecified";
        this.price = -1;
        this.mileage = -1;
        this.color = "Unspecified";
    }
    
    public Car(String VINnumber, String make, String model, int price, int mileage, String color){
        this.VINnumber = VINnumber;
        this.make = make;
        this.model = model;
        this.price = price;
        this.mileage = mileage;
        this.color = color;
    }
    
    public Car(Car car){
        this.VINnumber = car.VINnumber;
        this.make = car.make;
        this.model = car.model;
        this.price = car.price;
        this.mileage = car.mileage;
        this.color = car.color;
    }

    public String getVINnumber() {
        return VINnumber;
    }

    public void setVINnumber(String VINnumber) {
        this.VINnumber = VINnumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
}
