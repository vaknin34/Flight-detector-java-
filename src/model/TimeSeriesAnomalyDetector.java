//interface- algorithms implements
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.chart.XYChart.Series;

public interface TimeSeriesAnomalyDetector {
	
	public void learnNormal(TimeSeries ts);
	public List<AnomalyReport> detect(TimeSeries ts);
	public Series  paint(String ...strings );
	public String getName();
}
