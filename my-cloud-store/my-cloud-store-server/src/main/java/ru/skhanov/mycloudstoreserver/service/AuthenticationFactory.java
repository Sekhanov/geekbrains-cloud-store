package ru.skhanov.mycloudstoreserver.service;

/**
 * AuthenticationFactory
 */
public class AuthenticationFactory {

    public static AuthenticationService createAuthenticationService() {
        return new SqlUsersDaoService("jdbc:sqlite:my_cloud_store_server.db", "org.sqlite.JDBC");
    }
}