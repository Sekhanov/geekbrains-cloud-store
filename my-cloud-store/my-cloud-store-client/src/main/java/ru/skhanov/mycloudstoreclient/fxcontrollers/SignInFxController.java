package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.skhanov.mycloudstoreclient.Network;

public class SignInFxController implements Initializable {
	
	@FXML
	private VBox rootPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {				
	}
	
	
	public void enterStorage() {
		try {
			VBox vBox = FXMLLoader.load(getClass().getResource("/storagePanel.fxml"));
			rootPane.getChildren().setAll(vBox);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
