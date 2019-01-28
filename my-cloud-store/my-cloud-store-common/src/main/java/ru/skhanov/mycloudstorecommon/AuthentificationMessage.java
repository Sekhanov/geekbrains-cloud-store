package ru.skhanov.mycloudstorecommon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthentificationMessage {	
	
	public enum AuthCommandType {
		REGISTRATION, CHANE_PASS, AUTHORIZATION, DELETE_USER
	}	
	
	private boolean status;
	private String login;
	private String password;
	private AuthCommandType authCommandType;
}
