package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EditCar {
    @FXML private Button edit;
    @FXML private TextField registration;
    @FXML private TextField year;
    @FXML private TextField colours;
    @FXML private TextField carMake;
    @FXML private TextField carModel;
    @FXML private TextField price;
    private Option option;
    private network net;
    private int selectedIndex;

    @FXML
    public void requestCarEdit() throws Exception{
        String reg = registration.getText();
        int yr;
        String clr = colours.getText();
        String car_make = carMake.getText();
        String car_model = carModel.getText();
        int pr;
        Car car;

        String[] colourArr = clr.split(",");

        //data validation...
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(edit.getScene().getWindow());
        if((reg==null) || reg.isEmpty()){
            alert.setContentText("No registration number provided");
            alert.show();
        }else if((year.getText()==null) || year.getText().isEmpty() || invalidInt(year.getText())){
            alert.setContentText("Year field is empty");
            alert.show();
        }else if((clr==null) || clr.isEmpty()){
            alert.setContentText("Proper format for colours not followed");
            alert.show();
        }else if((car_make==null) || car_make.isEmpty()){
            alert.setContentText("Car Make might be empty");
            alert.show();
        }else if((car_model==null) || car_model.isEmpty()){
            alert.setContentText("Car Model field is not filled");
            alert.show();
        }
        else if((price.getText()==null) || price.getText().isEmpty() || invalidInt(price.getText())){
            alert.setContentText("No price given");
            alert.show();
        }
        else{
            yr = Integer.parseInt(year.getText());
            pr = Integer.parseInt(price.getText());
            car = new Car(reg, yr, colourArr, car_make, car_model, pr);
            System.out.println("Sending Car request for... " + selectedIndex    );
            System.out.println(car);
            this.net.editCar(car, this.selectedIndex);
        }
    }

    public void viewAllCarMenu(Option option) throws Exception{
        this.option = option;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("viewCar.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ViewCar controller = loader.getController();
                controller.setNet(net);
                controller.setOption(option);
                controller.init(false);
                Stage mainStage = (Stage)edit.getScene().getWindow();
                mainStage.setTitle("All Cars");
                mainStage.setScene(new Scene(root));
                mainStage.show();
            }
        });
    }


    private boolean invalidInt(String text) {
        try{
            Integer i = Integer.parseInt(text);
            return false;
        }catch (NumberFormatException e){

        }
        return true;
    }

    public void setNet(network net) { this.net = net; }

    public void setSelectedIndex(int selectedIndex) { this.selectedIndex = selectedIndex; }

    public int getSelectedIndex() { return this.selectedIndex; }

    public void init(Car car) {
        registration.setText(car.getRegistration());
        year.setText(String.valueOf(car.getYear()));
        colours.setText(car.getColourStr());
        carMake.setText(car.getCarMake());
        carModel.setText(car.getCarModel());
        price.setText(String.valueOf(car.getPrice()));
    }
}
