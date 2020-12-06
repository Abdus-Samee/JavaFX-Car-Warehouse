package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Car Management System");
        primaryStage.setScene(new Scene(root));

        Socket s = new Socket("127.0.0.1",8888);
        Login controller = loader.getController();
        network net = new network(s, controller);
        controller.setNet(net);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
