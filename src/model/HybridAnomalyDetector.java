package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import model.Feature;

public class HybridAnomalyDetector implements TimeSeriesAnomalyDetector
{
	protected HashMap<String, Circle> hybrid = new HashMap<>();
	protected HashMap<String, LinearAnomalyDetector> lin = new HashMap<>();
	protected HashMap<String, ZscoreAnomalyDetector> zScore=new HashMap<>();
	private Random rand = new Random();
	protected TimeSeries ts;
	
//An algorithm that chooses according to a measure of correlation how to measure / behave -
//according to the z-score algorithm or according to a linear regression algorithm
	@Override
	public void learnNormal(TimeSeries ts) {
		this.ts = ts;
		ArrayList<Point> points = new ArrayList<>();
		
		ArrayList<MatchFeature> mf = StatLib.FindMatch(ts,0).match;
		
		for (MatchFeature matchFeature : mf) {
			Feature f1 = ts.getFeatureByName2(matchFeature.f1);
			Feature f2 = ts.getFeatureByName2(matchFeature.f2);
			String name=f1.getName()+"-"+f2.getName();
			
			if(Math.abs(matchFeature.correlation)>=0.95)
			{
				TimeSeries t = new TimeSeries(f1,f2);
				LinearAnomalyDetector linear = new LinearAnomalyDetector();
				linear.learnNormal(t);
				lin.put(name,linear);
			}
			else if(Math.abs(matchFeature.correlation)<0.5) {
				TimeSeries t = new TimeSeries(f1,f2);
				ZscoreAnomalyDetector z = new ZscoreAnomalyDetector();
				z.learnNormal(t);
				zScore.put(name, z);
			}
			else {
				TimeSeries t = new TimeSeries(f1,f2);
				for(int i=0;i<f1.size;i++)
				{
					Point p=new Point(f1.samples.get(i),f2.samples.get(i));
					points.add(p);
				}
				hybrid.put(name,findMinimumCircle(points));
				points=new ArrayList<>();
			}
		}
		for (Feature f : StatLib.FindMatch(ts,0).noMatch) {
			TimeSeries t = new TimeSeries(f);
			ZscoreAnomalyDetector z = new ZscoreAnomalyDetector();
			z.learnNormal(t);
			zScore.put(f.name, z);
		}
	}

//The phase of detecting the anomalies - in this phase we will receive a flight file and according to the
//learning we performed we will know for a characteristic whether it deviates from what we learned
//in the preliminary algorithms 
	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		ArrayList<AnomalyReport> resultList = new ArrayList<AnomalyReport>();
		
		for (int i = 0; i < ts.table.size(); i++) {
			for (int j = 0; j < ts.table.size(); j++) {
				Feature f1 = ts.getTable().get(i);
				Feature f2 = ts.getTable().get(j);
				TimeSeries t = new TimeSeries(f1,f2);
				String name= f1.getName()+"-"+f2.getName();
				if(hybrid.containsKey(name)) {
					ArrayList<Point> points = new ArrayList<>();
					for(int k=0;k<f1.size;k++)
					{
						Point p=new Point(f1.samples.get(k),f2.samples.get(k));
						points.add(p);
					}
					int index=1;
					for (Point point : points) {
						if(!this.hybrid.get(name).isContainsPoint(point)) {
							String discription = f1.name_id + "-" + f2.name_id;
							String discription2 = f2.name_id + "-" + f1.name_id;
							AnomalyReport report = new AnomalyReport(discription,index);
							AnomalyReport report2 = new AnomalyReport(discription2,index);
							if(!StatLib.isContain(resultList,report) && !StatLib.isContain(resultList,report2))
								resultList.add(report);
						}
						index++;
					}
				}
				else if(lin.containsKey(name))
				{
					
					List<AnomalyReport> reports =this.lin.get(name).detect(t);
					for (AnomalyReport anomalyReport : reports) {
						AnomalyReport anomalyReport2 = new AnomalyReport(StatLib.ReverseString(anomalyReport.description), anomalyReport.timeStep);
						if(!StatLib.isContain(resultList,anomalyReport) && !StatLib.isContain(resultList,anomalyReport2))
							resultList.add(anomalyReport);
					}
				}
				else if(this.zScore.containsKey(name)){
					List<AnomalyReport> reports =this.zScore.get(name).detect(t);
					for (AnomalyReport anomalyReport : reports) {
						AnomalyReport anomalyReport2 = new AnomalyReport(StatLib.ReverseString(anomalyReport.description), anomalyReport.timeStep);
						if(!StatLib.isContain(resultList,anomalyReport) && !StatLib.isContain(resultList,anomalyReport2))
							resultList.add(anomalyReport);
					}
				}	
			}	
		}
		for (Feature f : StatLib.FindMatch(ts,0).noMatch) {
			if (zScore.containsKey(f.name)) {
				TimeSeries t = new TimeSeries(f);
				List<AnomalyReport> reports =this.zScore.get(f.name).detect(t);
				for (AnomalyReport anomalyReport : resultList) {
					if(!StatLib.isContain(resultList,anomalyReport))
						resultList.add(anomalyReport);	
				}
			}
		}
		resultList.sort((x,y)->{
			return (int) (x.timeStep-y.timeStep);
		});
		return resultList;
	}
	
//input: get a list of points
//output: return the minimum Circle(smallest one) that contains all the points in the list
	public Circle findMinimumCircle(final List<Point> points) {
		return WelezAlgo(points, new ArrayList<Point>());
    }
	
    private Circle WelezAlgo(final List<Point> points, final List<Point> R) {
    	Circle minimumCircle = null;
		
		if (R.size() == 3) {
			minimumCircle = new Circle(R.get(0), R.get(1), R.get(2));
		}
		else if (points.isEmpty() && R.size() == 2) {
			minimumCircle = new Circle(R.get(0), R.get(1));
		}
		else if (points.size() == 1 && R.isEmpty()) {
			minimumCircle = new Circle(points.get(0).x, points.get(0).y, 0);
		}
		else if (points.size() == 1 && R.size() == 1) {
			minimumCircle = new Circle(points.get(0), R.get(0));
		}
		else {
			Point p = points.remove(rand.nextInt(points.size()));
			minimumCircle = WelezAlgo(points, R);
			
			if (minimumCircle != null && !minimumCircle.isContainsPoint(p)) {
				R.add(p);
				minimumCircle = WelezAlgo(points, R);
				R.remove(p);
				points.add(p);
			}
		}
				
		return minimumCircle;
    }

	

	@Override
	public Series paint(String... strings) {
		// TODO Auto-generated method stub
		Series s= new Series();
		String key = strings[0] + "-" + strings[1];
		
		if (hybrid.containsKey(key)) {
			float xcenter = hybrid.get(key).center.x;
			float ycenter = hybrid.get(key).center.y;
			float radius = hybrid.get(key).radius;
			ArrayList<Point> points=new ArrayList<Point>();
			for(double angle=0;angle<360;angle+=0.5)
			{
				float x=(float) (radius*Math.cos(angle)+xcenter);
				float y=(float) (radius*Math.sin(angle)+ycenter);
				points.add(new Point(x, y));
			}
			for (Point point : points) 
				s.getData().add(new XYChart.Data(point.x, point.y));
			return s;
		}
		else if (lin.containsKey(key)) {
			s = lin.get(key).paint(strings);
			return s;
		}
		else if (zScore.containsKey(key)) {
			s = zScore.get(key).paint(strings);
			return s;
		}
		return null;
	}

	@Override
	public String getName() { return "Hybrid";}
	

}
