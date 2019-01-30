package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstoreclient.Util;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage.AuthCommandType;

public class SignInFxController implements Initializable {

	@FXML
	private VBox rootPane;
	
	@FXML
	private Label sqlOutputLabel;

	@FXML
	private TextField loginTextField;

	@FXML	
	private PasswordField passwordField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		createReciveMessageThread();
	}

	public void submit() {
		String login = loginTextField.getText();
		String password = passwordField.getText();
		AuthentificationMessage authMessage = new AuthentificationMessage(login, password,
				AuthCommandType.AUTHORIZATION);
		Network.sendMsg(authMessage);
	}

	private void enterStorage() {
		VBox vBox;
		try {
			vBox = FXMLLoader.load(getClass().getResource("/storagePanel.fxml"));
			rootPane.getChildren().setAll(vBox);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createReciveMessageThread() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					AuthentificationMessage authentificationMessage = Network.getAuthMesExchanger().exchange(null);			
						switch (authentificationMessage.getAuthCommandType()) {
						case AUTHORIZATION:
							Util.fxThreadProcess(() -> {
								if(authentificationMessage.isStatus()) {
									enterStorage();
									System.out.println("authentification passed");
//									sqlOutputLabel.setText("pass authentication");
								} else {
									sqlOutputLabel.setText("authentificaton failed");
								}
							});
							break;
						case CHANGE_PASS:

							break;
						case DELETE_USER:

							break;
						case REGISTRATION:

							break;
						default:
							break;
						}
					
				}
	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "ClientMsgReciver");
		t.setDaemon(true);
		t.start();
	}

}
