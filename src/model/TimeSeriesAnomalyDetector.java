//interface- algorithms implements
package model;

import java.util.List;
import java.util.function.Function;

import javafx.scene.chart.XYChart.Series;

public interface TimeSeriesAnomalyDetector {
	
	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
	Series  paint(String ...strings );
}
