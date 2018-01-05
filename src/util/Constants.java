/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Abhi
 */
public class Constants {    
    public final static String FXML_PATH="/fxml/";
    public final static String CSS_PATH="/css/";
    public final static String SAVE_SUCCESS="Saved Successfully!";
    public final static String MAIN="Main.fxml"; 
    public final static String LOGIN="Login.fxml"; 
    public final static String CUSTOMER="Customers.fxml"; 
    public final static String APPOINTMENT="Appointments.fxml"; 
    public final static String AM="AM"; 
    public final static String PM="PM"; 
    
    public class Appointment{
        public final static String CONTINUING="appointment.continuing";
        public final static String TERM="appointment.term";
        public final static String CONTRACT="appointment.contract";
        public final static String CUSTOMER_MAP="customers";
        public final static String APPOINTMENT_DATA="data";
    }
    
    public class Messages{
        public final static String RESOURCE_BUNDLE="Messages";
        public final static String NO_LABEL="Missing Label:";        
        public final static String PREFIX_LABEL="label.";
        public final static String PREFIX_ERROR="error.";
        public final static String SEVERITY_ERROR="error";
        public final static String SEVERITY_INFO="info";
        public final static String SEVERITY_SUCCESS="success";
    }
    
    public class Labels{
        public final static String USERNAME=Messages.PREFIX_LABEL+"username";
        public final static String PASSWORD=Messages.PREFIX_LABEL+"password";
        public final static String LOGIN=Messages.PREFIX_LABEL+"login";
        public final static String SIGN_IN=Messages.PREFIX_LABEL+"signIn";
        public final static String LOGIN_FAILED=Messages.PREFIX_LABEL+"loginFailed";
        public final static String LOGIN_SUCCESS=Messages.PREFIX_LABEL+"loginSuccess";
        public final static String CUSTOMER_INFO=Messages.PREFIX_LABEL+"customerInfo";
        public final static String CUSTOMER_NAME=Messages.PREFIX_LABEL+"custName";
        public final static String CUSTOMER_ADDR1=Messages.PREFIX_LABEL+"custAddr1";
        public final static String CUSTOMER_ADDR2=Messages.PREFIX_LABEL+"custAddr2";
        public final static String CUSTOMER_CITY=Messages.PREFIX_LABEL+"custCity";
        public final static String CUSTOMER_COUNTRY=Messages.PREFIX_LABEL+"custCountry";
        public final static String CUSTOMER_ZIP=Messages.PREFIX_LABEL+"custZip";
        public final static String CUSTOMER_PHONE=Messages.PREFIX_LABEL+"custPhone";
        public final static String CUSTOMER_ACTIVE=Messages.PREFIX_LABEL+"custActive";
        public final static String SUCCESS=Messages.PREFIX_LABEL+"success";
        public final static String ERROR=Messages.PREFIX_LABEL+"error";
        public final static String ALERT=Messages.PREFIX_LABEL+"alert";
        public final static String SAVE=Messages.PREFIX_LABEL+"save";
        public final static String SAVE_SUCCESS=Messages.PREFIX_LABEL+"saveSuccess"; 
        public final static String CONFIRM_CANCEL=Messages.PREFIX_LABEL+"confirmCancel"; 
        public final static String CONFIRM_DELETE=Messages.PREFIX_LABEL+"confirmDelete"; 
        public final static String CANCEL=Messages.PREFIX_LABEL+"cancel";
        public final static String WAIT=Messages.PREFIX_LABEL+"wait";
        public final static String PROCESSING=Messages.PREFIX_LABEL+"processing";
        public final static String CONFIRM=Messages.PREFIX_LABEL+"confirm";
        public final static String LOADING_PAGE=Messages.PREFIX_LABEL+"loading";
        public final static String CASS=Messages.PREFIX_LABEL+"cass";
        public final static String DATE_FORMAT="date.format";
        public final static String TIME_FORMAT="time.format";  
        public final static String TIME_OPENING="time.opening";  
        public final static String TIME_CLOSING="time.closing";  
        public final static String APP_INFO=Messages.PREFIX_LABEL+"appointmentInfo";
        public final static String APP_TYPE=Messages.PREFIX_LABEL+"type";
        public final static String APP_CONTACT=Messages.PREFIX_LABEL+"contact"; 
        public final static String APP_LOCATION=Messages.PREFIX_LABEL+"location";
        public final static String APP_URL=Messages.PREFIX_LABEL+"url";
        public final static String APP_CUSTOMER=Messages.PREFIX_LABEL+"customer";
        public final static String APP_TITLE=Messages.PREFIX_LABEL+"title";
        public final static String APP_DATE=Messages.PREFIX_LABEL+"appointmentDate"; 
        public final static String APP_START_TIME=Messages.PREFIX_LABEL+"startTime";
        public final static String APP_END_TIME=Messages.PREFIX_LABEL+"endTime";
        public final static String WEEKLY=Messages.PREFIX_LABEL+"weekly";
        public final static String MONTHLY=Messages.PREFIX_LABEL+"monthly";
    }
    
    public class Error{
        public final static String PREFIX_CONVERT=Messages.PREFIX_ERROR+"prefix.cannotConvert";
        public final static String SUFFIX_UTC_DATE=Messages.PREFIX_ERROR+"suffix.UTCDate";
        public final static String SUFFIX_UTC_TIME=Messages.PREFIX_ERROR+"suffix.UTCTime";
        public final static String EMPTY=Messages.PREFIX_ERROR+"cannotBeEmpty";
        public final static String NO_CONNECTION=Messages.PREFIX_ERROR+"noConnection";
        public final static String SAVE_FAILED=Messages.PREFIX_ERROR+"saveFailed";
        public final static String INVALID_APP_TIME=Messages.PREFIX_ERROR+"invalidAppTime";
    }
}
