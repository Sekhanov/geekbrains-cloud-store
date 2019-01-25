package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.net.URL;
import java.util.ResourceBundle;

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
	private TableColumn<FileParameters, Integer> localSizeColumn;	
	
	@FXML 
	private TableColumn<FileParameters, String> localDateColumn;
	
	@FXML 
	private TableView<FileParameters> cloudTable;
	
	@FXML
	private TableColumn<FileParameters, String> cloudNameColumn;
	
	@FXML
	private TableColumn<FileParameters, Integer> cloudSizeColumn;	
	
	@FXML 
	private TableColumn<FileParameters, String> cloudDateColumn;
	

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		initialazeTable(localTable, localNameColumn, localSizeColumn, localDateColumn);
		initialazeTable(cloudTable, cloudNameColumn, cloudSizeColumn, cloudDateColumn);
		
	}


	@SuppressWarnings("unchecked")
	private void initialazeTable(TableView<FileParameters> tableView, TableColumn<FileParameters, String> nameColumn, TableColumn<FileParameters, Integer> sizeColumn, TableColumn<FileParameters, String> dataColumn) {
		ObservableList<FileParameters> observableList = FXCollections.observableArrayList();
		observableList.addAll(new FileParameters("Name1", 1, "20 nov"), new FileParameters("name2", 2, "30 oct"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		tableView.getColumns().clear();
		tableView.getColumns().addAll(localNameColumn,localSizeColumn, localDateColumn);
		tableView.setItems(observableList);
	}
}
