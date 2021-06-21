package tests;

import java.io.IOException;

import model.XmlComplete;
import model.XmlSettings;

public class test7 {


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		XmlComplete x = new XmlComplete();
		x.WriteXmlToUser();
		XmlSettings xs =    x.LoadSettingsFromClient("settings.xml");
		
		XmlSettings x1 =    x.LoadSettingsFromClient("settings.xml");
		
		
	}

}
