package sample;

import java.io.*;
import java.net.Socket;

public class network{
    private Socket s;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private connThread conn;
    private Login loginController;
    String username;
    private Controller controller;
    private AddCar addCarController;
    private EditCar editCarController;

    public network(Socket s, Login loginController) throws Exception {
        this.s = s;
        this.loginController = loginController;
        this.os = new ObjectOutputStream(s.getOutputStream());
        this.is = new ObjectInputStream(s.getInputStream());
        this.conn = new connThread();
//        fileContainer fc = new fileContainer();
//        fc.setProperty("",this.username,0, null, null);
//        os.writeObject(fc);
        conn.start();
    }

    public void setUsername(String username) { this.username = username; }

    public void setController(Controller controller) { this.controller = controller; }

    public void setAddCarController(AddCar addCarController) { this.addCarController = addCarController; }

    public void setEditCarController(EditCar editCarController) { this.editCarController = editCarController; }

    public void write(Data data) throws Exception{
        os.writeUnshared(data);
    }

    public void viewALlCar() throws Exception{
        os.writeUnshared(new Option("view"));
    }

    public void addCar(Car car) throws Exception{
        os.writeUnshared(new Option("add", car));
    }

    public void editCar(Car car, int index) throws Exception{
        os.writeUnshared(new Option("edit", car, index));
    }

    private class connThread extends Thread{

        @Override
        public void run(){
            while(true){
                try {
                    Object object = is.readUnshared();
                    if(object instanceof Data) loginController.showMainMenu((Data)object);
                    else{
                        Option option = (Option)object;
                        if(option.command.equals("view")) controller.viewAllCarMenu(option);
                        else if(option.command.equals("add")) addCarController.viewAllCarMenu(option);
                        else if(option.command.equals("data-error")) addCarController.viewAllCarMenu(option);
                        else if(option.command.equals("edit")){
                            System.out.println("gotten edit: ");
                            System.out.println(option.getCarList());
                            editCarController.viewAllCarMenu(option);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
