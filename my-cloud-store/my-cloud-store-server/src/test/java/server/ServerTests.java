package server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.skhanov.mycloudstoreserver.service.SqlUsersDaoService;
import ru.skhanov.mycloudstoreserver.service.User;

public class ServerTests {

	@Test
	public void userDaoTest() {
		String testLogin = "testUser";
		String testPassword = "12345";
		String newPass = "54321";
		SqlUsersDaoService sqlUsersDaoService = new SqlUsersDaoService("jdbc:sqlite:my_cloud_store_server.db", "org.sqlite.JDBC");		
		sqlUsersDaoService.insertUser(testLogin, testPassword);
		sqlUsersDaoService.changePass(testLogin, testPassword, newPass);
		User user = sqlUsersDaoService.selectUserByName("testUser");
		assertEquals("54321", user.getPassword());
		sqlUsersDaoService.deleteUserByName("testUser");		
	}
	
	
	
	
	

}
