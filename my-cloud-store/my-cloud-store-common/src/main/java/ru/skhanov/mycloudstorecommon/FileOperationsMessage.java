package ru.skhanov.mycloudstorecommon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Класс для передачи с клиента на сервер необходимой операции
 * с файлом в облачном хранилище
 */
@Getter
@Setter
@AllArgsConstructor
public class FileOperationsMessage extends AbstractMessage  {

	public static final long serialVersionUID = 1344774173888738704L;
	
	public enum FileOperation {
		COPY, MOVE, DELETE		
	}
	
	private FileOperation fileOperation;	
	private String fileName;
}
