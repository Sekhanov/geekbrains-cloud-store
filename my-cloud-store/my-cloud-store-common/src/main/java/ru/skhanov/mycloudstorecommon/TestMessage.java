package ru.skhanov.mycloudstorecommon;

public class TestMessage extends AbstractMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FileParameters[] fileParameters;

	public TestMessage(FileParameters... fileParameters) {
		this.fileParameters = fileParameters;
	}
	
	
}
