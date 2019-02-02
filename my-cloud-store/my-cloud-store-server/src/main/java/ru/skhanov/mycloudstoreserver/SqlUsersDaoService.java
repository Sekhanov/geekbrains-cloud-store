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
		String name;
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

	public void deleteUserByName(String name) {
		String sqlString = "DELETE FROM users WHERE name = ?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = createPreparedStatement(sqlString, name);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User selectUserByName(String name) {
		String sqlString = "SELECT name, password FROM users WHERE name = ?";
		User user = null;
		try {
			PreparedStatement preparedStatement = createPreparedStatement(sqlString, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setName(resultSet.getString("name"));
				user.setPassword(resultSet.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean authentification(String name, String passwrd) {
		User user = selectUserByName(name);
		if(user == null) {
			return false;
		}
		return user.getPassword().equals(passwrd);
	}

}
