package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstorecommon.AbstractMessage;
import ru.skhanov.mycloudstorecommon.FileMessage;
import ru.skhanov.mycloudstorecommon.FileOperationsMessage;
import ru.skhanov.mycloudstorecommon.FileParameters;
import ru.skhanov.mycloudstorecommon.FileParametersListMessage;
import ru.skhanov.mycloudstorecommon.FileOperationsMessage.FileOperation;

public class StoragePanelFxController implements Initializable {
	
	private static final String  CLIENT_STORAGE = "client_storage/";

	@FXML
	private TableView<FileParameters> localTable;

	@FXML
	private TableView<FileParameters> cloudTable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeTables(localTable);
		initializeTables(cloudTable);
		refreshLocalFileTable();
//		createReciveMessageThread();
		requestCloudFileList();

	}

	private void refreshLocalFileTable() {
		refreshTableEntries(localTable, new FileParametersListMessage(CLIENT_STORAGE));
	}

	private void refreshTableEntries(TableView<FileParameters> table, FileParametersListMessage fileParametersList) {
		if (Platform.isFxApplicationThread()) {
			table.getItems().clear();
			fileParametersList.getFileParameterList().forEach(e -> table.getItems().add(e));
		} else {
			Platform.runLater(() -> {
				table.getItems().clear();
				fileParametersList.getFileParameterList().forEach(e -> table.getItems().add(e));
			});
		}
	}

	@SuppressWarnings("unchecked")
	private void initializeTables(TableView<FileParameters> table) {
		TableColumn<FileParameters, String> localNameColumn = new TableColumn<>("Name");
		localNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		localNameColumn.setPrefWidth(270);
		TableColumn<FileParameters, Integer> localSizeColumn = new TableColumn<>("Size");
		localSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
		localSizeColumn.setPrefWidth(85);
		TableColumn<FileParameters, String> localDateColumn = new TableColumn<>("Date");
		localDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		localDateColumn.setPrefWidth(140);
		table.getColumns().addAll(localNameColumn, localSizeColumn, localDateColumn);

	}

	private void createReciveMessageThread() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					AbstractMessage msg = Network.readObject();
					if (msg instanceof FileParametersListMessage) {
						FileParametersListMessage fileParametersList = (FileParametersListMessage) msg;
						refreshTableEntries(cloudTable, fileParametersList);
					}
					if(msg instanceof FileMessage) {
						FileMessage fileMessage = (FileMessage) msg;
						Files.write(Paths.get(CLIENT_STORAGE + fileMessage.getFilename()), fileMessage.getData(), StandardOpenOption.CREATE);
						refreshLocalFileTable();												
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} finally {
				Network.stop();
			}
		}, "ClientMsgReciver");
		t.setDaemon(true);
		t.start();
	}

	private void requestCloudFileList() {
		Network.sendMsg(new FileParametersListMessage());
	}

	public Path copyFileToCloud() {
		FileParameters focusedFileLine = localTable.getSelectionModel().getSelectedItem();
		Path path = Paths.get(CLIENT_STORAGE + focusedFileLine.getName());
		if (Files.exists(path)) {
			try {
				FileMessage fileMessage = new FileMessage(path);
				Network.sendMsg(fileMessage);
				requestCloudFileList();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}
	
	public void copyFileFromCloud() {
		FileParameters focusedFileLine = cloudTable.getSelectionModel().getSelectedItem();
		FileOperationsMessage fileOperationsMessage = new FileOperationsMessage(FileOperation.COPY, focusedFileLine.getName());
		Network.sendMsg(fileOperationsMessage);
	}
	
	public void moveFieToCloud() {
		try {
			Files.delete(copyFileToCloud());
			refreshLocalFileTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void moveFileFromClod() {
		FileParameters focusedFileLine = cloudTable.getSelectionModel().getSelectedItem();
		FileOperationsMessage fileOperationsMessage = new FileOperationsMessage(FileOperation.MOVE, focusedFileLine.getName());
		Network.sendMsg(fileOperationsMessage);
	}
	
	public void deleteLocalFile() {
		FileParameters focusedFileLine = localTable.getSelectionModel().getSelectedItem();
		Path path = Paths.get(CLIENT_STORAGE + focusedFileLine.getName());
		if (Files.exists(path)) {
			try {
				Files.delete(path);
				refreshLocalFileTable();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delteFileFromCloud() {
		FileParameters focusedFileLine = cloudTable.getSelectionModel().getSelectedItem();
		FileOperationsMessage fileOperationsMessage = new FileOperationsMessage(FileOperation.DELETE, focusedFileLine.getName());
		Network.sendMsg(fileOperationsMessage);
	}

}
