//z-score algorithm:
//This algorithm is a Univariate algorithm which means: an algorithm that looks for anomalies on any 
//variable in relation to itself only, and not in relation to other variables.
//A score of Z is defined as the distance between the crossing points and a collection of points
//represented by their mean in units of deviations standard.

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.chart.XYChart.Series;

public class ZscoreAnomalyDetector implements TimeSeriesAnomalyDetector{
	ArrayList<Double> t_x = new ArrayList<Double>();

//Preliminary stage - where we take a file of a normal flight and check for each characteristic
	//his behavior during a normal flight
	
	@Override
	public void learnNormal(TimeSeries ts) {
		for (TimeSeries.Feature f : ts.getTable()) {
			double max = -1;
			for (int i = 2; i < f.getSamples().size(); i++) {
				double x_avg = StatLib.avg( StatLib.al_to_fl( f.getSamples().subList(0, i)));
				double std =  Math.sqrt(StatLib.var(StatLib.al_to_fl(f.getSamples().subList(0, i))));
				for (int j = 0; j < i; j++) {
					double z_score;
					if (std != 0 ) {
						z_score = Math.abs( f.getSamples().get(j) - x_avg) / std;
					}
					else {z_score = 0;}
					if (z_score > max) {max = z_score;}
				}	
			}
			t_x.add(max);
			max = -1;
		}
	}
		

//The phase of detecting the anomalies - in this phase we will receive a flight file and according to the
//learning we performed we will know for a characteristic whether it deviates from what we learned
//in the preliminary phase
	
	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		ArrayList<AnomalyReport> arl = new ArrayList<AnomalyReport>();
		// TODO Auto-generated method stub
		for (int i = 0; i < ts.getTable().size(); i++) {
			TimeSeries.Feature f = ts.getTable().get(i);
			for (int j = 2; j < f.getSamples().size(); j++) {
				double x_avg = StatLib.avg( StatLib.al_to_fl( f.getSamples().subList(0, j)));
				double std =  Math.sqrt(StatLib.var(StatLib.al_to_fl(f.getSamples().subList(0, j))));
				if (std != 0 ) {
					for (int k = 0; k < j; k++) {
						double z_score = Math.abs( f.getSamples().get(k) - x_avg) / std;
						if (z_score > t_x.get(i)) {
							AnomalyReport ar = new AnomalyReport(f.getName_id(), k+1);
							if (!StatLib.isContain(arl, ar)) {arl.add(ar);}
						}
					}
				}
			}	
		}
		
		return arl;
	}





	@Override
	public Series paint(String... strings) {
		// TODO Auto-generated method stub
		return null;
	}

}
