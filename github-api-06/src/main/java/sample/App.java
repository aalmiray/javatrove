package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL fxml = classLoader.getResource("sample/app1.fxml");
        InputStream stream = classLoader.getResourceAsStream("sample/messages_de.properties");
        PropertyResourceBundle bundle = new PropertyResourceBundle(stream);
        FXMLLoader fxmlLoader = new FXMLLoader(fxml, bundle);

        stage.setScene(new Scene(fxmlLoader.load()));
        stage.sizeToScene();
        stage.show();
    }
}
