/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Abhi
 */
public class App extends Application {

    private static Object controllerObj;
  
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_PATH + Constants.LOGIN));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.getScene().getStylesheets().setAll(getClass().getResource(Constants.CSS_PATH + "app.css").toString());        
        stage.show();        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {               
        new ApplicationConfig();
        launch(args);
    }  

    public static Object getControllerObj() {
        Object obj = controllerObj;
        controllerObj = null;
        return obj;
    }
    
    public static void setControllerObj(Object o) {
        controllerObj = o;
    }

}
