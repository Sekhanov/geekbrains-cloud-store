package ru.skhanov.mycloudstoreserver.service;

/**
 * AuthenticationFactory
 */
public class AuthenticationFactory {

    public static AuthenticationService createAuthenticationService() {
        return new SqlDaoServiceLoggingProxy();
    }
}