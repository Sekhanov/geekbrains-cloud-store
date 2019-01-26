package client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

public class ClientTests {

	@Test
	public void getFileParameters() {
		assertTrue(true);
		try {
			System.out.println(Files.size(Paths.get("client_storage/2.txt")));


			BasicFileAttributes basicFileAttributes = Files.readAttributes(Paths.get("client_storage/2.txt"), BasicFileAttributes.class);
			System.out.println(basicFileAttributes.lastModifiedTime());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
