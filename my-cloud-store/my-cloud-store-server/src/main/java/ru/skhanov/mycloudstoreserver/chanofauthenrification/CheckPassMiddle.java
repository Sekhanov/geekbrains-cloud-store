package ru.skhanov.mycloudstoreserver.chanofauthenrification;

import ru.skhanov.mycloudstoreserver.service.AuthenticationService;

/**
 * CheckPassMiddle
 */
public class CheckPassMiddle extends MiddleWare {

    private AuthenticationService authenticationService;

    public CheckPassMiddle(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean check(String login, String password) {
        if(!authenticationService.checkPassword(login, password)) {
            System.out.printf("password fromm login %s not correct", login);
            return false;
        }
        return checkNext(login, password);
    }

    
}