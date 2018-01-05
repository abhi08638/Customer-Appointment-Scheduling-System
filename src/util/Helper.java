/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import main.App;

/**
 *
 * @author Abhi
 */
public class Helper {

    private static ResourceBundle mybundle = ResourceBundle.getBundle(Constants.Messages.RESOURCE_BUNDLE);

    private static Alert waitAlert = new Alert(Alert.AlertType.NONE, Helper.getLabel(Constants.Labels.WAIT));

    public static String getLabel(String label) {
        try {
            return mybundle.getString(label);
        } catch (Exception e) {
            printException(label + " was not found in the resource bundle for locale " + Locale.getDefault());
        }
        return Constants.Messages.NO_LABEL;
    }

    public static void doMessageAlert(String message, String severity) throws Exception {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        if (severity.equals(Constants.Messages.SEVERITY_SUCCESS)) {
            alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
            alert.getDialogPane().getStyleClass().add("successAlert");
            alert.setHeaderText(Helper.getLabel(Constants.Labels.SUCCESS));
        } else if (severity.equals(Constants.Messages.SEVERITY_ERROR)) {
            alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
            alert.getDialogPane().getStyleClass().add("errorAlert");
            alert.setHeaderText(Helper.getLabel(Constants.Labels.ERROR));
        } else if (severity.equals(Constants.Messages.SEVERITY_INFO)) {
            alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
            alert.getDialogPane().getStyleClass().add("infoAlert");
            alert.setHeaderText(Helper.getLabel(Constants.Labels.ALERT));
        }
        alert.getDialogPane().getStylesheets().add(App.class.getResource(Constants.CSS_PATH + "app.css").toExternalForm());
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            //no input expected
        }
    }

    public static boolean doCancelAlert() throws Exception {
        Alert alert = new Alert(Alert.AlertType.NONE, Helper.getLabel(Constants.Labels.CONFIRM_CANCEL), ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().getStyleClass().add("infoAlert");
        alert.setHeaderText(Helper.getLabel(Constants.Labels.CONFIRM));
        alert.getDialogPane().getStylesheets().add(App.class.getResource(Constants.CSS_PATH + "app.css").toExternalForm());
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }

    public static void doWaitAlert(boolean open,String label) {
        if(label==null){
            label = Constants.Labels.PROCESSING;
        }
        try {
            waitAlert.getDialogPane().getStyleClass().add("infoAlert");
            waitAlert.setHeaderText(Helper.getLabel(label));
            waitAlert.getDialogPane().getStylesheets().add(App.class.getResource(Constants.CSS_PATH + "app.css").toExternalForm());
            if (open) {
                waitAlert.show();
            } else {
                waitAlert.getButtonTypes().add(ButtonType.CANCEL);
                waitAlert.hide();
                waitAlert.getButtonTypes().remove(ButtonType.CANCEL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean doDeleteAlert() throws Exception {
        Alert alert = new Alert(Alert.AlertType.NONE, Helper.getLabel(Constants.Labels.CONFIRM_DELETE), ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().getStyleClass().add("infoAlert");
        alert.setHeaderText(Helper.getLabel(Constants.Labels.CONFIRM));
        alert.getDialogPane().getStylesheets().add(App.class.getResource(Constants.CSS_PATH + "app.css").toExternalForm());
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }  

    public static void printException(String message) {
        CustomMessage e = new CustomMessage(message);
        e.printStackTrace();
    }

    public static void throwException(String message) throws Exception {
        doMessageAlert(message, Constants.Messages.SEVERITY_ERROR);
        throw new CustomMessage(message);
    }

    public static int getInt(TextField t) throws Exception {
        return Integer.parseInt(t.getText());
    }

    public static double getDouble(TextField t) throws Exception {
        String doubleVal = t.getText().trim();
        if (doubleVal.contains("$")) {
            doubleVal = doubleVal.substring(doubleVal.indexOf("$") + 1);
        }
        return Double.parseDouble(doubleVal);
    }

}
