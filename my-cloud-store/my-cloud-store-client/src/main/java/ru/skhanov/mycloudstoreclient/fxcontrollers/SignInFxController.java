package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage.AuthCommandType;

public class SignInFxController implements Initializable {
	
	@FXML
	private VBox rootPane;
	
	@FXML
	private TextField loginTextField;
	
	@FXML
	private TextField passwordTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {				
	}
	
	public void submit() {
		String login = loginTextField.getText();
		String password = passwordTextField.getText();
		AuthentificationMessage authMessage = new AuthentificationMessage(false, login, password, AuthCommandType.AUTHORIZATION);
		Network.sendMsg(authMessage);
		
		if(true) {
			enterStorage();
		}
	}
	
	private void enterStorage() {
		try {
			VBox vBox = FXMLLoader.load(getClass().getResource("/storagePanel.fxml"));
			rootPane.getChildren().setAll(vBox);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
