package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstorecommon.AuthenticationMessage;
import ru.skhanov.mycloudstorecommon.AuthenticationMessage.AuthCommandType;

public class UserController implements Initializable {

	private static final String NEW_USER = "Create New User";
	private static final String NEW_PASS = "New Password";
	private static final String OLD_PASS = "Old Password";
	private static final String DELETE_USER = "Delete User";
	private static final String CHANGE_PASS = "Change Password";
	
	@FXML
	private VBox rootPane;
	
	@FXML
	private Label userLabel;
	
	@FXML
	private TextField loginField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML 
	private PasswordField confirmPasswordField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void setUserLabelText(String string) {
		userLabel.setText(string);
		if(userLabel.getText().equals(CHANGE_PASS)) {
			passwordField.setPromptText(OLD_PASS);;
			confirmPasswordField.setPromptText(NEW_PASS);
		}
		
	}
	
	@FXML
	private void enter() {
		String password = passwordField.getText().equals(confirmPasswordField.getText()) ? this.passwordField.getText() : null;
		if(password != null) {
			if(userLabel.getText().equals(NEW_USER)) {
				Network.sendMsg(new AuthenticationMessage(loginField.getText(), password, AuthCommandType.REGISTRATION));
				// Network.sendMsg(new AuthenticationMessage(loginField.getText(), password, AuthCommandType.REGISTRATION));
			}

			if(userLabel.getText().equals(DELETE_USER)) {
				Network.sendMsg(new AuthenticationMessage(loginField.getText(), password, AuthCommandType.DELETE_USER));
			}
		} 
		if(confirmPasswordField.getPromptText().equals(NEW_PASS)) {
			Network.sendMsg(new AuthenticationMessage(loginField.getText(), passwordField.getText(),
					confirmPasswordField.getText(), AuthCommandType.CHANGE_PASS));
		}
		 Stage stage = (Stage) rootPane.getScene().getWindow();
		 stage.close();
	}

}
