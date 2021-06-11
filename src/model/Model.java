package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Model extends Observable {

	Timer t = null;
	IntegerProperty timeStep;
	int rate = 0;
	TimeSeriesAnomalyDetector ad;
	XmlSettings ClientSettings;
	TimeSeries Train = null;
	TimeSeries Test = null;
	FGConnection fg;
	
	double aileron ;
	double elevators;
	double rudder;
	double throttle;
	double pitch;
	double yaw;
	double direction ;
	double speed;
	double roll;
	double height;
	boolean hasPause=true;
	boolean hasStop = true;
	boolean flagConnect=false;
	boolean flightStart=false;
	
	public Model(IntegerProperty timeStep,DoubleProperty rate) {
		this.timeStep=timeStep;
		this.rate=(int) (100/rate.doubleValue());
	}
	
	public void play() {
		//Making sure there won't be play twice
		if(t==null) {
			if(Test!=null) {
				flightStart=true;
				hasPause=false;
				hasStop=false;
				t=new Timer();
				t.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						if(flagConnect) {
							System.out.println("here");
							System.out.println("sending row "+timeStep.get());
							System.out.println("rate: "+rate);
							if(timeStep.getValue()+1>Test.NumOfRows) {
								stop();
							}
							else{
								updateValues(timeStep.getValue());
								fg.SendCommand(Test.readLine(timeStep.get()));
								timeStep.set(timeStep.getValue()+1);
							}
						}
						else {
							//System.out.println("sending row "+timeStep.get());
							//System.out.println("rate: "+rate);
							if(timeStep.getValue()+1>Test.NumOfRows) {
								stop();
							}else {
							updateValues(timeStep.getValue());
							timeStep.set(timeStep.getValue()+1);}
						}
					}
				}, 0,this.getRate());
			}
			else {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Can't play flight");
				a.setContentText("Unable to play flight, please load xml,train csv and test csv.");
				a.showAndWait();
			}
		}
	}
	public void pause() {
		if(Test!=null) {
			if(hasStop==false && hasPause==false && flightStart == true) {
				t.cancel();
				hasPause=true;
				//To allow to play after pause
				t=null;
			}
		}
	}
	public void stop() {
		if(Test!=null) {
			if(hasStop==false) {
				if(hasPause==false)t.cancel();
				t=null;
				timeStep.set(0);
				hasStop=true;
				resetValues();
			}
		}
	}
	public void forward() {
		if(Test!=null && timeStep.get()+20<=Test.NumOfRows)
			timeStep.set(timeStep.get()+20);
	}
	public void doubleForward() {
		if(Test!= null && timeStep.get()+40<=Test.NumOfRows)
			timeStep.set(timeStep.get()+40);
	}
	public void backward() {
		if(timeStep.get()-20>=0)
			timeStep.set(timeStep.get()-20);
	}
	public void doubleBackward() {
		if(timeStep.get()-40>=0)
			timeStep.set(timeStep.get()-40);
	}
	
	/*----------------------Getting and Setting--------------------*/
	
	public void setRate(double rate) { this.rate=(int) (100/rate); System.out.println("change");}
	public int getRate() {return this.rate;}
	
	public void setAlieron(double value) {this.aileron=value; this.setChanged(); this.notifyObservers("aileron");}
	public void setElevators(double value) {this.elevators=value; this.setChanged();this.notifyObservers("elevators");}
	public void setRudder(double value) {this.rudder=value; this.setChanged();this.notifyObservers("rudder");}
	public void setThrottle(double value) {this.throttle=value;this.setChanged();this.notifyObservers("throttle");}
	public void setYaw(double value) {this.yaw=value;this.setChanged();this.notifyObservers("yaw");}
	public void setPitch(double value) {this.pitch=value;this.setChanged();this.notifyObservers("pitch");}
	public void setRoll(double value) {this.roll=value;this.setChanged();this.notifyObservers("roll");}
	public void setHeigth(double value) {this.height=value;this.setChanged();this.notifyObservers("heigth");}
	public void setDirection(double value) {this.direction=value;this.setChanged();this.notifyObservers("direction");}
	public void setSpeed(double value) {this.speed=value;this.setChanged();this.notifyObservers("speed");}
	public void setClientSettings(XmlSettings clientSettings) {ClientSettings = clientSettings;}
	public void setTrain(TimeSeries train) { Train = train;}
	public void setTest(TimeSeries test) {Test = test;resetValues();}
	
	public double getAileron() {return aileron;}
	public double getRudder() {return rudder;}
	public double getElevators() {return elevators;}
	public double getThrottle() {return throttle;}
	public double getPitch() {return pitch;}
	public double getYaw() {return yaw;}
	public double getDirection() {return direction;}
	public double getSpeed() {return speed;}
	public boolean getIsFlightStart() {return flightStart;}



	public double getRoll() {return roll;}
	public double getHeigth() {return height;}
	public XmlSettings getClientSettings() {return ClientSettings;}
	

	


	public void updateValues(int i) {
		setAlieron(Test.getSepecificValue(getClientSettings().getAssociate("aileron"),i));
		setElevators(Test.getSepecificValue(getClientSettings().getAssociate("elevator"),i));
		setRudder(Test.getSepecificValue(getClientSettings().getAssociate("rudder"), i));
		setThrottle(Test.getSepecificValue(getClientSettings().getAssociate("throttle"), i));
		setYaw(Test.getSepecificValue(getClientSettings().getAssociate("yaw"), i));
		setPitch(Test.getSepecificValue(getClientSettings().getAssociate("pitch"), i));
		setRoll(Test.getSepecificValue(getClientSettings().getAssociate("roll"), i));
		setHeigth(Test.getSepecificValue(getClientSettings().getAssociate("heigth"), i));
		setDirection(Test.getSepecificValue(getClientSettings().getAssociate("direction"), i));
		setSpeed(Test.getSepecificValue(getClientSettings().getAssociate("speed"), i));
	}
	
	public void resetValues() {
		double x = (this.ClientSettings.getSetting("aileron").getMax()+this.ClientSettings.getSetting("aileron").getMin())/2;
		setAlieron(x);
		x=(this.ClientSettings.getSetting("elevator").getMax()+this.ClientSettings.getSetting("elevator").getMin())/2;
		setElevators(x);
		setRudder(0);
		setThrottle(0);
		setYaw(0);
		setPitch(0);
		setRoll(0);
		setHeigth(0);
		setDirection(0);
		setSpeed(0);
	}
	
	public String getCor(String selctedCol) {
		String cor = null;
		MatchAndNoMatch mAm = StatLib.FindMatch(Train,0);
		ArrayList<MatchFeature> Ma = mAm.match;
		for (MatchFeature matchFeature : Ma) {
			String name1 = Test.getFeatureByName2(matchFeature.f1).name_id;
			String name2 = Test.getFeatureByName2(matchFeature.f2).name_id;
			if (name1.equals(selctedCol)) return name2;
		}
		return null;
	}
	
	
	public void learnData() {this.ad.learnNormal(Train);}
	public List<AnomalyReport> DetectData() {return ad.detect(Test);}
	
	//make the connection the the flight gear
	public void Connect() {
		try {
			this.fg = new FGConnection(ClientSettings);
			flagConnect = true;
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Flight Gear Connected");
			a.showAndWait();
		} catch (Exception e) {
			flagConnect =false; 
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Flight Gear Could not connected");
			a.showAndWait();}	
	}	
	//Disconnect from flight gear
	public void disConnect() {
			if(flagConnect==true) {
			try {
				this.fg.CloseSocket();
				this.fg = null;
				flagConnect = false;
			} catch (Exception e) {flagConnect =true;}	
		}
	}

	public void setAnomalyDetector(TimeSeriesAnomalyDetector ad) {
		this.ad=ad;
	}

	public Series AlgoPaint(String...strings) {
		String indexCol1;
		String indexCol2 = null;
		if (strings[0] != null && strings[1] != null) {
			indexCol1 = Test.getFeatureByNameid(strings[0]).name;
			indexCol2 = Test.getFeatureByNameid(strings[1]).name;
		}
		else {
			indexCol1 = Test.getFeatureByNameid(strings[0]).name;
		}
		
		return this.ad.paint(indexCol1,indexCol2);
	}
}
