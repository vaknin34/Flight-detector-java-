package view.buttons;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public class Buttons extends AnchorPane
{
	public final ButtonsController controller;
	public Slider timeSlider;
	public Label videoTime;
	public ChoiceBox<Number> videoSpeed;
	
	public Buttons() {
		AnchorPane ap = null;
		FXMLLoader fxl = new FXMLLoader();
		try {
			ap = fxl.load(getClass().getResource("Buttons.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		
		if(ap!=null){
			controller=fxl.getController();
			timeSlider=controller.videoSlider;
			videoSpeed=controller.videoSpeed;
			videoSpeed.setItems(FXCollections.observableArrayList(0.25,0.5,0.75,1.0,1.25,1.5,1.75,2.0));
			videoSpeed.setValue(1.0);
			videoTime=controller.VideoTime;
			this.getChildren().add(ap);
		}
		else 
			controller =null;
	}
}
