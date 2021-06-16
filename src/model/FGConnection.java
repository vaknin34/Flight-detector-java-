//flight gear connection

package model;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class FGConnection
{
	protected XmlSettings flySettings;
	protected Socket client;
	protected PrintWriter OutToServer;
  
	   public FGConnection(XmlSettings setiing) throws UnknownHostException, IOException {
		this.flySettings = setiing;
		client = new Socket(flySettings.host, flySettings.port);
		OutToServer = new PrintWriter(client.getOutputStream());
	   }
	
	public String ftos(ArrayList<Float>line) {
		String out = "";
		for (Float float1 : line) {
			out += float1 + ",";
		}
		String res = out.substring(0, out.length()-1);
		return res;
	}
	
	public void SendCommand(ArrayList<Float> data) {
		OutToServer.println(ftos(data));
		OutToServer.flush();	
	}
	
	public void CloseSocket() {
		try {
			client.close();
			OutToServer.close();
		} catch (IOException e) {}
	}
}
