package ru.skhanov.mycloudstoreclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMainClass extends Application {
	
	@Override
	public void init() throws Exception {
		Network.start();
		System.out.println("start application method");
	}
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent rootPane = FXMLLoader.load(getClass().getResource("/signUp.fxml"));
//        Parent parent = FXMLLoader.load(getClass().getResource("/storagePanel.fxml"));
        primaryStage.setTitle("Box Client");
        primaryStage.setScene(new Scene(rootPane));
        primaryStage.show();        
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop() throws Exception {
    	Network.stop();
    	System.out.println("stop application method");
    }
}
