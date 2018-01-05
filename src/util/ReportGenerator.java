/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dataObjects.ReportSkeleton;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.DBConn;

/**
 *
 * @author Abhi
 */
public class ReportGenerator extends DBConn {

    public static String generateUserSchedules() throws Exception {
        try {
            String path = System.getProperty("user.dir") + "\\UserScheduleReport.txt";
            int col1Length = 0, col2Length = 0, col3Length = 0, col4Length = 0, col5Length = 0, col6Length = 0;
            ObservableList<ReportSkeleton> ol = getConsultationReport();
            for (ReportSkeleton rs : ol) {
                ArrayList<String> row = rs.getCols();
                for (int i = 1; i <= row.size(); i++) {
                    if (i == 1) {
                        if (col1Length < row.get(i - 1).length()) {
                            col1Length = row.get(i - 1).length();
                        }
                    } else if (i == 2) {
                        if (col2Length < row.get(i - 1).length()) {
                            col2Length = row.get(i - 1).length();
                        }
                    } else if (i == 3) {
                        if (col3Length < row.get(i - 1).length()) {
                            col3Length = row.get(i - 1).length();
                        }
                    } else if (i == 4) {
                        if (col4Length < row.get(i - 1).length()) {
                            col4Length = row.get(i - 1).length();
                        }
                    } else if (i == 5) {
                        if (col5Length < row.get(i - 1).length()) {
                            col5Length = row.get(i - 1).length();
                        }
                    } else if (i == 6) {
                        if (col6Length < row.get(i - 1).length()) {
                            col6Length = row.get(i - 1).length();
                        }
                    }
                }
            }
            col1Length = getColLength(col1Length, "Consultant");
            col2Length = getColLength(col2Length, "Client");
            col3Length = getColLength(col3Length, "Title");
            col4Length = getColLength(col4Length, "Date");
            col5Length = getColLength(col5Length, "Start Time");
            col6Length = getColLength(col6Length, "End Time");

            try (FileWriter fw = new FileWriter(path, false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                out.print(getCellVal("Consultant", col1Length));
                out.print(getCellVal("Client", col2Length));
                out.print(getCellVal("Title", col3Length));
                out.print(getCellVal("Date", col4Length));
                out.print(getCellVal("Start Time", col5Length));
                out.println(getCellVal("End Time", col6Length));
                for (int i = 0; i < (col1Length + col2Length + col3Length + col4Length + col5Length + col6Length) * 1.1; i++) {
                    out.print("-");
                }
                out.println();
                for (ReportSkeleton rs : ol) {
                    out.print(getCellVal(rs.getCols().get(0), col1Length));
                    out.print(getCellVal(rs.getCols().get(1), col2Length));
                    out.print(getCellVal(rs.getCols().get(2), col3Length));
                    out.print(getCellVal(rs.getCols().get(3), col4Length));
                    out.print(getCellVal(rs.getCols().get(4), col5Length));
                    out.println(getCellVal(rs.getCols().get(5), col6Length));
                }
            }
            return path;
        } catch (Exception e) {
            Helper.doMessageAlert("Error generating report", Constants.Messages.SEVERITY_ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<ReportSkeleton> getConsultationReport() throws Exception {
        ObservableList<ReportSkeleton> ol = FXCollections.observableArrayList();
        String sql = "select a.createdBy,c.customerName,a.title, a.start, a.end\n"
                + "from appointment a \n"
                + "join customer c on c.customerId = a.customerId\n"
                + "group by a.createdBy,a.createDate\n"
                + "order by a.createdBy,a.start";
        PreparedStatement stmt = DBConn.getConnection().prepareStatement(sql);
        ResultSet rs = DBConn.doSelect(stmt);
        while (rs.next()) {
            ReportSkeleton cols = new ReportSkeleton(
                    rs.getString("CREATEDBY"),
                    rs.getString("CUSTOMERNAME"),
                    rs.getString("TITLE"),
                    DateUtils.getFormattedDate(DateUtils.toInstant(rs.getObject("START", LocalDateTime.class))),
                    DateUtils.getFormattedTime(DateUtils.toInstant(rs.getObject("START", LocalDateTime.class))),
                    DateUtils.getFormattedTime(DateUtils.toInstant(rs.getObject("END", LocalDateTime.class)))
            );
            ol.add(cols);
        }
        return ol;
    }

    public static String generateTypeReport() throws Exception {
        try {
            String path = System.getProperty("user.dir") + "\\AppointmentTypeReport.txt";
            int col1Length = 0, col2Length = 0, col3Length = 0;
            ObservableList<ReportSkeleton> ol = getTypeReport();
            for (ReportSkeleton rs : ol) {
                ArrayList<String> row = rs.getCols();
                for (int i = 1; i <= row.size(); i++) {
                    if (i == 1) {
                        if (col1Length < row.get(i - 1).length()) {
                            col1Length = row.get(i - 1).length();
                        }
                    } else if (i == 2) {
                        if (col2Length < row.get(i - 1).length()) {
                            col2Length = row.get(i - 1).length();
                        }
                    } else if (i == 3) {
                        if (col3Length < row.get(i - 1).length()) {
                            col3Length = row.get(i - 1).length();
                        }
                    }
                }
            }
            col1Length = getColLength(col1Length, "Month");
            col2Length = getColLength(col2Length, "Type");
            col3Length = getColLength(col3Length, "Count");

            try (FileWriter fw = new FileWriter(path, false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                out.print(getCellVal("Month", col1Length));
                out.print(getCellVal("Type", col2Length));
                out.println(getCellVal("Count", col3Length));
                for (int i = 0; i < (col1Length + col2Length + col3Length) * 1.1; i++) {
                    out.print("-");
                }
                out.println();
                for (ReportSkeleton rs : ol) {
                    out.print(getCellVal(rs.getCols().get(0), col1Length));
                    out.print(getCellVal(rs.getCols().get(1), col2Length));
                    out.println(getCellVal(rs.getCols().get(2), col3Length));
                }
            }
            return path;
        } catch (Exception e) {
            Helper.doMessageAlert("Error generating report", Constants.Messages.SEVERITY_ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<ReportSkeleton> getTypeReport() throws Exception {
        ObservableList<ReportSkeleton> ol = FXCollections.observableArrayList();
        String sql = "SELECT  MONTHNAME(start) AS 'MONTH',\n"
                + "    description AS 'TYPE',\n"
                + "    count(description) AS 'COUNT'\n"
                + "FROM appointment\n"
                + "GROUP BY description,MonthName(start)\n"
                + "ORDER BY 1";
        PreparedStatement stmt = DBConn.getConnection().prepareStatement(sql);
        ResultSet rs = DBConn.doSelect(stmt);
        while (rs.next()) {
            ReportSkeleton cols = new ReportSkeleton(
                    rs.getString("MONTH"),
                    rs.getString("TYPE"),
                    Integer.toString(rs.getInt("COUNT"))
            );
            ol.add(cols);
        }
        return ol;
    }

    public static String generateCustomerAppointmentReport() throws Exception {
        try {
            String path = System.getProperty("user.dir") + "\\CustomerAppointmentReport.txt";
            int col1Length = 0, col2Length = 0, col3Length = 0;
            ObservableList<ReportSkeleton> ol = getCustomerAppointmentReport();
            for (ReportSkeleton rs : ol) {
                ArrayList<String> row = rs.getCols();
                for (int i = 1; i <= row.size(); i++) {
                    if (i == 1) {
                        if (col1Length < row.get(i - 1).length()) {
                            col1Length = row.get(i - 1).length();
                        }
                    } else if (i == 2) {
                        if (col2Length < row.get(i - 1).length()) {
                            col2Length = row.get(i - 1).length();
                        }
                    } else if (i == 3) {
                        if (col3Length < row.get(i - 1).length()) {
                            col3Length = row.get(i - 1).length();
                        }
                    }
                }
            }
            col1Length = getColLength(col1Length, "Month");
            col2Length = getColLength(col2Length, "Customer");
            col3Length = getColLength(col3Length, "Count");

            try (FileWriter fw = new FileWriter(path, false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                out.print(getCellVal("Month", col1Length));
                out.print(getCellVal("Customer", col2Length));
                out.println(getCellVal("Count", col3Length));
                for (int i = 0; i < (col1Length + col2Length + col3Length) * 1.1; i++) {
                    out.print("-");
                }
                out.println();
                for (ReportSkeleton rs : ol) {
                    out.print(getCellVal(rs.getCols().get(0), col1Length));
                    out.print(getCellVal(rs.getCols().get(1), col2Length));
                    out.println(getCellVal(rs.getCols().get(2), col3Length));
                }
            }
            return path;
        } catch (Exception e) {
            Helper.doMessageAlert("Error generating report", Constants.Messages.SEVERITY_ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<ReportSkeleton> getCustomerAppointmentReport() throws Exception {
        ObservableList<ReportSkeleton> ol = FXCollections.observableArrayList();
        String sql = "SELECT  MONTHNAME(start) AS 'MONTH',\n"
                + "		c.customerName,\n"
                + "		count(c.customerName) AS 'COUNT'\n"
                + "FROM appointment a\n"
                + "join customer c on c.customerId = a.customerId\n"
                + "GROUP BY c.customerName,MonthName(start)\n"
                + "ORDER BY 1;";
        PreparedStatement stmt = DBConn.getConnection().prepareStatement(sql);
        ResultSet rs = DBConn.doSelect(stmt);
        while (rs.next()) {
            ReportSkeleton cols = new ReportSkeleton(
                    rs.getString("MONTH"),
                    rs.getString("CUSTOMERNAME"),
                    Integer.toString(rs.getInt("COUNT"))
            );
            ol.add(cols);
        }
        return ol;
    }

    private static int getColLength(int colLength, String colName) {
        if (colLength < colName.length()) {
            colLength = colName.length() + 3;
        } else {
            colLength += 3;
        }
        return colLength;
    }

    private static String getCellVal(String col, int maxColLength) {
        String whiteSpace = col;
        int diff = maxColLength - col.length();
        for (int i = 0; i < diff; i++) {
            whiteSpace += " ";
        }
        whiteSpace += "|";
        return whiteSpace;
    }
}
