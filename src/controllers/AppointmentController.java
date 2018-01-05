/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dataObjects.Appointment;
import dataObjects.Customer;
import dataTransferObjects.AppointmentDto;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.App;
import main.DBConn;
import util.Constants;
import util.DataMapper;
import util.Helper;
import util.Validator;

/**
 *
 * @author Abhi
 */
public class AppointmentController implements Initializable, GlobalController {

    @FXML
    private Label titleLabel, contactLabel, locationLabel, customerLabel, typeLabel, startTimeLabel, endTimeLabel, appointmentDateLabel,header;
    @FXML
    private TextField titleText, contactText, locationText, urlText, startTimeText, endTimeText, appointmentDateText;
    @FXML
    private Button saveBtn,cancelBtn;
    @FXML
    private ComboBox customerDD,typeDD;

    private AppointmentDto aDto;
    private Appointment a;
    ObservableList<Customer> ol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        LinkedHashMap map = (LinkedHashMap) App.getControllerObj();
        a = (Appointment) map.get(Constants.Appointment.APPOINTMENT_DATA);
        if(a!=null){
            aDto = new AppointmentDto();
            DataMapper.mapAppointmentDto(a, aDto);
        }
        ol=(ObservableList<Customer>) map.get(Constants.Appointment.CUSTOMER_MAP);
        
        customerDD.setItems(ol);
        typeDD.getItems().addAll(
                Helper.getLabel(Constants.Appointment.CONTINUING),
                Helper.getLabel(Constants.Appointment.CONTRACT),
                Helper.getLabel(Constants.Appointment.TERM));
        
        titleLabel.setText(Helper.getLabel(Constants.Labels.APP_TITLE));
        contactLabel.setText(Helper.getLabel(Constants.Labels.APP_CONTACT));
        locationLabel.setText(Helper.getLabel(Constants.Labels.APP_LOCATION));
        customerLabel.setText(Helper.getLabel(Constants.Labels.APP_CUSTOMER));
        typeLabel.setText(Helper.getLabel(Constants.Labels.APP_TYPE));
        startTimeLabel.setText(Helper.getLabel(Constants.Labels.APP_START_TIME));
        endTimeLabel.setText(Helper.getLabel(Constants.Labels.APP_END_TIME));
        appointmentDateLabel.setText(Helper.getLabel(Constants.Labels.APP_DATE));        
        saveBtn.setText(Helper.getLabel(Constants.Labels.SAVE));
        cancelBtn.setText(Helper.getLabel(Constants.Labels.CANCEL));
        header.setText(Helper.getLabel(Constants.Labels.APP_INFO));
        
        startTimeText.setPromptText(Helper.getLabel(Constants.Labels.TIME_OPENING));
        endTimeText.setPromptText(Helper.getLabel(Constants.Labels.TIME_CLOSING));
        appointmentDateText.setPromptText(Helper.getLabel(Constants.Labels.DATE_FORMAT));
        
        if (aDto != null) {
            titleText.setText(aDto.getTitle());
            contactText.setText(aDto.getContact());
            locationText.setText(aDto.getLocation());
            customerDD.setValue(getCustomerModel(aDto.getCustomerId()));
            typeDD.setValue(aDto.getType());
            startTimeText.setText(aDto.getStartTime());
            endTimeText.setText(aDto.getEndTime());
            appointmentDateText.setText(aDto.getAppointmentDate());
            urlText.setText(aDto.getUrl());
        }
        Platform.runLater(() -> titleText.requestFocus());
    }
    
    private Customer getCustomerModel(int customerId){
        Optional<Customer> c = ol.stream().filter(obj -> obj.getCustomerId() == customerId).findFirst();
        return c.orElse(null);
    }

    public void doSave() throws Exception {
        Customer c = (Customer) customerDD.getSelectionModel().getSelectedItem();
        String type =(String)typeDD.getSelectionModel().getSelectedItem();
        if(c==null){
            c=new Customer();
        }if(type==null){
            type="";
        }
        if (Validator.hasRequiredValue(titleText, Constants.Labels.APP_TITLE)
                && Validator.hasRequiredValue(contactText, Constants.Labels.APP_CONTACT)
                && Validator.hasRequiredValue(locationText, Constants.Labels.APP_LOCATION)
                && Validator.hasRequiredValue(urlText, Constants.Labels.APP_URL)
                && Validator.hasRequiredValue(c.getCustomerName(), Constants.Labels.APP_CUSTOMER)
                && Validator.hasRequiredValue(type, Constants.Labels.APP_TYPE)
                && Validator.hasRequiredValue(appointmentDateText, Constants.Labels.APP_DATE)
                && Validator.hasRequiredValue(startTimeText, Constants.Labels.APP_START_TIME)
                && Validator.hasRequiredValue(endTimeText, Constants.Labels.APP_END_TIME)) {
            if (aDto == null) {
                aDto = new AppointmentDto();
            }            
            
            aDto.setTitle(titleText.getText());
            aDto.setContact(contactText.getText());
            aDto.setLocation(locationText.getText());
            aDto.setCustomerId(c.getCustomerId());
            aDto.setType(typeDD.getSelectionModel().getSelectedItem().toString());
            aDto.setStartTime(startTimeText.getText());
            aDto.setEndTime(endTimeText.getText());
            aDto.setUrl(urlText.getText());
            aDto.setAppointmentDate(appointmentDateText.getText());
            a = DataMapper.mapAppointmentDO(a, aDto);
            try {
                a = DBConn.saveAppointment(a);
                aDto = DataMapper.mapAppointmentDto(a, aDto);
                Helper.doMessageAlert(Helper.getLabel(Constants.Labels.SAVE_SUCCESS), Constants.Messages.SEVERITY_SUCCESS);
                changeScene(cancelBtn, Constants.MAIN);
            } catch (Exception e) {
                Helper.throwException(Helper.getLabel(Constants.Error.SAVE_FAILED));
            }
        }
    }

    public void doCancel() throws Exception {
        doCancel(true, cancelBtn, Constants.MAIN);
    }

}
