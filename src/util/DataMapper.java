/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dataObjects.Address;
import dataObjects.Appointment;
import dataObjects.City;
import dataObjects.Country;
import dataObjects.Customer;
import dataTransferObjects.AppointmentDto;
import dataTransferObjects.CustomerDto;
import java.time.Instant;
import main.ApplicationConfig;

/**
 *
 * @author Abhi
 */
public class DataMapper {

    public static Customer mapCustomerDO(Customer c, CustomerDto cDto) {
        if (c == null) {
            c = new Customer();
        }
        c.setCustomerId(cDto.getCustomerId());
        c.setActive(cDto.isActive());
        c.setCustomerName(cDto.getCustomerName());
        c.setLastUpdatedBy(ApplicationConfig.getUserName());
        c.setLastUpdate(Instant.now());
        if (c.getAddress() == null) {
            c.setAddress(mapAddress(cDto));
        } else {
            int addressId, cityId, countryId;
            addressId = c.getAddress().getAddressId();
            cityId = c.getAddress().getCity().getCityId();
            countryId = c.getAddress().getCity().getCountry().getCountryId();
            c.setAddress(mapAddress(cDto));
            c.getAddress().getCity().getCountry().setCountryId(countryId);
            c.getAddress().getCity().setCityId(cityId);
            c.getAddress().setAddressId(addressId);
        }
        if (c.getCustomerId() <= 0) {
            c.setCreatedBy(ApplicationConfig.getUserName());
            c.setCreatedDate(Instant.now());
        }
        return c;
    }

    public static Country mapCountry(CustomerDto cDto) {
        Country country = new Country();
        country.setCountry(cDto.getCountry());
        country.setCreatedBy(ApplicationConfig.getUserName());
        country.setLastUpdatedBy(ApplicationConfig.getUserName());
        country.setCreatedDate(Instant.now());
        country.setLastUpdate(Instant.now());
        return country;
    }

    public static City mapCity(CustomerDto cDto) {
        City city = new City();
        city.setCity(cDto.getCity());
        city.setCountry(mapCountry(cDto));
        city.setCreatedBy(ApplicationConfig.getUserName());
        city.setLastUpdatedBy(ApplicationConfig.getUserName());
        city.setCreatedDate(Instant.now());
        city.setLastUpdate(Instant.now());
        return city;
    }

    public static Address mapAddress(CustomerDto cDto) {
        Address address = new Address();
        address.setAddress(cDto.getAddress());
        address.setAddress2(cDto.getAddress2());
        address.setPhone(cDto.getPhone());
        address.setPostalCode(cDto.getPostalCode());
        address.setCity(mapCity(cDto));
        address.setCreatedBy(ApplicationConfig.getUserName());
        address.setLastUpdatedBy(ApplicationConfig.getUserName());
        address.setCreatedDate(Instant.now());
        address.setLastUpdate(Instant.now());
        return address;
    }

    public static CustomerDto mapCustomerDto(Customer c, CustomerDto cDto) {
        cDto.setCustomerId(c.getCustomerId());
        cDto.setCustomerName(c.getCustomerName());
        cDto.setActive(c.isActive());

        Address a = c.getAddress();
        cDto.setAddress(a.getAddress());
        cDto.setAddress2(a.getAddress2());
        cDto.setPhone(a.getPhone());
        cDto.setPostalCode(a.getPostalCode());

        City city = a.getCity();
        cDto.setCity(city.getCity());

        Country country = city.getCountry();
        cDto.setCountry(country.getCountry());

        return cDto;
    }

    public static Appointment mapAppointmentDO(Appointment a, AppointmentDto aDto) throws Exception {
        if (a == null) {
            a = new Appointment();
        }
        a.setContact(aDto.getContact());
        a.setCustomerId(aDto.getCustomerId());
        a.setLocation(aDto.getLocation());
        a.setDescription(aDto.getType());
        a.setTitle(aDto.getTitle());
        a.setUrl(aDto.getUrl());
        a.setLastUpdatedBy(ApplicationConfig.getUserName());
        a.setLastUpdate(Instant.now());
        a.setStart(DateUtils.toDataObjectInstant(aDto.getAppointmentDate(), aDto.getStartTime()));
        a.setEnd(DateUtils.toDataObjectInstant(aDto.getAppointmentDate(), aDto.getEndTime()));
        Instant openingTime = DateUtils.toDataObjectInstant(aDto.getAppointmentDate(), Helper.getLabel(Constants.Labels.TIME_OPENING));
        Instant closingTime = DateUtils.toDataObjectInstant(aDto.getAppointmentDate(), Helper.getLabel(Constants.Labels.TIME_CLOSING));

        if (a.getAppointmentId() <= 0) {
            a.setCreatedBy(ApplicationConfig.getUserName());
            a.setCreatedDate(Instant.now());
        }
        //check if end time before start time 
        if(a.getEnd().isBefore(a.getStart())){
            Helper.throwException(Helper.getLabel(Constants.Error.INVALID_APP_TIME));
            //Helper.throwException(a.getEnd()+"\n"+a.getStart());
        }        
        if(a.getStart().isBefore(openingTime) || a.getStart().isAfter(closingTime)
                || a.getEnd().isBefore(openingTime) || a.getEnd().isAfter(closingTime)){
            Helper.throwException("Outside buisness hours");
            
        }

        return a;
    }

    public static AppointmentDto mapAppointmentDto(Appointment a, AppointmentDto aDto) {
        aDto.setContact(a.getContact());
        aDto.setCustomerId(a.getCustomerId());
        aDto.setLocation(a.getLocation());
        aDto.setTitle(a.getTitle());
        aDto.setUrl(a.getUrl());
        aDto.setType(a.getDescription());
        //we use end for date because if going past 12 AM, using the start decrements the date
        aDto.setAppointmentDate(DateUtils.getFormattedDate(a.getEnd()));
        aDto.setStartTime(DateUtils.getFormattedTime(a.getStart()));
        aDto.setEndTime(DateUtils.getFormattedTime(a.getEnd()));
        return aDto;
    }
}
