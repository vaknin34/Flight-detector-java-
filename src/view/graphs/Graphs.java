package view.graphs;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.AnchorPane;

public class Graphs extends AnchorPane

{
	public final GraphsController controller;
	public LineChart AlgoChart;
	public LineChart CorChart;
	public LineChart Fchart;
	
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
			this.getChildren().add(ap);
		}
		else
			controller=null;
	}

}
