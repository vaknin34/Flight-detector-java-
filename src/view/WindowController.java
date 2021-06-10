package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import view.viewlist.Viewlist;
import viewModel.ViewModel;
import view.openfiles.Openfiles;
import view.graphs.Graphs;
import view.joystick.Joystick;
import view.buttons.Buttons;

public class WindowController {

	@FXML Viewlist viewlist;
	@FXML Openfiles openfiles;
	@FXML Graphs graphs;
	@FXML Joystick joystick;
	@FXML Buttons buttons;
	
	Series seriesFeature;
	Series seriesCor;
	Series seriesAlgo;
	Series seriesRegularFlight;
	Series seriesAnomaliesFlight;
	Series seriesAnomaliesPoints;
	
	String selectedCol,corlleatedCol;
	
	public void init() {
		ViewModel vm = new ViewModel();
		
		buttons.controller.onPlay=vm.Play;
		buttons.controller.onPause=vm.Pause;
		buttons.controller.onStop=vm.Stop;
		buttons.controller.onForward=vm.Forward;
		buttons.controller.onDoubleForward=vm.DoubleForward;
		buttons.controller.onBackward=vm.Backward;
		buttons.controller.onDoubleBackward=vm.DoubleBackward;
		openfiles.controller.connect=vm.connect;
		openfiles.controller.disconnect=vm.disconnect;
		
		openfiles.controller.xmlFile=()->vm.openXml();
		openfiles.controller.classFile=()->vm.openCLASSFile();
		openfiles.controller.trainCSVFile=()->vm.openTrainCSVFile();
		openfiles.controller.testCSVFile=()->vm.openTestCsv();
		
		seriesFeature = new Series();
		seriesCor = new Series();
		seriesAlgo = new Series();
		seriesAlgo.setName("Anomaly Algo");
		seriesRegularFlight = new Series();
		seriesRegularFlight.setName("Regular Flight");
		seriesAnomaliesFlight = new Series();
		seriesAnomaliesFlight.setName("Anomaly Flight");
		seriesAnomaliesPoints = new Series();
		seriesAnomaliesPoints.setName("Anomalies Points");
		
		
		graphs.Fchart.getData().add(seriesFeature);
		graphs.CorChart.getData().add(seriesCor);
		graphs.AlgoChart.getData().addAll(seriesAlgo,seriesRegularFlight,seriesAnomaliesFlight,seriesAnomaliesPoints);
		
		selectedCol=null;
		corlleatedCol=null;
		
/*           Binding and Listener              */
		
		vm.rate.bindBidirectional(buttons.videoSpeed.valueProperty());
		buttons.timeSlider.valueProperty().bindBidirectional(vm.timeStep);		
		buttons.videoTime.textProperty().bind(vm.videoTime);
		buttons.timeSlider.valueProperty().addListener((o,ov,nv)->{
			if(selectedCol!=null) {
				if(ov.intValue()+1==nv.intValue()) 
					vm.paintFeature(selectedCol, nv, seriesFeature);
				else {
					clearSeries(seriesFeature);
				//	vm.FilluntillNow(selectedCol, seriesFeature);
				}
			}
			else {
				clearSeries(seriesFeature);
			}
			if(corlleatedCol!=null) {
				if(ov.intValue()+1==nv.intValue())
					vm.paintFeature(corlleatedCol, nv, seriesCor);
				else
					clearSeries(seriesCor);
			}
			else {
				clearSeries(seriesCor);
			}
		});
		
		vm.testPath.addListener((o,ov,nv)->{
			ArrayList<String> titles = vm.getColTitels();
			if (titles != null) {
				ObservableList<String> list = FXCollections.observableArrayList(titles);
				viewlist.list.setItems(list);
				buttons.timeSlider.setMax(vm.getTrain().NumOfRows);
				buttons.timeSlider.setMin(0);
			}
		});
		
		vm.xmlPath.addListener((o,ov,nv)->{
			if(vm.getXs()!=null) {
				joystick.SetMaxMinForSliders(
						vm.getXs().getSetting("rudder").getMax(),
						vm.getXs().getSetting("rudder").getMin(),
						vm.getXs().getSetting("throttle").getMax(),
						vm.getXs().getSetting("throttle").getMin());
				joystick.setMaxMinForClock(
					vm.getXs().getSetting("direction").getMax(),
					vm.getXs().getSetting("direction").getMin(),
					vm.getXs().getSetting("heigth").getMax(),
					vm.getXs().getSetting("heigth").getMin(),
					vm.getXs().getSetting("pitch").getMax(),
					vm.getXs().getSetting("pitch").getMin(),
					vm.getXs().getSetting("roll").getMax(),
					vm.getXs().getSetting("roll").getMin(),
					vm.getXs().getSetting("speed").getMax(),
					vm.getXs().getSetting("speed").getMin(),
					vm.getXs().getSetting("yaw").getMax(),
					vm.getXs().getSetting("yaw").getMin());
				
				joystick.aileron.bind(vm.DisplayVar.get("aileron"));
				joystick.elevator.bind(vm.DisplayVar.get("elevator"));
				joystick.rudder.valueProperty().bind(vm.DisplayVar.get("rudder"));
				joystick.throttle.valueProperty().bind(vm.DisplayVar.get("throttle"));
				joystick.direction.valueProperty().bind(vm.DisplayVar.get("direction"));
				joystick.altitude.valueProperty().bind(vm.DisplayVar.get("heigth"));
				joystick.pitch.valueProperty().bind(vm.DisplayVar.get("pitch"));
				joystick.roll.valueProperty().bind(vm.DisplayVar.get("roll"));
				joystick.speed.valueProperty().bind(vm.DisplayVar.get("speed"));
				joystick.yaw.valueProperty().bind(vm.DisplayVar.get("yaw"));
			}
		});
		vm.algoName.addListener((o,ov,nv)->{
			if(vm.getAd()!=null) {
				graphs.AlgoChart.setTitle(nv.substring(6));
			
			}
		});
		joystick.aileron.addListener((o,ov,nv)->{
			double max = vm.getXs().getSetting("aileron").getMax();
			double min = vm.getXs().getSetting("aileron").getMin();
			double a=joystick.canvasCircle.getLayoutX()-joystick.canvasCircle.getRadius();
			double b=joystick.canvasCircle.getLayoutX()+joystick.canvasCircle.getRadius();
			nv=joystick.NormlaizeJoystic((double)nv.doubleValue() ,max,min,a,b);
			joystick.movingCircle.setLayoutX((double) nv);
		});
		
		joystick.elevator.addListener((o,ov,nv)->{
			double max = vm.getXs().getSetting("elevator").getMax();
			double min = vm.getXs().getSetting("elevator").getMin();
			double a=joystick.canvasCircle.getLayoutY()-joystick.canvasCircle.getRadius();
			double b=joystick.canvasCircle.getLayoutY()+joystick.canvasCircle.getRadius();
			nv=joystick.NormlaizeJoystic((double)nv ,max,min,a,b);
			joystick.movingCircle.setLayoutY((double) nv);
		});
		
		viewlist.list.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->{
			clearSeries(seriesAlgo,seriesAnomaliesFlight,seriesAnomaliesPoints,seriesCor,seriesFeature,seriesRegularFlight);
			selectedCol=nv;
			graphs.Fchart.setTitle(selectedCol);
			vm.FilluntillNow(selectedCol, seriesFeature);
			corlleatedCol=vm.getCorllated(selectedCol);
			if(corlleatedCol==null) {
				graphs.CorChart.setTitle("");
				clearSeries(seriesCor,seriesAlgo,seriesRegularFlight,seriesAnomaliesFlight,seriesAnomaliesPoints);
			}
			else {
				graphs.CorChart.setTitle(corlleatedCol);
				seriesCor.getData().clear();
				vm.FilluntillNow(corlleatedCol, seriesCor);
				if(vm.getAd()!=null && selectedCol!=null && corlleatedCol != null) {
					clearSeries(seriesRegularFlight,seriesAlgo);
					vm.PaintTrainPoints(selectedCol,corlleatedCol,seriesRegularFlight);
					graphs.AlgoChart.getData().remove(0);
					graphs.AlgoChart.getData().add(0, vm.PaintAlgo(selectedCol, corlleatedCol));
				}
			}
			
		});
		
	}
	public void clearSeries(Series...series) {
		for (Series s : series) {
			s.getData().clear();
		}
	}
	
}
