/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.scene.control.TextField;

/**
 *
 * @author Abhi
 */
public class Validator {
    
    public static boolean hasRequiredValue(TextField t,String fieldName) throws Exception{
        if(!hasValue(t)){            
            Helper.doMessageAlert(Helper.getLabel(fieldName)+Helper.getLabel(Constants.Error.EMPTY),Constants.Messages.SEVERITY_ERROR);        
            return false;
        }
        return true;
    }
    
    public static boolean hasRequiredValue(String val,String fieldName) throws Exception{
        if(!hasValue(val)){            
            Helper.doMessageAlert(Helper.getLabel(fieldName)+Helper.getLabel(Constants.Error.EMPTY),Constants.Messages.SEVERITY_ERROR);        
            return false;
        }
        return true;
    }
    
    public static boolean hasValue(String t){
        if(t==null|| t.trim().equals("")){                       
            return false;
        }
        return true;
    }
    
    public static boolean hasValue(TextField t){
        if(t.getText()==null|| t.getText().trim().equals("")){                       
            return false;
        }
        return true;
    }     
    
    public static boolean isInteger(TextField t,String fieldName) throws Exception{
        try{
            Helper.getInt(t);
            return true;
        }catch(Exception e){
            Helper.throwException(fieldName+" must be a whole number");            
        }
        return false;
    }
    
    public static boolean isDouble(TextField t,String fieldName) throws Exception{
        try{
            Helper.getDouble(t);
            return true;
        }catch(Exception e){
            Helper.throwException(fieldName+" must be a number");            
        }
        return false;
    }            
}
