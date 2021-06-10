package model;

import java.util.ArrayList;

import model.TimeSeries.Feature;

public class MatchAndNoMatch {
	
	ArrayList<MatchFeature> match;
	ArrayList<Feature> noMatch;
	
	
	public MatchAndNoMatch(ArrayList<MatchFeature> match, ArrayList<Feature> noMatch) {
		super();
		this.match = match;
		this.noMatch = noMatch;
	}
	

}
