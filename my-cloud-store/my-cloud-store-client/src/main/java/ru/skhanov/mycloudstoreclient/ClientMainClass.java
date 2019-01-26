package ru.skhanov.mycloudstoreclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMainClass extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/storagePanel.fxml"));
        primaryStage.setTitle("Box Client");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();        
    }
    public static void main(String[] args) {
        launch(args);
    }
}
