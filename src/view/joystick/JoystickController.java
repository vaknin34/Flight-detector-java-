package view.joystick;

import eu.hansolo.medusa.Gauge;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.control.Slider;

public class JoystickController {

	@FXML protected Circle CanvasCircle;
	@FXML protected Circle movingCircle;
	@FXML protected Slider rudder;
	@FXML protected Slider throttle;
	@FXML protected Gauge DirectionValue;
	@FXML protected Gauge AltitudeValue;
	@FXML protected Gauge yawValue;
	@FXML protected Gauge speedValue;
	@FXML protected Gauge PitchValue;
	@FXML protected Gauge RollValue;
	

}
