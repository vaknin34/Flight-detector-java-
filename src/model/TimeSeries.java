//
package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class TimeSeries {
	
	//
	public class Feature{
		public ArrayList<Float> samples = new ArrayList<Float>();
		public String name; //{1,,,n}
		public int size;
		public String name_id; //{t1,t2,,tn}
		
		//-----------------------------setters&getters-----------------------------//
		protected String getName_id() {return name_id;}
		
		protected void setName_id(String name_id) {this.name_id = name_id;}
		
		public Feature(String name) {this.name_id = name;}
		
		public Feature(ArrayList<Float> samples) {this.samples = samples;}
		
		public ArrayList<Float> getSamples() {return samples;}
		
		public void setSamples(ArrayList<Float> samples) {this.samples = samples;}
		
		public String getName() {return name;}
		
		public void setName(String name) {this.name = name;}
		
		//-----------------------------constructor---------------------------------//
		public Feature() {
			super();
		}
		//--------------------------------------------------------------------------//
		public void add_sample(Float sam) {
			this.samples.add(sam);
			this.size++;
		}
		
	}
	
	public String csvFileName;
	public ArrayList<Feature> table = new ArrayList<Feature>();
	public ArrayList<String> ColNames = new ArrayList<String>();
	public int NumOfRows = 0;
	
//--------------------------constructors----------------------------------------//	
	public TimeSeries(String csvFileName) {
		this.csvFileName = csvFileName;
		try {
			Path pathToFile = Paths.get(csvFileName);
			BufferedReader br = Files.newBufferedReader(pathToFile,
					StandardCharsets.US_ASCII);
			String line = br.readLine();
			String[] titles = line.split(",");
			int k = 0;
			for (String title : titles) {
				Feature temp  = new Feature(title);
				this.ColNames.add(title);
				temp.name = "" + k;
				this.table.add(temp);
				k++;
			}
			line = br.readLine();
			while (line != null) {
				String[] data = line.split(",");
				for (int i = 0; i < data.length; i++) {
					float f = Float.parseFloat(data[i]);
					table.get(i).add_sample(f);
				}
				line = br.readLine();
			}
			br.close();
			this.NumOfRows = this.table.get(0).getSamples().size();
		}catch (Exception e) {
			// TODO: handle exception
			this.table = null;
		}
	}
	
	public TimeSeries() {
		super();
	}


	public TimeSeries(Feature ... features) {
		ArrayList<Feature> fs = new ArrayList<TimeSeries.Feature>();
		for (Feature feature : features) {
			fs.add(feature);
		}
		this.table = fs;
		
	}
//-----------------------------------getters----------------------------------//
	public ArrayList<Feature> getTable() {return table;}

	public ArrayList<Float> getFeatureByName(String s){
		for (Feature feature : table) {
			String n = feature.getName(); 
			if (n.equals(s)) {
				return feature.getSamples();	
			}
		}
		return null;	
	}
	
	public Feature getFeatureByName2(String s){
		for (Feature feature : table) {
			String n = feature.getName(); 
			if (n.equals(s)) {
				return feature;	
			}
		}
		return null;	
	}
	
	public Feature getFeatureByNameid(String s){
		for (Feature feature : table) {
			String n = feature.getName_id(); 
			if (n.equals(s)) {
				return feature;	
			}
		}
		return null;	
	}
	
	public float getSepecificValue(String ColName , int TimeStemp) {
		Feature f = this.getFeatureByNameid(ColName);
		if (f == null) {
			return (Float) null;
		}
		if (TimeStemp > f.getSamples().size()) {
			return (Float) null;
		}
		return f.getSamples().get(TimeStemp);
	}
	
	public ArrayList<Float> readLine(int index){
		ArrayList<Float> line = new ArrayList<Float>();
		for (int i = 0; i < this.ColNames.size(); i++) {
			line.add(this.table.get(i).getSamples().get(index));
		}
		return line;
	}
	
	public int getColIndex(String s) {
		for (int i = 0; i < ColNames.size(); i++) {
			if (ColNames.get(i).equals(s)) {
				return i;
			}
		}
		return -1;
	}
	public float getMinVal(String selctedCol) {
		Feature f = getFeatureByNameid(selctedCol);
		if (f!=null) {
			float min = f.getSamples().get(0);
			for (int i = 1; i < f.samples.size(); i++) {
				if (f.samples.get(i) < min) {
					min  = f.samples.get(i);
				}
				
			}
			return min;
		}
		
		return 0;
	}
	public float getMaxVal(String selctedCol) {
		Feature f = getFeatureByNameid(selctedCol);
		if (f!=null) {
			float max = f.getSamples().get(0);
			for (int i = 1; i < f.samples.size(); i++) {
				if (f.samples.get(i) > max) {
					max = f.samples.get(i);
				}
				
			}
			return max;
		}
		return 0;
	}
		
}


