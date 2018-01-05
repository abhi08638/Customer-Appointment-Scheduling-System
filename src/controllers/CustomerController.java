/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dataObjects.Customer;
import dataTransferObjects.CustomerDto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
public class CustomerController implements Initializable, GlobalController {

    @FXML
    private Label nameLabel, addr1Label, addr2Label, cityLabel, countryLabel, zipLabel, phoneLabel, header;
    @FXML
    private TextField nameText, addr1Text, addr2Text, cityText, countryText, zipText, phoneText;
    @FXML
    private Button saveBtn, cancelBtn;
    @FXML
    private CheckBox activeCB;

    private CustomerDto customerDto;
    private Customer customer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customer = (Customer) App.getControllerObj();
        if(customer!=null){
            customerDto = new CustomerDto();
            DataMapper.mapCustomerDto(customer, customerDto);
        }
        nameLabel.setText(Helper.getLabel(Constants.Labels.CUSTOMER_NAME));
        addr1Label.setText(Helper.getLabel(Constants.Labels.CUSTOMER_ADDR1));
        addr2Label.setText(Helper.getLabel(Constants.Labels.CUSTOMER_ADDR2));
        cityLabel.setText(Helper.getLabel(Constants.Labels.CUSTOMER_CITY));
        countryLabel.setText(Helper.getLabel(Constants.Labels.CUSTOMER_COUNTRY));
        zipLabel.setText(Helper.getLabel(Constants.Labels.CUSTOMER_ZIP));
        phoneLabel.setText(Helper.getLabel(Constants.Labels.CUSTOMER_PHONE));
        activeCB.setText(Helper.getLabel(Constants.Labels.CUSTOMER_ACTIVE));
        activeCB.setSelected(true);
        saveBtn.setText(Helper.getLabel(Constants.Labels.SAVE));
        cancelBtn.setText(Helper.getLabel(Constants.Labels.CANCEL));
        header.setText(Helper.getLabel(Constants.Labels.CUSTOMER_INFO));

        if (customerDto != null) {
            nameText.setText(customerDto.getCustomerName());
            addr1Text.setText(customerDto.getAddress());
            addr2Text.setText(customerDto.getAddress2());
            cityText.setText(customerDto.getCity());
            countryText.setText(customerDto.getCountry());
            phoneText.setText(customerDto.getPhone());
            zipText.setText(customerDto.getPostalCode());
            activeCB.setSelected(customerDto.isActive());
        }
        Platform.runLater(() -> nameText.requestFocus());
    }

    public void doSave() throws Exception {
        if (Validator.hasRequiredValue(nameText, Constants.Labels.CUSTOMER_NAME)
                && Validator.hasRequiredValue(addr1Text, Constants.Labels.CUSTOMER_ADDR1)
                && Validator.hasRequiredValue(cityText, Constants.Labels.CUSTOMER_CITY)
                && Validator.hasRequiredValue(countryText, Constants.Labels.CUSTOMER_COUNTRY)
                && Validator.hasRequiredValue(zipText, Constants.Labels.CUSTOMER_ZIP)
                && Validator.hasRequiredValue(phoneText, Constants.Labels.CUSTOMER_PHONE)) {
            if (customerDto == null) {
                customerDto = new CustomerDto();
            }
            customerDto.setCustomerName(nameText.getText());
            customerDto.setAddress(addr1Text.getText());
            customerDto.setAddress2(addr2Text.getText());
            customerDto.setCity(cityText.getText());
            customerDto.setCountry(countryText.getText());
            customerDto.setPostalCode(zipText.getText());
            customerDto.setPhone(phoneText.getText());
            customerDto.setActive(activeCB.isSelected());            
            customer = DataMapper.mapCustomerDO(customer, customerDto);
            try {
                customer = DBConn.saveCustomer(customer);
                customerDto = DataMapper.mapCustomerDto(customer, customerDto);
                Helper.doMessageAlert(Helper.getLabel(Constants.Labels.SAVE_SUCCESS), Constants.Messages.SEVERITY_SUCCESS);
                changeScene(cancelBtn,Constants.MAIN);
            } catch (Exception e) {
                Helper.throwException(Helper.getLabel(Constants.Error.SAVE_FAILED));
            }
        }
    }

    public void doCancel() throws Exception {
        doCancel(true, cancelBtn, Constants.MAIN);
    }

}
