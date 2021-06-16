package model;

import java.util.ArrayList;

import model.Feature;

public class MatchAndNoMatch {
	
	public ArrayList<MatchFeature> match;
	public ArrayList<Feature> noMatch;
	
	
	public MatchAndNoMatch(ArrayList<MatchFeature> match, ArrayList<Feature> noMatch) {
		super();
		this.match = match;
		this.noMatch = noMatch;
	}
}
