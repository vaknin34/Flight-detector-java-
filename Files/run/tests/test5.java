package tests;

import java.io.IOException;
import java.util.List;

import model.AlgoClassLoader;
import model.AnomalyReport;
import model.TimeSeries;

public class test5 {

	public static void main(String[] args) throws IOException
	{
		AlgoClassLoader a = new AlgoClassLoader();
		System.out.println(a.load(""));
		TimeSeries train = new TimeSeries("trainFile1.csv");
		TimeSeries test = new TimeSeries("testFile1.csv");
		a.learnNormal(train);
		List<AnomalyReport> arl = a.detect(test);
		for (AnomalyReport anomalyReport : arl) {
			System.out.println(anomalyReport.description);
			System.out.println(anomalyReport.timeStep);
		}
		
	}

}
