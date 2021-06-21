package tests;

import java.awt.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import model.AnomalyReport;
import model.TimeSeries;
import model.ZscoreAnomalyDetector;

public class testv1 {


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String[] data = {"4", "5", "6", "4", "5", "6", "4", "5", "6", "4", "5", "6", "4", "5"};
		FileWriter writer = new FileWriter("anomalyTrain.csv");
		writer.write("f1");
		writer.write(System.getProperty( "line.separator" ));
		for (String string : data) {
			writer.write(string + ",");
			writer.write(System.getProperty( "line.separator" ));
		
		}
		writer.close();
		
		String[] data1 = {"5", "5", "5", "-99", "5", "5", "5", "5", "5", "5", "88", "5", "5", "5"};
		FileWriter writer1 = new FileWriter("anomalyTest.csv");
		writer1.write("f1");
		writer1.write(System.getProperty( "line.separator" ));
		for (String string : data1) {
			writer1.write(string + ",");
			writer1.write(System.getProperty( "line.separator" ));
		
		}
		writer1.close();
		
		
		
		
		TimeSeries ts_train = new TimeSeries("anomalyTrain.csv");
		
		ZscoreAnomalyDetector z = new ZscoreAnomalyDetector();
		z.learnNormal(ts_train);
		
		TimeSeries ts_test = new TimeSeries("anomalyTest.csv");
		
		java.util.List<AnomalyReport> Arl =  z.detect(ts_test);
		
		
	}

}
