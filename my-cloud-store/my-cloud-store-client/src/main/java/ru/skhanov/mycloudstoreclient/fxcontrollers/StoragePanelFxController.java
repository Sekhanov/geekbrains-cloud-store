package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.skhanov.mycloudstoreclient.Network;
import ru.skhanov.mycloudstorecommon.AbstractMessage;
import ru.skhanov.mycloudstorecommon.FileParameters;
import ru.skhanov.mycloudstorecommon.FileParametersList;

public class StoragePanelFxController implements Initializable {

	@FXML
	private TableView<FileParameters> localTable;

	@FXML
	private TableColumn<FileParameters, String> localNameColumn;

	@FXML
	private TableColumn<FileParameters, Long> localSizeColumn;

	@FXML
	private TableColumn<FileParameters, String> localDateColumn;

	@FXML
	private TableView<FileParameters> cloudTable;

	@FXML
	private TableColumn<FileParameters, String> cloudNameColumn;

	@FXML
	private TableColumn<FileParameters, Long> cloudSizeColumn;

	@FXML
	private TableColumn<FileParameters, String> cloudDateColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Network.start();
		createReciveMessageThread();
		requestCloudFileList();
		initialazeTable(localTable, localNameColumn, localSizeColumn, localDateColumn, new FileParametersList("client_storage"));	

	}

	private void createReciveMessageThread() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage abstractMessage = Network.readObject();
                    if (abstractMessage instanceof FileParametersList) {
                    	FileParametersList fileParametersList = (FileParametersList) abstractMessage;
                    	initialazeTable(cloudTable, cloudNameColumn, cloudSizeColumn, cloudDateColumn, fileParametersList);
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

	@SuppressWarnings("unchecked")
	private void initialazeTable(TableView<FileParameters> tableView,
			TableColumn<FileParameters, String> nameColumn,
			TableColumn<FileParameters, Long> sizeColumn,
			TableColumn<FileParameters, String> dataColumn,
			FileParametersList fileParametersList) {
		if(Platform.isFxApplicationThread()) {
			ObservableList<FileParameters> observableList = FXCollections.observableArrayList();
			fileParametersList.getFileParameterList().forEach(e -> observableList.add(e));

			nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
			dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
			tableView.getColumns().clear();
			tableView.getColumns().addAll(localNameColumn, localSizeColumn, localDateColumn);
			tableView.setItems(observableList);
		} else {
			Platform.runLater(() -> {
				ObservableList<FileParameters> observableList = FXCollections.observableArrayList();
				fileParametersList.getFileParameterList().forEach(e -> observableList.add(e));

				nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
				sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
				dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
				tableView.getColumns().clear();
				tableView.getColumns().addAll(localNameColumn, localSizeColumn, localDateColumn);
				tableView.setItems(observableList);
			});
		}

	}

	public void requestCloudFileList() {
//      Network.sendMsg(new FileRequest("1.txt"));
      Network.sendMsg(new FileParametersList());
//    	Network.sendMsg(new TestMessage(new FileParameters("asdf", 1l, "sdfgsd")));
	}
	
//    public void refreshLocalFilesList() {
//        if (Platform.isFxApplicationThread()) {
//
//        } else {
//            Platform.runLater(() -> {
//
//            });
//        }
//    }

}
