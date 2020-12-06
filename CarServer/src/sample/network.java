package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class network extends Thread{
    private ServerSocket ss;
    private static final String FILE_NAME = "C:\\Users\\Asus\\IdeaProjects\\CarServer\\src\\sample\\cars.txt";
    public static List<Car> carList = new ArrayList<>();
    public static Map<String, Integer> mapCarMakeModel = new HashMap<>();

    public network(ServerSocket ss){
        this.ss = ss;
        fillCarList();

        while (true){
            try{
                Socket s = ss.accept();
                System.out.println("Connected... ... ...");
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                connThread conn = new connThread(s,os,is);
                conn.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void fillCarList(){
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            while (true) {
                line = br.readLine();
                if (line == null) break;
                System.out.println(line);

                //adding car to carList
                String[] properties = line.split(",");
                int l = properties.length;
                String[] colours = new String[3];

                for(int i = 2; i < 5; i++){
                    colours[i-2] = properties[i];
                }

                Car car = new Car(properties[0], Integer.parseInt(properties[1]), colours, properties[l-3], properties[l-2], Integer.parseInt(properties[l-1]));
                carList.add(car);

                if(mapCarMakeModel.containsKey(car.getCarMake()+car.getCarModel())){
                    mapCarMakeModel.put(car.getCarMake()+car.getCarModel(), mapCarMakeModel.get(car.getCarMake()+car.getCarModel())+1);
                }
                else{
                    mapCarMakeModel.put(car.getCarMake()+car.getCarModel(), 1);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public Option verifyCar(Car car, String command){
        Option option;
        String registration = car.getRegistration();
        String carMake = car.getCarMake();
        String carModel = car.getCarModel();
        String reply = "";

        if(registration.contains(" ") || !unique(registration)) reply += "Invalid Registration\n";
        if(carMake.contains(" ")) reply += "Invalid Car Make\n";
        if(carModel.contains(" ")) reply += "Invalid Car Model";

        if(reply.isEmpty() && command.equals("add")){
            option = new Option("add", car);
            return option;
        }else if(reply.isEmpty() && command.equals("edit")){
            option = new Option("edit", car);
            return option;
        }

        option = new Option("data-error");
        option.setErrorMessage(reply);
        return option;
    }

    private boolean unique(String registration) {
        for(Car car : carList){
            if(car.getRegistration().equals(registration)) return false;
        }

        return true;
    }

    synchronized public Option checkEditedCar(Car car, int index){
        Option option;
        String registration = car.getRegistration();
        String carMake = car.getCarMake();
        String carModel = car.getCarModel();
        String reply = "";
        int regFound = -1;

        /**
         * Problem...
         * */
        for(int i = 0; i < carList.size(); i++){
            if(carList.get(i).getRegistration().equals(car.getRegistration())){
                regFound = i;
                break;
            }
        }
        if(registration.contains(" ") || (regFound != index && regFound!=-1)) reply += "Invalid Registration\n";
        if(carMake.contains(" ")) reply += "Invalid Car Make\n";
        if(carModel.contains(" ")) reply += "Invalid Car Model";

        if(reply.isEmpty()){
            option = new Option("edit", car);
            return option;
        }

        option = new Option("data-error");
        option.setErrorMessage(reply);
        return option;
    }

    synchronized public void addEditedCarToList(Car car, int index){
        carList.set(index, car);
        updateFile();
    }

    synchronized public void updateFile(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));

            for(Car car : carList){
                String line = "";
                line += car.getRegistration() + "," + car.getYear() + ",";
                for(String s : car.getColour()) line += s + ",";
                line += car.getCarMake() + "," + car.getCarModel() + "," + car.getPrice();

                bw.write(line);
                bw.write("\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class connThread extends Thread{
        private Socket s;
        private ObjectOutputStream os;
        private ObjectInputStream is;

        public connThread(Socket s, ObjectOutputStream os, ObjectInputStream is){
            this.s = s;
            this.os = os;
            this.is = is;
        }

        /*public void write(String message) throws IOException {
            dos.writeUTF(message);
            dos.flush();
        }*/

        @Override
        public void run(){
            while(true){
                try {
                    Object object = is.readUnshared();

                    if(object instanceof Data){
                        Data data = (Data)object;
                        if(data.password == null){
                            Data sendData = new Data("viewer", null);
                            os.writeUnshared(sendData);
                        }else{
                            String username = data.username;
                            String password = data.password;

                            try {
                                String line;
                                BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Asus\\IdeaProjects\\CarServer\\src\\sample\\admin.txt"));
                                while (true) {
                                    line = br.readLine();
                                    if (line == null) break;
                                    String[] info = line.split(",");
                                    if(info[0].equals(username) && info[1].equals(password)){
                                        Data sendData = new Data(username, password);
                                        os.writeUnshared(sendData);
                                        break;
                                    }
                                }
                                br.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        Option option = (Option) object;
                        if(option.command.equals("view")){
                            option.addToCarList(carList);
                            option.addToCarMakeModelMap(mapCarMakeModel);
                            os.writeUnshared(option);
                        }else if(option.command.equals("add")){
                            Option ans = verifyCar(option.getCar(), "add");
                            if(ans.command.equals("add")){
                                carList.add(option.getCar());
                                updateFile();
                                option.addToCarList(carList);
                                os.writeUnshared(option);
                            }else{
                                System.out.println("Sent");
                                System.out.println(ans);
                                ans.addToCarList(carList);
                                os.writeUnshared(ans);
                            }
                        }else if(option.command.equals("edit")){
                            Option ans = checkEditedCar(option.getCar(), option.selectedCarIndex);
                            if(ans.command.equals("edit")){
                                addEditedCarToList(option.getCar(), option.selectedCarIndex);
                                ans.addToCarList(carList);
                                System.out.println("Edited:\n" + ans.getCar());
                                System.out.println("Sent carList:");
                                System.out.println(ans.getCarList());
                                os.writeUnshared(ans);
                            }//do the data-error...
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
