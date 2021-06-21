package tests;

import java.io.IOException;

import model.FGConnection;

public class FGtest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FGConnection f = new FGConnection("resources/reg_flight.csv");
			f.StartPlay();
			f.PlayFlight("localhost",5400,20,1000);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
