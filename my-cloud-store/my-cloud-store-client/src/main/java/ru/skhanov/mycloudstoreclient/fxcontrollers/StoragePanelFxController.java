package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import ru.skhanov.mycloudstorecommon.FileParameters;
import ru.skhanov.mycloudstorecommon.FileParametersList;

public class StoragePanelFxController implements Initializable {
	
	private static final String  CLIENT_STORAGE = "client_storage/";

	@FXML
	private TableView<FileParameters> localTable;

	@FXML
	private TableView<FileParameters> cloudTable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Network.start();
		initializeTables(localTable);
		initializeTables(cloudTable);
		refreshLocalFileTable();
		createReciveMessageThread();
		requestCloudFileList();

	}

	private void refreshLocalFileTable() {
		refreshTableEntries(localTable, new FileParametersList(CLIENT_STORAGE));
	}

	private void refreshTableEntries(TableView<FileParameters> table, FileParametersList fileParametersList) {
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
					AbstractMessage abstractMessage = Network.readObject();
					if (abstractMessage instanceof FileParametersList) {
						FileParametersList fileParametersList = (FileParametersList) abstractMessage;
						refreshTableEntries(cloudTable, fileParametersList);
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
		Network.sendMsg(new FileParametersList());
	}

	public Path copyFileToCloud() {
		FileParameters fileParameters = localTable.getSelectionModel().getSelectedItem();
		Path path = Paths.get(CLIENT_STORAGE + fileParameters.getName());
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
	
	public void moveFieToCloud() {
		try {
			Files.delete(copyFileToCloud());
			refreshLocalFileTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteLocalFile() {
		FileParameters fileParameters = localTable.getSelectionModel().getSelectedItem();
		Path path = Paths.get(CLIENT_STORAGE + fileParameters.getName());
		if (Files.exists(path)) {
			try {
				Files.delete(path);
				refreshLocalFileTable();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
