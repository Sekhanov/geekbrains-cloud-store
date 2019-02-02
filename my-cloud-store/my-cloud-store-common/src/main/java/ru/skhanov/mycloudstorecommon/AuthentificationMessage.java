package ru.skhanov.mycloudstorecommon;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * Тип сообщения для обмена с сервером информации об аутентификации пользователей
 */
@Getter
@Setter
@RequiredArgsConstructor
public class AuthentificationMessage extends AbstractMessage {	
	
	private static final long serialVersionUID = 1122033184461537593L;
	
	public enum AuthCommandType {
		REGISTRATION, CHANGE_PASS, AUTHORIZATION, DELETE_USER
	}	
	
	private boolean status;	
	
	@NonNull
	private String login;
	@NonNull
	private String password;
	@NonNull
	private AuthCommandType authCommandType;
}
