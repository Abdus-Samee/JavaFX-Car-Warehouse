package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Option implements Serializable {
    Car car = null;
    private List<Car> carList;
    private Map<String, Integer> mapCarMakeModel = new HashMap<>();
    String command;
    String errorMessage;
    int selectedCarIndex;

    public Option(String command) { this.command = command; }

    public Option(String command, Car car){
        this.command = command;
        this.car = car;
    }

    public Option(String command, Car car, int selectedCarIndex){
        this.command = command;
        this.car = car;
        this.selectedCarIndex = selectedCarIndex;
    }

    public void setCommand(String command) { this.command = command; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getErrorMessage() { return this.errorMessage; }

    public void addToCarList(List<Car> carList){
        this.carList = new ArrayList<>(carList);
    }

    public void addToCarMakeModelMap(Map<String, Integer> mapCarMakeModel) { this.mapCarMakeModel = mapCarMakeModel; }

    public List<Car> getCarList() { return this.carList; }

    public Car getCar() { return this.car; }
}
