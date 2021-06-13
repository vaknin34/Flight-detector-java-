package view.graphs;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;

public class Graphs extends AnchorPane

{
	public final GraphsController controller;
	public LineChart AlgoChart;
	public LineChart CorChart;
	public LineChart Fchart;
	public NumberAxis FchartX;
	public NumberAxis FchartY;
	public NumberAxis CorxAxis;
	public NumberAxis CoryAxis;
	
	public Graphs() 
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap= null;
		try {
			ap = fxl.load(getClass().getResource("Graphs.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		
		if(ap!=null) {
			controller=fxl.getController();
			Fchart=controller.FeatureChart;
			CorChart=controller.CorrelatedChart;
			AlgoChart=controller.algoChart;
			FchartX = controller.FchartX;
			FchartY = controller.FchartY;
			CorxAxis = controller.CorxAxis;
			CoryAxis = controller.CoryAxis;
			this.getChildren().add(ap);
		}
		else
			controller=null;
	}

}
