package view.joystick;

import java.io.IOException;

import eu.hansolo.medusa.Gauge;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Joystick extends AnchorPane
{
	public final JoystickController controller;
	public Gauge direction,speed,yaw,roll,pitch,altitude;
	public Slider rudder,throttle;
	public Circle canvasCircle,movingCircle;
	public DoubleProperty aileron,elevator;
	
	public Joystick()
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap = null;
		try {
			ap = fxl.load(getClass().getResource("Joystick.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}

		if(ap!=null) {
			controller=fxl.getController();
			direction=controller.DirectionValue;
			altitude=controller.AltitudeValue;
			speed=controller.speedValue;
			yaw=controller.yawValue;
			pitch=controller.PitchValue;
			roll=controller.RollValue;
			rudder=controller.rudder;
			throttle=controller.throttle;
			movingCircle=controller.movingCircle;
			canvasCircle=controller.CanvasCircle;
			aileron = new SimpleDoubleProperty();
			elevator = new SimpleDoubleProperty();
			this.getChildren().add(ap);
		}
		else
			controller=null;
	}
	public void SetMaxMinForSliders(double...ds)
	{
		controller.rudder.setMax(ds[0]);
		controller.rudder.setMin(ds[1]);
		controller.throttle.setMax(ds[2]);
		controller.throttle.setMin(ds[3]);
	}
	
	public void setMaxMinForClock(double...ds) {
		controller.DirectionValue.setMaxValue(ds[0]);
		controller.DirectionValue.setMinValue(ds[1]);
		
		controller.AltitudeValue.setMaxValue(ds[2]);
		controller.AltitudeValue.setMinValue(ds[3]);
		
		controller.PitchValue.setMaxValue(ds[4]);
		controller.PitchValue.setMinValue(ds[5]);
		
		controller.RollValue.setMaxValue(ds[6]);
		controller.RollValue.setMinValue(ds[7]);
		
		controller.speedValue.setMaxValue(ds[8]);
		controller.speedValue.setMinValue(ds[9]);
		
		controller.yawValue.setMaxValue(ds[10]);
		controller.yawValue.setMinValue(ds[11]);
	}
	
	public double NormlaizeJoystic(double x ,double max,double min,double a,double b) {
		double res=a+((x-min)*(b-a))/(max-min);
		return res;
	}

}
