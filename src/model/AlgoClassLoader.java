package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.chart.XYChart.Series;

public class AlgoClassLoader implements FileLoader<String>,TimeSeriesAnomalyDetector {
	AlgoLoader a;
	
	@Override
	public String load(String path) {
		String input, className="test.";
		try {
			System.out.println("Please enter a class directory ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			input = in.readLine();
			input+="/";
			System.out.println("Enter the class name");
			className += in.readLine();
			in.close();
			a = new AlgoLoader(input, className);
		} catch (Exception e) {return "Failed to load Class";}
		return "Load Success!";
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		// TODO Auto-generated method stub
		this.a.learnNormal(ts);
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		// TODO Auto-generated method stub
		return this.a.detect(ts);
	}



	@Override
	public Series paint(String... strings) {
		// TODO Auto-generated method stub
		return a.algo.paint(strings);
	}


}
