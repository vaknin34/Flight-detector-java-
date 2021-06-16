package model;

import java.util.ArrayList;

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
