package ru.skhanov.mycloudstoreclient.fxcontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import ru.skhanov.mycloudstorecommon.FileParameters;

public class StoragePanelFxController implements Initializable {

	@FXML
	TableView<FileParameters> localFileStorageTableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<FileParameters> fileParametersList = FXCollections.observableArrayList();
		fileParametersList.addAll(new FileParameters("Name1", 1, "20 nov"), new FileParameters("name2", 2, "30 oct"));
		localFileStorageTableView.setItems(fileParametersList);
	}

//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//        Network.start();
//        Thread t = new Thread(() -> {
//            try {
//                while (true) {
//                    AbstractMessage am = Network.readObject();
//                    if (am instanceof FileMessage) {
//                        FileMessage fm = (FileMessage) am;
//                        Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
//                        refreshLocalFilesList();
//                    }
//                }
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                Network.stop();
//            }
//        });
//        t.setDaemon(true);
//        t.start();
//        filesList.setItems(FXCollections.observableArrayList());
//        refreshLocalFilesList();		
//	}
//	
//	public void pressOnDownloadBtn(ActionEvent actionEvent) {
//        if (tfFileName.getLength() > 0) {
//            Network.sendMsg(new FileRequest(tfFileName.getText()));
//            tfFileName.clear();
//        }
//    }
//
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

}
