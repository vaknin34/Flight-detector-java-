package view.joystick;

import eu.hansolo.medusa.Gauge;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.control.Slider;

public class JoystickController {

	@FXML Circle CanvasCircle;
	@FXML Circle movingCircle;
	@FXML Slider rudder;
	@FXML Slider throttle;
	@FXML Gauge DirectionValue;
	@FXML Gauge AltitudeValue;
	@FXML Gauge yawValue;
	@FXML Gauge speedValue;
	@FXML Gauge PitchValue;
	@FXML Gauge RollValue;
	

}
