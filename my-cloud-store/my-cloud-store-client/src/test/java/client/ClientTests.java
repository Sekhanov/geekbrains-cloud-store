package client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Ignore;
import org.junit.Test;

public class ClientTests {

	@Test
	@Ignore
	public void getFileParameters() {
		assertTrue(true);
		try {
			System.out.println(Files.size(Paths.get("client_storage/2.txt")));


			BasicFileAttributes basicFileAttributes = Files.readAttributes(Paths.get("client_storage/2.txt"), BasicFileAttributes.class);
			System.out.println(basicFileAttributes.lastModifiedTime());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void nio() {
		
		Path path = Paths.get("c:\\temp\\");
		System.out.println(path.toString());
		
	}
}
