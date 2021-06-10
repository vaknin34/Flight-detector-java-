package model;

public class Point {
	public final float x,y;
	
	public Point(float x, float y) {
		this.x=x;
		this.y=y;
	}
	
	//Calculate the distance squared
	public float distanceSquared(final Point p) {
		float disX = this.x - p.x;
		float disY = this.y - p.y;
		
		return (float) (Math.pow(disX,2) + Math.pow(disY,2));
	}
	
	//Calculate the distance
	public float distance(final Point p) {
		return (float) Math.sqrt(distanceSquared(p));
	}
	
	//Checking if the three points are collinear
	public static boolean areColinear(final Point p1, final Point p2, final Point p3) {
		float temp1 = p2.y - p3.y;
		float temp2 = p3.y - p1.y;
		float temp3 = p1.y - p2.y;
		return Math.abs(p1.x * temp1 + p2.x * temp2 + p3.x * temp3) == 0.0;
	}
	
}
