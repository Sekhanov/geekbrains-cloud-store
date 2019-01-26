package ru.skhanov.mycloudstorecommon;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Long size;
	private String date;
	
	
	
	public FileParameters(String fileName, String fileStoragePath) {
		Path path = Paths.get(name + fileStoragePath);
		this.name = fileName;
		try {
			this.size = Files.size(path);
			BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
			this.date = basicFileAttributes.lastModifiedTime().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileParameters(Path path) {
		this.name = path.toFile().getName();
		try {
			this.size = Files.size(path);
			BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
			this.date = basicFileAttributes.lastModifiedTime().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
}
