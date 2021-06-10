package view.openfiles;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Openfiles extends AnchorPane
{
	public final OpenfilesController controller;
	
	public Openfiles()
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap = null;
		
		try {
			ap = fxl.load(getClass().getResource("Openfiles.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		if(ap!=null) {
			controller=fxl.getController();
			this.getChildren().add(ap);
		}
		else
			controller=null;
	}

}
