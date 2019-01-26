package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.skhanov.mycloudstorecommon.FileParameters;

public class StoragePanelFxController implements Initializable {		
	

	@FXML
	private TableView<FileParameters> localTable;
	
	@FXML
	private TableColumn<FileParameters, String> localNameColumn;
	
	@FXML
	private TableColumn<FileParameters, Long> localSizeColumn;	
	
	@FXML 
	private TableColumn<FileParameters, FileTime> localDateColumn;
	
	@FXML 
	private TableView<FileParameters> cloudTable;
	
	@FXML
	private TableColumn<FileParameters, String> cloudNameColumn;
	
	@FXML
	private TableColumn<FileParameters, Long> cloudSizeColumn;	
	
	@FXML 
	private TableColumn<FileParameters, FileTime> cloudDateColumn;
	

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		initialazeTable(localTable, localNameColumn, localSizeColumn, localDateColumn);
		initialazeTable(cloudTable, cloudNameColumn, cloudSizeColumn, cloudDateColumn);
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void initialazeTable(TableView<FileParameters> tableView, TableColumn<FileParameters, String> nameColumn, TableColumn<FileParameters, Long> sizeColumn, TableColumn<FileParameters, FileTime> dataColumn) {
		ObservableList<FileParameters> observableList = FXCollections.observableArrayList();
		try {
			Files.list(Paths.get("client_storage")).map(e -> new FileParameters(e)).forEach(e -> observableList.add(e));
		} catch (IOException e) {
			e.printStackTrace();
		}
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		tableView.getColumns().clear();
		tableView.getColumns().addAll(localNameColumn,localSizeColumn, localDateColumn);
		tableView.setItems(observableList);
	}
	
	

	
//    public void refreshLocalFilesList() {
//        if (Platform.isFxApplicationThread()) {
//            try {
//                filesList.getItems().clear();
//                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Platform.runLater(() -> {
//                try {
//                    filesList.getItems().clear();
//                    Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
	
	
//	@SuppressWarnings("unchecked")
//	private void initialazeTable(TableView<FileParameters> tableView, TableColumn<FileParameters, String> nameColumn, TableColumn<FileParameters, Long> sizeColumn, TableColumn<FileParameters, FileTime> dataColumn) {
//		ObservableList<FileParameters> observableList = FXCollections.observableArrayList();
//		observableList.addAll(new FileParameters("Name1", 1l, FileTime.fromMillis(new Date().getTime())), new FileParameters("name2", 2l, FileTime.fromMillis(new Date().getTime())));
//		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
//		dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//		tableView.getColumns().clear();
//		tableView.getColumns().addAll(localNameColumn,localSizeColumn, localDateColumn);
//		tableView.setItems(observableList);
//	}
	
	
}
