package Exercise33_02Client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {
    //IO Streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane borderPane = new BorderPane();
        TextArea textArea = new TextArea();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);

        TextField tfPounds = new TextField();
        TextField tfInches = new TextField();
        Button btnSubmit = new Button("Submit");

        gridPane.add(new Label("Weight in pounds: "),0,0);
        gridPane.add(tfPounds,1,0);
        gridPane.add(new Label("Height in inches: "),0,1);
        gridPane.add(tfInches,1,1);
        gridPane.add(btnSubmit,2,1);

        borderPane.setTop(gridPane);
        borderPane.setLeft(textArea);

        Scene scene = new Scene(borderPane, 450, 350);
        primaryStage.setTitle("Exercise33_02Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        //
        btnSubmit.setOnAction(actionEvent -> {
            try{
                //Get the weight and height from the textfields
                double weight = Double.parseDouble(tfPounds.getText().trim());
                double height = Double.parseDouble(tfInches.getText().trim());

                //Send the weight to the server
                toServer.writeDouble(weight);
                toServer.writeDouble(height);
                toServer.flush();

                //Get BMI from server
                double bmi = fromServer.readDouble();
                String status = fromServer.readUTF();

                //Display to the text area
                textArea.appendText("Weight: "+weight+" \n");
                textArea.appendText("Height: "+height+" \n");
                textArea.appendText("BMI is "+bmi+". "+status+" \n");

            }catch (IOException ex){
                System.err.println(ex);
            }
        });

        try {
            //Create a socket to connect to the server
            Socket socket = new Socket("localhost",5000);

            //create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
            fromServer = new DataInputStream(socket.getInputStream());

            //Create output stream to send data to server
            toServer = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ex){
            textArea.appendText(ex.toString() + '\n');
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
