package view.buttons;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;

public class ButtonsController
{

	@FXML Button doubleback;
	@FXML Button back;
	@FXML Button play;
	@FXML Button pause;
	@FXML Button stop;
	@FXML Button forward;
	@FXML Button doubleforward;
	@FXML ChoiceBox<Number> videoSpeed;
	@FXML Slider videoSlider;
	@FXML Label VideoTime;
	
	public Runnable onPlay,onPause,onStop,onForward,onBackward,onDoubleForward,onDoubleBackward,clearGraphs,clearSelect;
	
	@FXML public void SkipbackwardDouble() {if(onDoubleBackward!=null) onDoubleBackward.run();}
	@FXML public void Skipbackward() {if(onBackward!=null) onBackward.run();}
	@FXML public void startFlight() {if(onPlay!=null) onPlay.run();}
	@FXML public void pauseFlight() {if(onPause!=null) onPause.run();}
	@FXML public void stopFlight() {if(onStop!=null) onStop.run(); 
									if(clearGraphs!=null)clearGraphs.run();
									if(clearSelect!=null) clearSelect.run();}
	@FXML public void SkipForward() {if(onForward!=null) onForward.run();}
	@FXML public void SkipForwardDouble() {if(onDoubleForward!=null) onDoubleForward.run();}

	

}
