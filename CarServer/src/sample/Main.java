package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.ServerSocket;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ServerSocket ss = new ServerSocket(8888);
        network net = new network(ss);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
