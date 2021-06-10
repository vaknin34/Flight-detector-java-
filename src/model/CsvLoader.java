package model;

public class CsvLoader implements FileLoader<TimeSeries>{

	@Override
	public TimeSeries load(String path) {
		TimeSeries ts = new TimeSeries();
		return ts;
	}

}
