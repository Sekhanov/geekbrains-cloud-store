package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.skhanov.mycloudstoreclient.MessageReciver;
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

	@FXML
	private void submit() {
		String login = loginTextField.getText();
		String password = passwordField.getText();
		AuthentificationMessage authMessage = new AuthentificationMessage(login, password,
				AuthCommandType.AUTHORIZATION);
		Network.sendMsg(authMessage);
	}

	@FXML
	private void registration() {
		openUserWindow("Create New User");
	}

	private void openUserWindow(String userWindowLabel) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(rootPane.getScene().getWindow());
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/User.fxml"));
		VBox vRootBox;
		try {
			vRootBox = fxmlLoader.load();
			UserController userController = fxmlLoader.getController();
			userController.setUserLabelText(userWindowLabel);
			dialog.setTitle("My Cloud Storage");
			dialog.setScene(new Scene(vRootBox));
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void changePassword() {
		openUserWindow("Change Password");
	}
	
	@FXML
	private void deleteUser() {
		openUserWindow("Delete User");
	}

	private void enterStorage() {
		VBox vBox;
		try {
			vBox = FXMLLoader.load(getClass().getResource("/StoragePanel.fxml"));
			rootPane.getChildren().setAll(vBox);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Метод создает поток, который обменивается и обрабатывает сообщения типа
	 * {@link AuthentificationMessage} из {@link MessageReciver}
	 */
	private void createReciveMessageThread() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					AuthentificationMessage authentificationMessage = Network.getAuthMesExchanger().exchange(null);
					switch (authentificationMessage.getAuthCommandType()) {
					case AUTHORIZATION:
						Util.fxThreadProcess(() -> {
							if (authentificationMessage.isStatus()) {
								enterStorage();
								System.out.println("authentification passed");
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
						Util.fxThreadProcess(() -> {
							if (authentificationMessage.isStatus()) {
								sqlOutputLabel.setText("user successfully registered");
							} else {
								sqlOutputLabel.setText("user alredy exist");
							}
						});
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
