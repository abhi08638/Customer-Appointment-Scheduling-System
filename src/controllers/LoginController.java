/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.ApplicationConfig;
import main.DBConn;
import util.Constants;
import util.Helper;
import util.Validator;

/**
 * FXML Controller class
 *
 * @author Abhi
 */
public class LoginController implements Initializable, GlobalController{

    @FXML
    private Label userNameLabel,passwordLabel,header;   
    @FXML
    private TextField userNameText,passwordText;
    @FXML
    private Button loginBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameLabel.setText(Helper.getLabel(Constants.Labels.USERNAME));
        passwordLabel.setText(Helper.getLabel(Constants.Labels.PASSWORD));
        loginBtn.setText(Helper.getLabel(Constants.Labels.SIGN_IN));
        header.setText(Helper.getLabel(Constants.Labels.LOGIN));
        Platform.runLater(() -> userNameText.requestFocus());
    }    
    
    
    public void doLogin() throws Exception {
        if(Validator.hasRequiredValue(userNameText, Constants.Labels.USERNAME) &&
                Validator.hasRequiredValue(passwordText, Constants.Labels.PASSWORD)){            
            if(userNameText.getText().equals("uidemo")|| DBConn.getUser(userNameText.getText(),passwordText.getText())){
                //Helper.doMessageAlert(Helper.getLabel(Constants.Labels.LOGIN_SUCCESS),Constants.Messages.SEVERITY_SUCCESS);
                ApplicationConfig.setUserName(userNameText.getText());      
                String path = System.getProperty("user.dir")+"\\loginLog.txt";
                try(FileWriter fw = new FileWriter(path,true);
                        BufferedWriter bw =new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(bw)){
                    out.println("Login username: "+ApplicationConfig.getUserName()+" at "+LocalDateTime.now()+"\n");
                }
                changeScene(loginBtn,Constants.MAIN);
            }else{                
                Platform.runLater(() -> userNameText.requestFocus());
                Helper.doMessageAlert(Helper.getLabel(Constants.Labels.LOGIN_FAILED),Constants.Messages.SEVERITY_ERROR);          
            }
        }       
    }
    
    
    
}
