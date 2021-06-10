
package model;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import model.TimeSeries.Feature;



public class LinearAnomalyDetector implements TimeSeriesAnomalyDetector {

	ArrayList<CorrelatedFeatures> cofeatures_ls = new ArrayList<CorrelatedFeatures>();
	TimeSeries ts;

	

//getter
	public List<CorrelatedFeatures> getNormalModel(){return this.cofeatures_ls;}

//----------------------------functions-----------------------------------//

	/*
	public boolean contain(CorrelatedFeatures cof) {
		boolean b = false;
		for (CorrelatedFeatures correlatedFeature : this.cofeatures_ls) {
			if (correlatedFeature.feature1.equals(cof.feature2) && correlatedFeature.feature2.equals(cof.feature1)){
				b = true;
			}
		}
		return b;
	} 
	*/
//Preliminary step - we will take a file of a normal flight and check for each
//characteristic which of the other characteristics is most correlative to it according to pearson	
	public void learnNormal(TimeSeries ts) {
		this.ts=ts;
		MatchAndNoMatch mam = StatLib.FindMatch(ts, 0.9);
		//mam.match.forEach(m -> System.out.println(ts.getFeatureByName2(m.f1).name_id + " "+ ts.getFeatureByName2(m.f2).name_id + " " + m.correlation));
		for (MatchFeature maf : mam.match) {
			Feature f1 = ts.getFeatureByName2(maf.f1);
			Feature f2 = ts.getFeatureByName2(maf.f2);
			float[] mainfeature = StatLib.al_to_fl(f1.samples);
			float[] subfeature = StatLib.al_to_fl(f2.samples);
 			Point[] points = StatLib.points_gen(mainfeature, subfeature);
 			Line lrg = StatLib.linear_reg(points);
 			float threshold = 0;
			for (Point point : points) {
				float dev = StatLib.dev(point, lrg);
				if (threshold < dev) {
					threshold = dev;
				}
			}
			CorrelatedFeatures cof = new CorrelatedFeatures(f1.getName(), f2.getName(), maf.correlation, lrg,(float) (threshold+0.025));
			this.cofeatures_ls.add(cof);
		}
		//System.out.println("");
		//this.cofeatures_ls.forEach(cof -> System.out.println(ts.getFeatureByName2(cof.feature1).name_id + " " + ts.getFeatureByName2(cof.feature2).name_id + " " + cof.corrlation));
		
	
		
		
		
		/*
		System.out.println("linear corrleation");
		float max = 0;
		ArrayList<Feature> f_array = ts.getTable();
		for (int i = 0; i < f_array.size(); i++) {
			float [] mainfeature = StatLib.al_to_fl(f_array.get(i).getSamples());
			int index = 0;
			for (int j = i+1; j < f_array.size()-1; j++) {
				if (i != j) {
					float [] subfeature = StatLib.al_to_fl(f_array.get(j).getSamples());
					float res  = StatLib.pearson(mainfeature, subfeature);
					res = Math.abs(res);
					if (res >= max && res > 0.9) {
						max = res;
						index = j;
					}
				}	
			}
			if (max != 0) {
				float [] subfeature = StatLib.al_to_fl(f_array.get(index).getSamples());
				Point[] points = points_gen(mainfeature, subfeature);
				Line lrg = StatLib.linear_reg(points);
				float threshold = 0;
				for (Point point : points) {
					float dev = StatLib.dev(point, lrg);
					if (threshold < dev) {
						threshold = dev;
					}
				}
				CorrelatedFeatures cof = new CorrelatedFeatures(f_array.get(i).getName(), f_array.get(index).getName(), max, lrg,(float) (threshold+0.025));
				if (!contain(cof)) {
					System.out.println(f_array.get(i).name_id + " " + f_array.get(index).name_id);
					this.cofeatures_ls.add(cof);
				}
			}
			max =0;
			
		}*/
	}

//The phase of detecting the anomalies - in this phase we will receive a flight file and according to the
//learning we performed we will know for a characteristic whether it deviates from what we learned
//in the preliminary phase
	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		ArrayList<AnomalyReport> arl = new ArrayList<AnomalyReport>();
		for (CorrelatedFeatures correlatedFeature : this.cofeatures_ls) {
			ArrayList<Float> t1 = ts.getFeatureByName(correlatedFeature.feature1);
			ArrayList<Float> t2 = ts.getFeatureByName(correlatedFeature.feature2);
			float[] f1 = StatLib.al_to_fl(t1);
			float[] f2 = StatLib.al_to_fl(t2);
			Point[] points =StatLib.points_gen(f1, f2);
			int tindex = 1;
			for (Point p : points) {
				float dev_temp = StatLib.dev(p, correlatedFeature.lin_reg);
				if ( dev_temp > correlatedFeature.threshold) {
					String d = ts.getFeatureByName2(correlatedFeature.feature1).name_id + "-" + ts.getFeatureByName2(correlatedFeature.feature2).name_id;
					AnomalyReport ar = new AnomalyReport(d, tindex);
					//System.out.println(correlatedFeature.corrlation + "linear");
						arl.add(ar);
				}
				tindex++;
			}	
		}
		return arl;
	}

	@Override
	public Series paint(String... strings) {
		String a=strings[0];
		String b=strings[1];
		Line line = null;
		for (CorrelatedFeatures CF : cofeatures_ls) {
			if((a.equals(CF.feature1) && b.equals(CF.feature2)))
				line = CF.lin_reg;
				
		}
		if (line == null) {
			return null;
		}
		Series s = new Series();
		float xMax = ts.getMaxVal(a);
		float xMin = ts.getMinVal(a);
		float yMax = line.f(xMax);
		float yMin = line.f(xMin);
		
		s.getData().add(new XYChart.Data(xMin,yMin));
		s.getData().add(new XYChart.Data(xMax,yMax));
		
		return s;
	}
	

}
