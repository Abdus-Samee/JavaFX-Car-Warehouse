package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML private Button view;
    @FXML private Button add;
    @FXML private Button edit;
    @FXML private Button del;
    private network net;
    private Option option;

    @FXML
    public void viewCar() throws Exception{
        net.viewALlCar();
    }

    @FXML
    public void addCar() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addCar.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AddCar controller = loader.getController();
        controller.setNet(this.net);

        net.setAddCarController(controller);
        Stage mainStage = (Stage)add.getScene().getWindow();
        mainStage.setTitle("Add Car");
        mainStage.setScene(new Scene(root));
        mainStage.show();
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
                System.out.println(net.username);

                ViewCar controller = loader.getController();
                controller.setNet(net);
                controller.setOption(option);
                controller.init(false);
//                System.out.println(option.getCarList());
//                net.setViewCarController(controller);
                Stage mainStage = (Stage)view.getScene().getWindow();
                mainStage.setTitle("All Cars");
                mainStage.setScene(new Scene(root));
                mainStage.show();
            }
        });
    }

    public void setNet(network net) { this.net = net; }
}
