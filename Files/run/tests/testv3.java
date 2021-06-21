package tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import model.AlgoLoader;
import model.AnomalyReport;
import model.TimeSeries;
import model.TimeSeriesAnomalyDetector;

public class testv3 {


	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		// TODO Auto-generated method stub
		TimeSeries train = new TimeSeries("trainFile1.csv");
		TimeSeries test = new TimeSeries("testFile1.csv");
		TimeSeriesAnomalyDetector algo1 = new AlgoLoader("ZscoreAnomalyDetector","test.ZscoreAnomalyDetector");
		algo1.learnNormal(train);
		List<AnomalyReport> arr1 =  algo1.detect(test);
		
		
		
		TimeSeriesAnomalyDetector algo2 = new AlgoLoader("LinearAnomalyDetector","test.LinearAnomalyDetector");
		algo2.learnNormal(train);
		List<AnomalyReport> arr2 =  algo2.detect(test);
		
		
		TimeSeriesAnomalyDetector algo3 = new AlgoLoader("HybridAnomalyDetector","test.HybridAnomalyDetector");
		algo3.learnNormal(train);
		List<AnomalyReport> arr3 =  algo3.detect(test);
		
		
		
		
		
		
	}

}
