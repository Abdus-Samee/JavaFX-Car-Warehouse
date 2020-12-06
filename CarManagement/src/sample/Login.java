package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML private Button admin;
    @FXML private Button viewer;
    @FXML private TextField username;
    @FXML private PasswordField password;
    private network net;

    @FXML
    public void loginAsAdmin() throws Exception{
        String user = this.username.getText();
        String pass = this.password.getText();
        System.out.println(pass);
        Data data = new Data(user, pass);
//        net.setLoginController(this);
        net.write(data);
    }

    @FXML
    public void loginAsViewer() throws Exception{
        net.setUsername("default");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        System.out.println(net.username);

        Stage mainStage = (Stage)admin.getScene().getWindow();
        mainStage.setTitle("Main Menu");
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    public void showMainMenu(Data data) throws Exception{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                net.setUsername(data.username);

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
                Stage mainStage = (Stage)admin.getScene().getWindow();
                mainStage.setTitle("Main Menu");
                mainStage.setScene(new Scene(root));
                mainStage.show();
            }
        });
    }

    public void setNet(network net) { this.net = net; }
}
