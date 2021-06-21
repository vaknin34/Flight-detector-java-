package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.AnomalyReport;
import model.HybridAnomalyDetector;
import model.LinearAnomalyDetector;
import model.TimeSeries;
import model.ZscoreAnomalyDetector;

public class testv2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TimeSeries train = new TimeSeries("A://java-final-project//resources/a.csv");
		TimeSeries test = new TimeSeries("A://java-final-project//resources/b.csv");
		ZscoreAnomalyDetector z = new ZscoreAnomalyDetector();
		z.learnNormal(train);
		List<AnomalyReport> a = z.detect(test);
		LinearAnomalyDetector lin = new LinearAnomalyDetector();
		lin.learnNormal(train);
		a=lin.detect(test);
		HybridAnomalyDetector h = new HybridAnomalyDetector();
		// need to fix hybrid
		h.learnNormal(train);
		a=h.detect(test);
		
		for (AnomalyReport anomalyReport : a) {
			System.out.println(anomalyReport.description + "\n");
			System.out.println(anomalyReport.timeStep + "\n");
		}
		
	}

}
