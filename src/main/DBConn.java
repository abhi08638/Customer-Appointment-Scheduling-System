/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dataObjects.Address;
import dataObjects.Appointment;
import dataObjects.City;
import dataObjects.Country;
import dataObjects.Customer;
import static java.lang.Math.toIntExact;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Helper;
import util.Constants;
import util.DateUtils;

/**
 *
 * @author Abhi
 */
public class DBConn {

    protected static Connection getConnection() throws Exception {
        //add connection pooling 
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        String db = "U04puO";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U04puO";
        String pass = "53688311014";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
            //System.out.println("Connected to database : " + db);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            Helper.doWaitAlert(false, null);
            Helper.printException(Helper.getLabel(Constants.Error.NO_CONNECTION));
            Helper.throwException(Helper.getLabel(Constants.Error.NO_CONNECTION));
            //System.exit(0);
        }
        return conn;
    }

    protected static ResultSet doSelect(PreparedStatement stmt) throws Exception {
        try (Connection c = getConnection()) {
            if (c != null) {
                try {
                    ResultSet rs = stmt.executeQuery();
                    return rs;
                } catch (Exception e) {
                    throw e;
                }
            } else {
                Helper.throwException(Helper.getLabel(Constants.Error.NO_CONNECTION));
            }
        }
        return null;
    }

    private static void doUpdate(PreparedStatement stmt) throws Exception {
        try (Connection c = getConnection()) {
            if (c != null) {
                try {
                    stmt.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }
            } else {
                Helper.throwException(Helper.getLabel(Constants.Error.NO_CONNECTION));
            }
        }
    }

    public static ObservableList<Customer> getAllCustomers() throws Exception {
        ObservableList<Customer> ol = FXCollections.observableArrayList();
        String sql = "select * from customer";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet rs = doSelect(stmt);
        while (rs.next()) {
            Customer c = new Customer();
            c.setActive(rs.getBoolean("ACTIVE"));
            c.setCustomerName(rs.getString("CUSTOMERNAME"));
            c.setCreatedBy(rs.getString("CREATEDBY"));
            c.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            c.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            c.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
            c.setCustomerId(rs.getInt("CUSTOMERID"));
            c.setAddress(getAddress(rs.getInt("ADDRESSID")));
            ol.add(c);
        }
        return ol;
    }

    public static ObservableList<Appointment> getAllActiveAppointments(LocalDateTime endDate) throws Exception {
        ObservableList<Appointment> ol = FXCollections.observableArrayList();
        String sql = "select * from appointment a where a.end > ? and UPPER(a.createdBy) = ? "
                + "and a.start < ? order by a.start";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setObject(1, DateUtils.toLocalDateTime(Instant.now()));
        stmt.setString(2, ApplicationConfig.getUserName().toUpperCase());
        stmt.setObject(3, endDate);
        ResultSet rs = doSelect(stmt);
        while (rs.next()) {
            Appointment a = new Appointment();
            a.setAppointmentId(rs.getInt("APPOINTMENTID"));
            a.setContact(rs.getString("CONTACT"));
            a.setDescription(rs.getString("DESCRIPTION"));
            a.setTitle(rs.getString("TITLE"));
            a.setLocation(rs.getString("LOCATION"));
            a.setUrl(rs.getString("URL"));
            a.setStart(DateUtils.toInstant(rs.getObject("START", LocalDateTime.class)));
            a.setEnd(DateUtils.toInstant(rs.getObject("END", LocalDateTime.class)));
            a.setCreatedBy(rs.getString("CREATEDBY"));
            a.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            a.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            a.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
            a.setCustomerId(rs.getInt("CUSTOMERID"));
            ol.add(a);
        }
        return ol;
    }

    public static boolean getUser(String userName, String password) throws Exception {
        //List<Schema> result = new ArrayList<>();
        Helper.doWaitAlert(true, null);
        String sql = "select * from user where UPPER(username) = ? and password = ?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, userName.toUpperCase());
        stmt.setString(2, password);
        ResultSet rs = doSelect(stmt);
        Helper.doWaitAlert(false, null);
        while (rs.next()) {
            return true;
        }
        return false;
    }

    public static Appointment saveAppointment(Appointment a) throws Exception {
        Helper.doWaitAlert(true, null);

        String sql = "select * from appointment a where ((? between a.start and a.end or ? between a.start and a.end) "
                    + "OR (a.start between ? and ? or a.end between ? and ?))";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Appointment dbAppointment = getAppointment(a.getAppointmentId());
        if (dbAppointment == null) {
            stmt = getConnection().prepareStatement(sql);
            stmt.setObject(1, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(2, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setObject(3, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(4, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setObject(5, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(6, DateUtils.toLocalDateTime(a.getEnd()));
            rs = doSelect(stmt);
            if (rs.next()) {
                Helper.doWaitAlert(false, null);
                Helper.throwException("Conflicting Appoinment");
            }
            sql = "insert into appointment(customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdateBy) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?)";
            stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, a.getCustomerId());
            stmt.setString(2, a.getTitle());
            stmt.setString(3, a.getDescription());
            stmt.setString(4, a.getLocation());
            stmt.setString(5, a.getContact());
            stmt.setString(6, a.getUrl());
            stmt.setObject(7, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(8, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setObject(9, DateUtils.toLocalDateTime(a.getCreatedDate()));
            stmt.setString(10, a.getCreatedBy());
            stmt.setString(11, a.getLastUpdatedBy());
        } else {
            sql = "select * from appointment a where ((? between a.start and a.end or ? between a.start and a.end) "
                    + "OR (a.start between ? and ? or a.end between ? and ?)) and a.appointmentId<>?";
            stmt = getConnection().prepareStatement(sql);
            stmt.setObject(1, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(2, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setObject(3, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(4, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setObject(5, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(6, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setInt(7, a.getAppointmentId());
            rs = doSelect(stmt);
            if (rs.next()) {
                Helper.doWaitAlert(false, null);
                Helper.throwException("Conflicting Appoinment");
            }
            sql = "update appointment set customerId = ?, title = ?, description = ?, location = ?, contact = ?, url = ?, start = ?, end = ?, lastUpdateBy = ? "
                    + "where appointmentId = ?";

            stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, a.getCustomerId());
            stmt.setString(2, a.getTitle());
            stmt.setString(3, a.getDescription());
            stmt.setString(4, a.getLocation());
            stmt.setString(5, a.getContact());
            stmt.setString(6, a.getUrl());
            stmt.setObject(7, DateUtils.toLocalDateTime(a.getStart()));
            stmt.setObject(8, DateUtils.toLocalDateTime(a.getEnd()));
            stmt.setString(9, a.getLastUpdatedBy());
            stmt.setInt(10, a.getAppointmentId());
        }
        doUpdate(stmt);
        if (a.getAppointmentId() == 0) {
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long key = rs.getLong(1);
                a = getAppointment(toIntExact(key));
            }
        } else {
            a = getAppointment(a.getAppointmentId());
        }
        Helper.doWaitAlert(false, null);
        return a;
    }

    public static Appointment getAppointment(int appointmentId) throws Exception {
        Appointment a = new Appointment();
        String sql = "select * from appointment a where a.appointmentId=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt(1, appointmentId);
        ResultSet rs = doSelect(stmt);

        if (rs.next()) {
            a.setAppointmentId(rs.getInt("APPOINTMENTID"));
            a.setContact(rs.getString("CONTACT"));
            a.setDescription(rs.getString("DESCRIPTION"));
            a.setTitle(rs.getString("TITLE"));
            a.setLocation(rs.getString("LOCATION"));
            a.setUrl(rs.getString("URL"));
            a.setStart(DateUtils.toInstant(rs.getObject("START", LocalDateTime.class)));
            a.setEnd(DateUtils.toInstant(rs.getObject("END", LocalDateTime.class)));
            a.setCreatedBy(rs.getString("CREATEDBY"));
            a.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            a.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            a.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
            a.setCustomerId(rs.getInt("CUSTOMERID"));
        } else {
            return null;
        }

        return a;
    }

    public static void deleteAppointment(int appointmentId) throws Exception {
        Appointment a = new Appointment();
        String sql = "delete from appointment where appointmentId=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt(1, appointmentId);
        doUpdate(stmt);
    }

    public static Customer saveCustomer(Customer c) throws Exception {
        Helper.doWaitAlert(true, null);
        Customer dbCustomer = getCustomer(c.getCustomerId());
        c.setAddress(saveAddress(c.getAddress()));
        PreparedStatement stmt = null;
        if (dbCustomer == null) {
            String sql = "insert into customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) "
                    + "values (?,?,?,?,?,?)";
            stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, c.getCustomerName());
            stmt.setInt(2, c.getAddress().getAddressId());
            stmt.setBoolean(3, c.isActive());
            stmt.setObject(4, DateUtils.toLocalDateTime(c.getCreatedDate()));
            stmt.setString(5, c.getCreatedBy());
            stmt.setString(6, c.getLastUpdatedBy());
        } else {
            String sql = "update customer set customerName = ?, active = ?, lastUpdateBy = ? "
                    + "where customerId = ?";

            stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, c.getCustomerName());
            stmt.setBoolean(2, c.isActive());
            stmt.setString(3, c.getLastUpdatedBy());
            stmt.setInt(4, c.getCustomerId());
        }
        doUpdate(stmt);
        if (c.getCustomerId() == 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long key = rs.getLong(1);
                c = getCustomer(toIntExact(key));
            }
        } else {
            c = getCustomer(c.getCustomerId());
        }
        Helper.doWaitAlert(false, null);
        return c;
    }

    public static Customer getCustomer(int customerId) throws Exception {
        Customer c = new Customer();
        String sql = "select * from customer c where c.customerId=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt(1, customerId);
        ResultSet rs = doSelect(stmt);

        if (rs.next()) {
            System.out.println(rs.getObject("CREATEDATE", LocalDateTime.class));
            System.out.println(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            System.out.println(DateUtils.convertToLocalTime(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class))));
            System.out.println(DateUtils.getFormattedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class))));
            System.out.println(DateUtils.getFormattedTime(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class))));

            c.setActive(rs.getBoolean("ACTIVE"));
            c.setCustomerName(rs.getString("CUSTOMERNAME"));
            c.setCreatedBy(rs.getString("CREATEDBY"));
            c.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            c.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            c.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
            c.setCustomerId(rs.getInt("CUSTOMERID"));
            c.setAddress(getAddress(rs.getInt("ADDRESSID")));
        } else {
            return null;
        }

        return c;
    }

    public static Address saveAddress(Address a) throws Exception {
        Address dbAddress = getAddress(a.getAddressId());
        a.setCity(saveCity(a.getCity()));
        PreparedStatement stmt = null;
        if (dbAddress == null) {
            String sql = "insert into address(address, address2, phone, postalCode, createDate, createdBy, lastUpdateBy, cityId) "
                    + "values (?,?,?,?,?,?,?,?)";

            stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, a.getAddress());
            stmt.setString(2, a.getAddress2());
            stmt.setString(3, a.getPhone());
            stmt.setString(4, a.getPostalCode());
            stmt.setObject(5, DateUtils.toLocalDateTime(a.getCreatedDate()));
            stmt.setString(6, a.getCreatedBy());
            stmt.setString(7, a.getLastUpdatedBy());
            stmt.setInt(8, a.getCity().getCityId());
        } else {
            String sql = "update address set address = ?, address2 = ?, phone = ?, postalCode = ?, lastUpdateBy = ? "
                    + "where addressId = ?";

            stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, a.getAddress());
            stmt.setString(2, a.getAddress2());
            stmt.setString(3, a.getPhone());
            stmt.setString(4, a.getPostalCode());
            stmt.setString(5, a.getLastUpdatedBy());
            stmt.setInt(6, a.getAddressId());
        }
        doUpdate(stmt);
        if (a.getAddressId() == 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long key = rs.getLong(1);
                a = getAddress(toIntExact(key));
            }
        } else {
            a = getAddress(a.getAddressId());
        }

        return a;
    }

    public static Address getAddress(int addressId) throws Exception {
        Address a = new Address();
        String sql = "select * from address a where a.addressId=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt(1, addressId);
        ResultSet rs = doSelect(stmt);

        if (rs.next()) {
            a.setAddress(rs.getString("ADDRESS"));
            a.setAddress2(rs.getString("ADDRESS2"));
            a.setPhone(rs.getString("PHONE"));
            a.setPostalCode(rs.getString("POSTALCODE"));
            a.setAddressId(rs.getInt("ADDRESSID"));
            a.setCreatedBy(rs.getString("CREATEDBY"));
            a.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            a.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            a.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
            a.setCity(getCity(rs.getInt("CITYID")));
        } else {
            return null;
        }

        return a;
    }

    public static City saveCity(City c) throws Exception {
        City dbCity = getCity(c.getCityId());
        c.setCountry(saveCountry(c.getCountry()));
        PreparedStatement stmt = null;
        if (dbCity == null) {
            String sql = "insert into city(city, createDate, createdBy, lastUpdateBy, countryId) "
                    + "values (?,?,?,?,?)";

            stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, c.getCity());
            stmt.setObject(2, DateUtils.toLocalDateTime(c.getCreatedDate()));
            stmt.setString(3, c.getCreatedBy());
            stmt.setString(4, c.getLastUpdatedBy());
            stmt.setInt(5, c.getCountry().getCountryId());
        } else {
            String sql = "update city set city = ?, lastUpdateBy = ? "
                    + "where cityId = ?";

            stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, c.getCity());
            stmt.setString(2, c.getLastUpdatedBy());
            stmt.setInt(3, c.getCityId());
        }
        doUpdate(stmt);
        if (c.getCityId() == 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long key = rs.getLong(1);
                c = getCity(toIntExact(key));
            }
        } else {
            c = getCity(c.getCityId());
        }
        return c;
    }

    public static City getCity(int cityId) throws Exception {
        City c = new City();
        String sql = "select * from city c where c.cityId=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt(1, cityId);
        ResultSet rs = doSelect(stmt);

        if (rs.next()) {
            c.setCity(rs.getString("CITY"));
            c.setCityId(rs.getInt("CITYID"));
            c.setCreatedBy(rs.getString("CREATEDBY"));
            c.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            c.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            c.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
            c.setCountry(getCountry(rs.getInt("COUNTRYID")));
        } else {
            return null;
        }

        return c;
    }

    public static Country saveCountry(Country c) throws Exception {
        Country dbCountry = getCountry(c.getCountryId());
        PreparedStatement stmt = null;
        if (dbCountry == null) {
            String sql = "insert into country(country, createDate, createdBy, lastUpdateBy) "
                    + "values (?,?,?,?)";

            stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, c.getCountry());
            stmt.setObject(2, DateUtils.toLocalDateTime(c.getCreatedDate()));
            stmt.setString(3, c.getCreatedBy());
            stmt.setString(4, c.getLastUpdatedBy());
        } else {
            String sql = "update country set country = ?, lastUpdateBy = ? "
                    + "where countryId = ?";

            stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, c.getCountry());
            stmt.setString(2, c.getLastUpdatedBy());
            stmt.setInt(3, c.getCountryId());
        }
        doUpdate(stmt);
        if (c.getCountryId() == 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long key = rs.getLong(1);
                c = getCountry(toIntExact(key));
            }
        } else {
            c = getCountry(c.getCountryId());
        }

        return c;
    }

    public static Country getCountry(int countryId) throws Exception {
        Country c = new Country();
        String sql = "select * from country c where c.countryId=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt(1, countryId);
        ResultSet rs = doSelect(stmt);

        if (rs.next()) {
            c.setCountry(rs.getString("COUNTRY"));
            c.setCountryId(rs.getInt("COUNTRYID"));
            c.setCreatedBy(rs.getString("CREATEDBY"));
            c.setCreatedDate(DateUtils.toInstant(rs.getObject("CREATEDATE", LocalDateTime.class)));
            c.setLastUpdate(rs.getTimestamp("LASTUPDATE").toInstant());
            c.setLastUpdatedBy(rs.getString("LASTUPDATEBY"));
        } else {
            return null;
        }

        return c;
    }   
}
