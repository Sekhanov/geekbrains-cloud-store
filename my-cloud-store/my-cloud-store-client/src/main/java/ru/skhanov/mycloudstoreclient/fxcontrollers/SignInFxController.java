package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstoreclient.Util;
import ru.skhanov.mycloudstorecommon.AbstractMessage;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage;
import ru.skhanov.mycloudstorecommon.FileMessage;
import ru.skhanov.mycloudstorecommon.FileParametersListMessage;
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
		createReciveMessageThread();
	}

	public void submit() {
		String login = loginTextField.getText();
		String password = passwordTextField.getText();
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
					AbstractMessage msg = Network.readObject();
					if (msg instanceof AuthentificationMessage) {
						AuthentificationMessage authentificationMessage = (AuthentificationMessage) msg;
						switch (authentificationMessage.getAuthCommandType()) {
						case AUTHORIZATION:
							Util.fxThreadProcess(this::enterStorage);
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
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} finally {
				Network.stop();
			}
		}, "ClientMsgReciver");
		t.setDaemon(true);
		t.start();
	}

}
