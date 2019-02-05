package ru.skhanov.mycloudstoreserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;


/**
 * Класс для взаимодействия с таблицей Users в базе данных
 * для реализации механизма аутентификации в приложении
 */
public class SqlUsersDaoService {
	
	@Getter
	@Setter
	public class User {
		String login;
		String password;
	}

	private Connection connection;

	public SqlUsersDaoService(String sqlUrl, String sqlDriverClass) {
		try {
			Class.forName(sqlDriverClass);
			this.connection = DriverManager.getConnection(sqlUrl);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("DB with URL " + sqlUrl + " successfully connected");
	}

	private PreparedStatement createPreparedStatement(String sql, String... parameters) throws SQLException {
		PreparedStatement preparedStatement = null;
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++) {
				preparedStatement.setString(i + 1, parameters[i]);
			}
		return preparedStatement;
	}

	public void insertUser(String name, String password) {
		String sqlString = "INSERT into users (name, password) VALUES (?, ?);";
		try {
			PreparedStatement preparedStatement = createPreparedStatement(sqlString, name, password);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteUserByName(String login) {
		String sqlString = "DELETE FROM users WHERE name = ?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = createPreparedStatement(sqlString, login);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User selectUserByName(String login) {
		String sqlString = "SELECT name, password FROM users WHERE name = ?";
		User user = null;
		try {
			PreparedStatement preparedStatement = createPreparedStatement(sqlString, login);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setLogin(resultSet.getString("name"));
				user.setPassword(resultSet.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean authentification(String login, String passwrd) {
		User user = selectUserByName(login);
		if(user == null) {
			return false;
		}
		return user.getPassword().equals(passwrd);
	}
	
	public boolean changePass(String login, String oldPass, String newPass) {
		if(authentification(login, oldPass)) {
			String sqlString = "UPDATE users SET password = ? WHERE name = ?";
			try {
				PreparedStatement preparedStatement = createPreparedStatement(sqlString, newPass, login);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {	
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

}
