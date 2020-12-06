package sample;

import java.io.Serializable;

public class Data implements Serializable {
    String username = null;
    String password = null;

    Data(String username, String password){
        this.username = username;
        this.password = password;
    }
}
