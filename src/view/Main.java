package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import model.XmlComplete;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			XmlComplete xml = new XmlComplete();
			xml.WriteXmlToUser();
			FXMLLoader fxl = new FXMLLoader();
			BorderPane root = fxl.load(getClass().getResource("Window.fxml").openStream());
			WindowController wc = fxl.getController();
			wc.init();
			
			Scene scene = new Scene(root,1340,750);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
