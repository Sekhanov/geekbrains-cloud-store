package ru.skhanov.mycloudstoreserver.chanofauthenrification;

import ru.skhanov.mycloudstoreserver.service.AuthenticationService;

/**
 * LoginExistCheckMiddle
 */
public class LoginExistCheckMiddle extends MiddleWare {

    private AuthenticationService authenticationService;

    public LoginExistCheckMiddle(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean check(String login, String password) {
        if (!authenticationService.isLogin(login)) {
            System.out.printf("login %s not exist", login);
            return false;
        }
        return checkNext(login, password);
    }

}