package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ViewCar implements Initializable {
    @FXML private Button back;
    @FXML private TextField searchRegistration;
    @FXML private TableView<Car> carTableView;
    @FXML private TableColumn<Car, String> registration;
    @FXML private TableColumn<Car, Integer> year;
    @FXML private TableColumn<Car, String> colors;
    @FXML private TableColumn<Car, String> carMake;
    @FXML private TableColumn<Car, String> carModel;
    @FXML private TableColumn<Car, Integer> price;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> quantity;
    private network net;
    private Option option;
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public void clickItem(MouseEvent event)
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            System.out.println(carTableView.getSelectionModel().getSelectedIndex());
            System.out.println(carTableView.getSelectionModel().getSelectedItem());
            //call another fxml file...
            Car selectedCar = carTableView.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editCar.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditCar editCarController = loader.getController();
            editCarController.setSelectedIndex(carTableView.getSelectionModel().getSelectedIndex());
            editCarController.setNet(net);
            editCarController.init(selectedCar);
            net.setEditCarController(editCarController);
            Stage mainStage = (Stage)back.getScene().getWindow();
            mainStage.setTitle("Edit Car");
            mainStage.setScene(new Scene(root));
            mainStage.show();
        }
    }

    public void setNet(network net) { this.net = net; }

    public void setOption(Option option) { this.option = option; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void init(boolean showAlertDialog) {
        registration.setCellValueFactory(new PropertyValueFactory<Car, String>("Registration"));
        year.setCellValueFactory(new PropertyValueFactory<Car, Integer>("Year"));
        colors.setCellValueFactory(new PropertyValueFactory<Car, String>("ColourStr"));
        carMake.setCellValueFactory(new PropertyValueFactory<Car, String>("CarMake"));
        carModel.setCellValueFactory(new PropertyValueFactory<Car, String>("CarModel"));
        price.setCellValueFactory(new PropertyValueFactory<Car, Integer>("Price"));


        FilteredList<Car> filteredData = new FilteredList<>(getCarData(), b -> true);

        searchRegistration.textProperty().addListener(((observableValue, oldVal, newVal) -> {
            filteredData.setPredicate(car -> {
                if(newVal == null || newVal.isEmpty()) return true;

                if(car.getRegistration().equals(newVal)) return true;

                return false;
            });
        }));

        SortedList<Car> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(carTableView.comparatorProperty());

        carTableView.setItems(sortedData);

        if(showAlertDialog){
            alert.setContentText(this.option.getErrorMessage());
            alert.show();
        }
    }

    private ObservableList<Car> getCarData() {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        for(Car car : option.getCarList()){
            cars.add(car);
        }

        return cars;
    }

    public void goBack(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("sample.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(net.username);

                Controller controller = loader.getController();
                controller.setNet(net);
                net.setController(controller);
                Stage mainStage = (Stage)back.getScene().getWindow();
                mainStage.setTitle("Main Menu");
                mainStage.setScene(new Scene(root));
                mainStage.show();
            }
        });
    }
}
