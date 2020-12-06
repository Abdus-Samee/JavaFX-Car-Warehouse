package sample;

import java.io.Serializable;

public class Car implements Serializable {
    private String registration;
    private int year;
    private String[] colour = new String[3];
    private String carMake;
    private String carModel;
    private int price;

    Car(String registration, int year, String[] colour, String carMake, String carModel, int price){
        this.registration = registration;
        this.year = year;
        this.colour = colour;
        this.carMake = carMake;
        this.carModel  =carModel;
        this.price = price;
    }

    @Override
    public String toString(){
        System.out.println("----------CAR INFO----------");
        String one = "\t| Registration: " + registration + "\n\t| Year: " + year + "\n\t| Colours: ";
        String two = "";
        for(int i = 0; i < colour.length-1; i++) two += colour[i] + " ";
        two += colour[colour.length-1];
        String three = "\n\t| Car Make: " + carMake + "\n\t| Car Model: " + carModel + "\n\t| Price: " + price;
        three += "\n----------------------------\n";

        return one + two + three;
    }

    public String getRegistration() {
        return registration;
    }

    public int getYear() {
        return year;
    }

    public String[] getColour() {
        return colour;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getPrice() {
        return price;
    }

    public String getColourStr() {
        String ans = "";
        for(int i = 0; i < colour.length; i++) ans += colour[i] + " ";
        return ans;
    }
}
