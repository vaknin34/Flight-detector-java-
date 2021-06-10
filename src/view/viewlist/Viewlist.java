package view.viewlist;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class Viewlist extends AnchorPane
{
	public final ViewlistController controller;
	public ListView<String> list;
	
	public Viewlist()
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap = null;
		try {
			ap = fxl.load(getClass().getResource("Viewlist.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		
		if(ap!=null) {
			controller=fxl.getController();
			list=controller.listView;
			this.getChildren().add(ap);
		}
		else
			controller=null;
	}

}
