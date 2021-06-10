//Reading and writing to an xml file

package model;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmlWR {
	
// write- to an xml file- the settings the client should fill
	public static void WriteToXML (XmlSettings settings) throws IOException
	{
	    FileOutputStream fos = new FileOutputStream("resources/settings.xml");
	    XMLEncoder encoder = new XMLEncoder(fos);
	    encoder.setExceptionListener(new ExceptionListener() {
	            public void exceptionThrown(Exception e) {
	                System.out.println("Exception! :"+e.toString());
	            }
	    });
	    encoder.writeObject(settings);
	    encoder.close();
	    fos.close();
	}
	
	
//read- the xml file after the client fill all the arguments
	public static XmlSettings deserializeFromXML(String path) throws Exception{
		FileInputStream fis = new FileInputStream(path);
	    XMLDecoder decoder = new XMLDecoder(fis);
	    XmlSettings decodedSettings = (XmlSettings) decoder.readObject();
	    decoder.close();
	    fis.close();
	    return decodedSettings;
	}	
}
