package tests;

import java.io.IOException;

import model.XmlComplete;
import model.XmlSettings;

public class test8 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XmlComplete x = new XmlComplete();
		
		try {
			x.WriteXmlToUser();
			XmlSettings xs =  x.LoadSettingsFromClient("resources/settings.xml");
			x.SaveXml(xs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
