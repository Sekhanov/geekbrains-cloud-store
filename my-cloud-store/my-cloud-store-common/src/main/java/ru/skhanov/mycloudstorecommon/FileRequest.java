package ru.skhanov.mycloudstorecommon;

public class FileRequest extends AbstractMessage {

	private static final long serialVersionUID = -8210870560089805346L;	
	
	private String filename;

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename) {
        this.filename = filename;
    }
}
