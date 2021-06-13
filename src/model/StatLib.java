package model;

import java.util.ArrayList;
import java.util.List;

import model.TimeSeries.Feature;

public class StatLib {

	
	
	public static float[] al_to_fl(List<Float> list) {
		float[] arr = new float[list.size()];
		int index = 0;
		for (Float value: list) {
		   arr[index++] = value;
		}	
		return arr;
	
	}
	
	
	// simple average
	public static float avg(float[] x){
		float sum =0;
		for (float f : x) {
			sum = f + sum;
		}
		
		return sum / x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float xavg = avg(x);
		float temp = 0;
		for (float f : x) {
			temp += (f-xavg)*(f-xavg);
		}
		
		return temp / (x.length);
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		float avgx = avg(x);
		float  avgy = avg(y);
		float  sum = 0;
		for (int i = 0; i < y.length; i++) {
			sum += (((x[i] -avgx) * (y[i] - avgy))); 
		}
		
		return sum/x.length;
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float covxy = cov(x, y);
		float varx = var(x);
		float vary = var(y);
		double stdx = Math.sqrt(varx);
		double stdy = Math.sqrt(vary);
		
		return (float) (covxy / (stdx * stdy));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] x = new float[points.length];
		float[] y = new float[points.length];
		int i = 0;
		for (Point p : points) {
			x[i] = p.x;
			y[i] = p.y;
			i++;
		}
		float xavg = avg(x);
		float yavg = avg(y);
		float xvar = var(x);
		float covxy = cov(x, y);
		float a = covxy/xvar;
		float b = yavg - (a * xavg);
		Line l = new Line(a, b);
		
		return l;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		Line lrg = linear_reg(points);
		
		return Math.abs(lrg.f(p.x) - p.y);
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		return Math.abs(l.f(p.x) - p.y);
	}

	public static MatchAndNoMatch FindMatch(TimeSeries ts,double treshold){
		ArrayList<MatchFeature> mfl = new ArrayList<MatchFeature>();
		ArrayList<Feature> noMatch = new ArrayList<Feature>();
		float max =0;
		MatchFeature mf = null ;
		for (int i = 0; i < ts.getTable().size(); i++) {
			TimeSeries.Feature f1 = ts.getTable().get(i);
			for (int j = i+1; j < ts.getTable().size(); j++) {
				if (i != j) {
					TimeSeries.Feature f2 = ts.getTable().get(j);
					float cor = Math.abs(pearson(al_to_fl(f1.getSamples()), al_to_fl(f2.getSamples())));
					if(cor > max && cor > treshold) {
						max = cor;
						mf = new MatchFeature(f1.getName(), f2.getName(), cor);
					}
				}		
			}
			if (mf != null) {mfl.add(mf);}
			else {noMatch.add(f1);}
			max = 0;
			mf= null;
		}
		//mfl.forEach(m ->System.out.println(m.f1+" " + m.f2 +" "+ m.correlation));
		return new MatchAndNoMatch(mfl, noMatch);
	}
	
	//input: an array list of Anomaly Report and an Anomaly Report
	//output: return true if arl contains the ar , else return false
	public static boolean isContain(ArrayList<AnomalyReport> arl, AnomalyReport ar) {
		for (AnomalyReport anomalyReport : arl) {
			if (anomalyReport.description.equals(ar.description) && 
					anomalyReport.timeStep == ar.timeStep)
			{
				return true;
			}
			
		}
		return false;
	}
	
	
	public static String ReverseString(String s) {
	   
	 
	        // getBytes() method to convert string
	        // into bytes[].
	        byte[] strAsByteArray = s.getBytes();
	 
	        byte[] result = new byte[strAsByteArray.length];
	 
	        // Store result in reverse order into the
	        // result byte[]
	        for (int i = 0; i < strAsByteArray.length; i++)
	            result[i] = strAsByteArray[strAsByteArray.length - i - 1];
	 
	        return new String(result);
	    
	}
	
	public static Point[] points_gen(float[] x, float[] y) {
		Point[] p_array = new Point[x.length];
		for (int i = 0; i < p_array.length; i++) {
			p_array[i] = new Point(x[i], y[i]);
		}
		return p_array;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
