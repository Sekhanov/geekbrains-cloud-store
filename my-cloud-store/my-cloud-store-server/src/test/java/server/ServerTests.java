package server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.skhanov.mycloudstorecommon.FileParametersListMessage;
import ru.skhanov.mycloudstoreserver.SqlUsersDaoService;

public class ServerTests {

	@Test
	public void userDaoTest() {
		SqlUsersDaoService sqlUsersDaoService = new SqlUsersDaoService("jdbc:sqlite:my_cloud_store_server.db", "org.sqlite.JDBC");		
		sqlUsersDaoService.insertUser("testUser", "12345");
		SqlUsersDaoService.User user = sqlUsersDaoService.selectUserByName("testUser");
		assertEquals("12345", user.getPassword());
		sqlUsersDaoService.deleteUserByName("testUser");
	}
	
	@Test
	public void serverStorage() {
		FileParametersListMessage fileParametersList = new FileParametersListMessage("server_storage");
		fileParametersList.getFileParameterList().forEach(e -> System.out.println(e.getName()));
	}
}
