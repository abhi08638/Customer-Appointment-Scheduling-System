/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.time.ZoneId;
import java.util.Locale;

/**
 *
 * @author Abhi
 */
public class ApplicationConfig {

    private static ZoneId defZoneId;

    private static String userName;    
    
    public ApplicationConfig(){
        //Locale.setDefault(Locale.FRANCE);  
        setDefZoneId(ZoneId.systemDefault());                
    }

    public static ZoneId getDefZoneId() {
        return defZoneId;
    }

    public static void setDefZoneId(ZoneId defZoneId) {
        ApplicationConfig.defZoneId = defZoneId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        ApplicationConfig.userName = userName;
    }

}
