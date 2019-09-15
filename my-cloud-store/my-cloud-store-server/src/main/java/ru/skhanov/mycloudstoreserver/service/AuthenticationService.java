package ru.skhanov.mycloudstoreserver.service;

/**
 * AuthenticationService
 */
public interface AuthenticationService {

    void insertUser(String name, String password);

	void deleteUserByName(String login);

	public User selectUserByName(String login);

	public boolean authentication(String login, String password);
	
	public boolean changePass(String login, String oldPass, String newPass);
    
}