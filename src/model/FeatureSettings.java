package model;

import java.io.Serializable;

public class FeatureSettings  implements Serializable{
		public FeatureSettings() {};
		private String real_col_name;
		private String assosicate_name;
		private double min;
		private double max;
		
	//-----------------------setters& getters------------------------------------//	
		public String getReal_col_name() {return real_col_name;}
		public void setReal_col_name(String real_col_name) {this.real_col_name = real_col_name;}
		public String getAssosicate_name() {return assosicate_name;}
		public void setAssosicate_name(String assosicate_name) {this.assosicate_name = assosicate_name;}
		public double getMin() {return min;}
		public void setMin(double min) {this.min = min;}
		public double getMax() {return max;}
		public void setMax(double max) {this.max = max;}
		
	//------------------------------------------------------------------------------//
		@Override
		    public String toString() {
		        return "FeatureSettings [real_col_name=" + real_col_name + ", assosicate_name=" + assosicate_name
		                + ", min=" + min + ", max=" + max  + "]";
		    }
	}
