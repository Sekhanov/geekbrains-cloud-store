package ru.skhanov.mycloudstorecommon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthentificationMessage extends AbstractMessage {	
	
	private static final long serialVersionUID = 1122033184461537593L;
	
	public enum AuthCommandType {
		REGISTRATION, CHANE_PASS, AUTHORIZATION, DELETE_USER
	}	
	
	private boolean status;
	private String login;
	private String password;
	private AuthCommandType authCommandType;
}
