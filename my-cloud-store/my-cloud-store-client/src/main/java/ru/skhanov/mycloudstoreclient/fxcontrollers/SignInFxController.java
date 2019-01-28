package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import ru.skhanov.mycloudstoreclient.Network;

public class SignInFxController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Network.start();		
	}
	
	
}
