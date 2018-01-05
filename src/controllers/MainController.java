/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dataObjects.Appointment;
import dataObjects.Customer;
import main.App;
import util.Constants;
import util.Helper;
import util.Validator;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.DBConn;
import util.DateUtils;
import util.ReportGenerator;

/**
 *
 * @author Abhi
 */
public class MainController implements Initializable, GlobalController {

    @FXML
    private Button exitBtn, addCustomerBtn, addAppointmentBtn;
    @FXML
    private Label header,customerTableLabel,appTableLabel;
    @FXML
    private TableColumn customerName, customerActive;
    @FXML
    private TableColumn<Appointment, String> appCustomer, appDate, appStart, appEnd, appTitle;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TextField customerSearchbox, appSearchBox;

    private ObservableList<Customer> cList = FXCollections.observableArrayList();
    private ObservableList<Appointment> aList = FXCollections.observableArrayList();

    @FXML
    protected RadioButton monthly, weekly;
    ToggleGroup group = new ToggleGroup();

    @FXML
    private void handleRadioButtonAction(ActionEvent event) {        
        refreshAppointments();
    }
    
    public void doConsultationReport()throws Exception{
        Helper.doWaitAlert(true, null);
        String path = ReportGenerator.generateUserSchedules();
        Helper.doWaitAlert(false, null);
        Helper.doMessageAlert("Report was saved to "+path, Constants.Messages.SEVERITY_SUCCESS);        
    }
    
    public void doTypeReport()throws Exception{
        Helper.doWaitAlert(true, null);
        String path = ReportGenerator.generateTypeReport();
        Helper.doWaitAlert(false, null);
        Helper.doMessageAlert("Report was saved to "+path, Constants.Messages.SEVERITY_SUCCESS);
    }
    
    public void doCustomerAppointmentReport()throws Exception{
        Helper.doWaitAlert(true, null);
        String path = ReportGenerator.generateCustomerAppointmentReport();
        Helper.doWaitAlert(false, null);
        Helper.doMessageAlert("Report was saved to "+path, Constants.Messages.SEVERITY_SUCCESS);        
    }
    
    private void refreshAppointments(){
        Helper.doWaitAlert(true,null);
        try {        
            if (monthly.selectedProperty().getValue().equals(true)) {
                aList = DBConn.getAllActiveAppointments(DateUtils.getEndOfMonth());
            } else {
                aList = DBConn.getAllActiveAppointments(DateUtils.getEndOfWeek());
            }
            loadAppointmentTable();
        } catch (Exception e) {
            Helper.printException(e.getMessage());
        }
        Helper.doWaitAlert(false,null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        Helper.doWaitAlert(true, Constants.Labels.LOADING_PAGE);
        header.setText(Helper.getLabel(Constants.Labels.CASS));
        customerTableLabel.setText(Helper.getLabel(Constants.Labels.CUSTOMER_INFO));
        appTableLabel.setText(Helper.getLabel(Constants.Labels.APP_INFO));
        weekly.setText(Helper.getLabel(Constants.Labels.WEEKLY));
        monthly.setText(Helper.getLabel(Constants.Labels.MONTHLY));
        weekly.setToggleGroup(group);
        monthly.setToggleGroup(group);
        monthly.setSelected(true);

        try {
            cList = DBConn.getAllCustomers();
            loadCustomerTable();
            refreshAppointments();            
        } catch (Exception e) {
            Helper.printException(e.getMessage());
        }
        Helper.doWaitAlert(false, null);
        doLoginReminders();
    }

    private void doLoginReminders() {
        ObservableList<Appointment> ol
                = aList.stream()
                        .filter(obj -> obj.getStart().isBefore(Instant.now().plus(15, ChronoUnit.MINUTES)))
                        .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
        String reminder = "";
        for (Appointment a : ol) {
            reminder += "You have a meeting with Client " + getCustomerName(a.getCustomerId()) + " from " + DateUtils.getFormattedTime(a.getStart())+" to "
                     + DateUtils.getFormattedTime(a.getEnd()) + "\n";
        }
        if (!ol.isEmpty()) {
            try {
                Helper.doMessageAlert(reminder, Constants.Messages.SEVERITY_INFO);
            } catch (Exception e) {
                Helper.printException(e.getMessage());
            }
        }
    }

    private void loadCustomerTable() {
        customerTable.setItems(cList);
        customerName.setCellValueFactory(
                new PropertyValueFactory<>("customerName"));
        customerActive.setCellValueFactory(
                new PropertyValueFactory<>("active"));
    }

    private void loadAppointmentTable() {        
        appointmentTable.setItems(aList);
        //we use end for date because if going past 12 AM, using the start decrements the date
        appDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateUtils.getFormattedDate(cellData.getValue().getEnd())));
        appStart.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateUtils.getFormattedTime(cellData.getValue().getStart())));
        appEnd.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateUtils.getFormattedTime(cellData.getValue().getEnd())));
        appCustomer.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getCustomerName(cellData.getValue().getCustomerId())));
    }

    private String getCustomerName(int customerId) {
        Optional<Customer> c = cList.stream().filter(obj -> obj.getCustomerId() == customerId).findFirst();
        return c.get().getCustomerName();
    }

    public void doExit() throws Exception {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    public void doAppointmentDelete() throws Exception {
        Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
        boolean deleteConfirmed = false;
        if (a != null) {
            deleteConfirmed = Helper.doDeleteAlert();
        } else {
            deleteConfirmed = false;
        }
        if (deleteConfirmed) {
            DBConn.deleteAppointment(a.getAppointmentId());
            refreshAppointments();
        }
    }

    public void doCustomerSearch() throws Exception {
        if (Validator.hasValue(customerSearchbox)) {
            ObservableList<Customer> ol
                    = cList.stream()
                            .filter(obj
                                    -> obj.getCustomerName().toUpperCase().contains(customerSearchbox.getText().toUpperCase()))
                            .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));

            customerTable.setItems(ol);
        } else {
            loadCustomerTable();
        }
    }

    public void doAppointmentSearch() throws Exception {
        if (Validator.hasValue(appSearchBox)) {
            ObservableList<Appointment> ol
                    = aList.stream()
                            .filter(obj
                                    -> getCustomerName(obj.getCustomerId()).toUpperCase().contains(appSearchBox.getText().toUpperCase()))
                            .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));

            appointmentTable.setItems(ol);
        } else {
            loadAppointmentTable();
        }
    }

    public void doAddCustomer() throws Exception {
        changeScene(addCustomerBtn, Constants.CUSTOMER);
    }

    public void doModifyCustomer() throws Exception {
        Customer c = customerTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            App.setControllerObj(c);
            changeScene(addCustomerBtn, Constants.CUSTOMER);
        }
    }

    public void doModifyAppointment() throws Exception {
        Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
        if (a != null) {
            LinkedHashMap map = new LinkedHashMap();
            ObservableList<Customer> ol = cList.stream()
                    .filter(obj -> obj.isActive())
                    .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
            map.put(Constants.Appointment.CUSTOMER_MAP, ol);
            map.put(Constants.Appointment.APPOINTMENT_DATA, a);
            App.setControllerObj(map);
            changeScene(addAppointmentBtn, Constants.APPOINTMENT);
        }
    }

    public void doAddAppointment() throws Exception {
        LinkedHashMap map = new LinkedHashMap();
        ObservableList<Customer> ol = cList.stream()
                .filter(obj -> obj.isActive())
                .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
        map.put(Constants.Appointment.CUSTOMER_MAP, ol);
        App.setControllerObj(map);
        changeScene(addAppointmentBtn, Constants.APPOINTMENT);
    }
}
