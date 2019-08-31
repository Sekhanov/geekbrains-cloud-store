package ru.skhanov.mycloudstorecommon;

import lombok.Getter;
import lombok.Setter;


/**
 * Тип сообщения для обмена с сервером информации об аутентификации пользователей
 */
@Getter
@Setter
public class AuthenticationMessage extends AbstractMessage {	
	
	private static final long serialVersionUID = 1122033184461537593L;
	
	public enum AuthCommandType {
		REGISTRATION, CHANGE_PASS, AUTHORIZATION, DELETE_USER
	}	
	
	private boolean status;		

	private String login;
	private String password;
	private String newPassword;	
	private AuthCommandType authCommandType;
	
	public AuthenticationMessage(String login, String password, String newPassword, AuthCommandType authCommandType) {
		this.login = login;
		this.password = password;
		this.newPassword = newPassword;
		this.authCommandType = authCommandType;
	}

	public AuthenticationMessage(String login, String password, AuthCommandType authCommandType) {
		this.login = login;
		this.password = password;
		this.authCommandType = authCommandType;
	}
	
	
	
	
}
