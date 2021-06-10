
package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ViewNameLoader {

	public ArrayList<String> Load(String path){
		Scanner s;
		try {
			s = new Scanner(new BufferedReader(new FileReader(path)));
			ArrayList<String> names=new ArrayList<String>();
			while (s.hasNext()) {
				String string = (String) s.next();
				names.add(string);
			}
			s.close();
			return names;
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		return null;
		
	}
}
