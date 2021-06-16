package view;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.MultipleSelectionModel;
import view.viewlist.Viewlist;
import viewModel.ViewModel;
import view.openfiles.Openfiles;
import view.graphs.Graphs;
import view.joystick.Joystick;
import view.buttons.Buttons;

public class WindowController {

	@FXML public Viewlist viewlist;
	@FXML public Openfiles openfiles;
	@FXML public Graphs graphs;
	@FXML public Joystick joystick;
	@FXML public Buttons buttons;
	
	public Series seriesFeature;
	public Series seriesCor;
	public Series seriesAlgo;
	public Series seriesRegularFlight;
	public Series seriesAnomaliesFlight;
	public Series seriesAnomaliesPoints;
	
	String selectedCol,corlleatedCol;
	
	
	public void init() {
		ViewModel vm = new ViewModel();
		
		buttons.controller.onPlay=vm.Play;
		buttons.controller.onPause=vm.Pause;
		buttons.controller.onStop=vm.Stop;
		buttons.controller.clearGraphs=()->clearSeries(seriesAnomaliesFlight,seriesAnomaliesPoints,seriesCor,seriesFeature);
		buttons.controller.clearSelect=()->viewlist.list.getSelectionModel().clearSelection();
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
		seriesFeature.setName("Feature");
		seriesCor = new Series();
		seriesCor.setName("Correlated");;
		seriesAlgo = new Series();
		seriesAlgo.setName("Anomaly Algo");
		seriesRegularFlight = new Series();
		seriesRegularFlight.setName("Regular Flight");
		seriesAnomaliesFlight = new Series();
		seriesAnomaliesFlight.setName("Anomaly Flight");
		seriesAnomaliesPoints = new Series();
		seriesAnomaliesPoints.setName("Anomalies Points");
		
		
		
		graphs.Fchart.getData().add(seriesFeature);
		graphs.Fchart.setAnimated(false);
		graphs.CorChart.getData().add(seriesCor);
		
		graphs.CorChart.setAnimated(false);
		graphs.AlgoChart.getData().addAll(seriesAlgo,seriesRegularFlight,seriesAnomaliesFlight,seriesAnomaliesPoints);
		graphs.AlgoChart.setAnimated(false);
		
		selectedCol=null;
		corlleatedCol=null;
		
/*           Binding and Listener              */
		
		vm.rate.bindBidirectional(buttons.videoSpeed.valueProperty());
		buttons.timeSlider.valueProperty().bindBidirectional(vm.timeStep);		
		buttons.videoTime.textProperty().bind(vm.videoTime);
		
		vm.timeStep.addListener((o,ov,nv)->{
			if (nv.intValue() == vm.getTest().NumOfRows) {
				if(Thread.currentThread().getName().equals("JavaFx Application Thread"))
					clearSeries(seriesAlgo,seriesAnomaliesFlight,seriesAnomaliesPoints,seriesCor,seriesFeature,seriesRegularFlight);
				else
					Platform.runLater(()->clearSeries(seriesAlgo,seriesAnomaliesFlight,seriesAnomaliesPoints,seriesCor,seriesFeature,seriesRegularFlight));
			}
			if(selectedCol!=null) {
				if(ov.intValue()+1==nv.intValue()) 
						vm.paintFeature(selectedCol, nv, seriesFeature);
				else if(nv.intValue() != 0) {
					clearSeries(seriesFeature);
					vm.FilluntillNow(selectedCol, seriesFeature);
				}
			
				else
					Platform.runLater(()->clearSeries(seriesFeature));
			
			if(corlleatedCol!=null) {
				if(ov.intValue()+1==nv.intValue())
					vm.paintFeature(corlleatedCol, nv, seriesCor);
				else if(nv.intValue() != 0) {
					clearSeries(seriesCor);
					vm.FilluntillNow(corlleatedCol, seriesCor);
				}
			}
			else {
				if(Thread.currentThread().getName().equals("JavaFx Application Thread"))
					clearSeries(seriesCor);
				}
			if (selectedCol != null && corlleatedCol != null && vm.getAd() != null && vm.getAd().getName().equals("Linear")) {
				vm.PaintTestPoints(selectedCol, corlleatedCol, nv.intValue(),seriesAnomaliesFlight ,seriesAnomaliesPoints);
 			}
			else if (selectedCol != null && vm.getAd() != null && vm.getAd().getName().equals("Zscore")) {
				vm.PaintTestZscorePoints(selectedCol, nv.intValue(),seriesAnomaliesFlight ,seriesAnomaliesPoints);
			}
			else if(vm.getAd() != null && vm.getAd().getName().equals("Hybrid")) {
				if((selectedCol!=null&&corlleatedCol==null) || (selectedCol!=null && corlleatedCol!=null &&vm.getCoraleted(selectedCol,corlleatedCol)<0.5))
					vm.PaintTestZscorePoints(selectedCol, nv.intValue(),seriesAnomaliesFlight ,seriesAnomaliesPoints);
				else if((selectedCol!=null && corlleatedCol!=null &&vm.getCoraleted(selectedCol,corlleatedCol)>=0.5))
					vm.PaintTestPoints(selectedCol, corlleatedCol, nv.intValue(),seriesAnomaliesFlight ,seriesAnomaliesPoints);
					
			}
			if(nv.intValue()==0) {
				Platform.runLater(()->{
					viewlist.list.getSelectionModel().clearSelection();
					graphs.CorChart.setTitle(null);
				});
				
			}
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
				if(nv.length() > 6) {
					graphs.AlgoChart.setTitle(nv.substring(6));
					if(viewlist.list.getSelectionModel()!=null) {
						String index =viewlist.list.getSelectionModel().getSelectedItem();
						viewlist.list.selectionModelProperty().get().clearSelection();
						graphs.CorChart.setTitle(null);
					}
				}
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
			
			graphs.FchartX.setUpperBound(vm.getTest().NumOfRows);
			graphs.FchartX.setLowerBound(0);
			graphs.FchartX.setAutoRanging(false);
			graphs.FchartX.setTickUnit(10);
			
			graphs.FchartY.setUpperBound((int)vm.getTest().getMaxVal(selectedCol)+1);
			graphs.FchartY.setLowerBound((int)vm.getTest().getMinVal(selectedCol)-1);
			graphs.FchartY.setAutoRanging(false);
			graphs.FchartY.setTickUnit(10);
			
			
			graphs.Fchart.setTitle(selectedCol);
			if(selectedCol!=null) {
				vm.FilluntillNow(selectedCol, seriesFeature);
				corlleatedCol=vm.getCorllated(selectedCol);
				if(corlleatedCol==null) {
					graphs.CorChart.setTitle("");
					clearSeries(seriesCor);
				}
				else {
					graphs.CorxAxis.setUpperBound(vm.getTest().NumOfRows);
					graphs.CorxAxis.setLowerBound(0);
					graphs.CorxAxis.setAutoRanging(false);
					graphs.CorxAxis.setTickUnit(10);
					
					graphs.CoryAxis.setUpperBound((int)vm.getTest().getMaxVal(corlleatedCol)+1);
					graphs.CoryAxis.setLowerBound((int)vm.getTest().getMinVal(corlleatedCol)-1);
					graphs.CoryAxis.setAutoRanging(false);
					graphs.CoryAxis.setTickUnit(10);
					
					graphs.CorChart.setTitle(corlleatedCol);
					clearSeries(seriesCor);
					vm.FilluntillNow(corlleatedCol, seriesCor);
				}
			if(vm.getAd() != null && corlleatedCol != null && vm.getAd().getName().equals("Linear")) {
				clearSeries(seriesRegularFlight,seriesAlgo);
				vm.PaintTrainPoints(selectedCol,corlleatedCol,seriesRegularFlight);
			    vm.PaintAlgo(selectedCol, corlleatedCol,seriesAlgo);
			}
			if(vm.getAd() != null && vm.getAd().getName().equals("Zscore")) {
				clearSeries(seriesRegularFlight,seriesAlgo);
				vm.PaintZscoreTrain(selectedCol,seriesRegularFlight);
			    vm.PaintAlgo(selectedCol, corlleatedCol,seriesAlgo);
			}
			if(vm.getAd() != null && vm.getAd().getName().equals("Hybrid")) {
				clearSeries(seriesRegularFlight,seriesAlgo);
				if((selectedCol!=null&&corlleatedCol==null) || (selectedCol!=null && corlleatedCol!=null &&vm.getCoraleted(selectedCol,corlleatedCol)<0.5))
					vm.PaintZscoreTrain(selectedCol, seriesFeature);
				else if((selectedCol!=null && corlleatedCol!=null &&vm.getCoraleted(selectedCol,corlleatedCol)>=0.5))
					vm.PaintTrainPoints(selectedCol,corlleatedCol,seriesRegularFlight);
			    vm.PaintAlgo(selectedCol, corlleatedCol,seriesAlgo);
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
