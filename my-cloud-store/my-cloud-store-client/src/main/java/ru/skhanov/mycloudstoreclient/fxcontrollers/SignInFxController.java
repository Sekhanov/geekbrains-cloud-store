package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.skhanov.mycloudstoreclient.MessageReceiver;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstoreclient.Util;
import ru.skhanov.mycloudstorecommon.AuthenticationMessage;
import ru.skhanov.mycloudstorecommon.AuthenticationMessage.AuthCommandType;

public class SignInFxController implements Initializable {
	
	public static final String USER_PANEL = "/User.fxml";
	public static final String STORAGE_PANEL = "/StoragePanel.fxml";

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
		createReceiveMessageThread();
	}

	@FXML
	private void submit() {
		String login = loginTextField.getText();
		String password = passwordField.getText();
		AuthenticationMessage authMessage = new AuthenticationMessage(login, password,
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
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(USER_PANEL));
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
			vBox = FXMLLoader.load(getClass().getResource(STORAGE_PANEL));
			rootPane.getChildren().setAll(vBox);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Метод создает поток, который обменивается и обрабатывает сообщения типа
	 * {@link AuthenticationMessage} из {@link MessageReceiver}
	 */
	private void createReceiveMessageThread() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					AuthenticationMessage authenticationMessage = Network.getAuthMesExchanger().exchange(null);
					switch (authenticationMessage.getAuthCommandType()) {
					case AUTHORIZATION:
						Util.fxThreadProcess(() -> {
							if (authenticationMessage.isStatus()) {
								enterStorage();
								System.out.println("authentication passed");
							} else {
								sqlOutputLabel.setText("authentication failed");
							}
						});
						break;
					case CHANGE_PASS:
						Util.fxThreadProcess(() -> {
							if(authenticationMessage.isStatus()) {
								sqlOutputLabel.setText("user password with login '"  + authenticationMessage.getLogin() + "' successfully changed");
							} else {
								sqlOutputLabel.setText("no user with login '" + authenticationMessage.getLogin() + "' or password incorrect");
							}
						});
						break;
					case DELETE_USER:
						Util.fxThreadProcess(() -> {
							if(authenticationMessage.isStatus()) {
								sqlOutputLabel.setText("user with login '"  + authenticationMessage.getLogin() + "' and his storage successfully deleted");
							} else {
								sqlOutputLabel.setText("no user with login '" + authenticationMessage.getLogin() + "' or password incorrect");
							}
						});
						break;
					case REGISTRATION:
						Util.fxThreadProcess(() -> {
							if (authenticationMessage.isStatus()) {
								sqlOutputLabel.setText("user successfully registered");
							} else {
								sqlOutputLabel.setText("user with login '" + authenticationMessage.getLogin() + "' already exist");
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
		}, "AuthMsgReceiver");
		t.setDaemon(true);
		t.start();
	}

}
