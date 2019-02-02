package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class UserController implements Initializable {
	
	@FXML
	private Label userLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void setUserLabelText(String string) {
		userLabel.setText(string);
	}

}
