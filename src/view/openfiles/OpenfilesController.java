package view.openfiles;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class OpenfilesController {

	@FXML private MenuItem csv;
	@FXML private MenuItem csv1;
	@FXML private MenuItem algo;
	@FXML private MenuItem xml;

	public Runnable xmlFile,trainCSVFile,testCSVFile,classFile,connect,disconnect;
	
	@FXML public void openXMLFile() {if(xmlFile!=null) xmlFile.run();}
	@FXML public void openTrainCSVFile() {if(trainCSVFile!=null) trainCSVFile.run();}
	@FXML public void openTestCsv() {if(testCSVFile!=null) testCSVFile.run();}
	@FXML public void openCLASSFile() {if(classFile!=null) classFile.run();}
	@FXML public void Connect() {if(connect!=null) connect.run();}
	@FXML public void Disconnect() {if(disconnect!=null) disconnect.run();}

}
