/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Student
 */
public class DB_Manipulator 
{
    private static Statement stmt = null;
    private final static ResultSet rs = null;
    private static Connection connection;
    private static PreparedStatement prepStmt;

    private DB_Manipulator() throws ClassNotFoundException, SQLException {
        //Register the JDBC driver for MySQL.
        Class.forName("com.mysql.jdbc.Driver");
        // Use localhost or 127.0.0.1 to point to your machine
        String url = "jdbc:mysql://127.0.0.1/cis_db";
        String userName = "cisman";
        String password = "cisman";
        connection = DriverManager.getConnection(url, userName, password);
        stmt = connection.createStatement();
    }            
       
        public static boolean checkMRN(String mrn) throws SQLException {
            
            boolean exists = true;

            try {

                /*
                 *  The Prepared Statement will pull out resultset of MRN
                 */
                prepStmt = connection.prepareStatement(
                        "SELECT ident_list FROM patient_info WHERE ident_list = " + mrn);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet rs = prepStmt.executeQuery();
                
                if (!rs.next()) {
                    exists = false;
                }
                
            } catch (SQLException se) {
                System.out.println("Error in DB_Manipulator.checkMRN: " + se);
            } finally {
                prepStmt.close();            
            }
            return exists;
        }
        
        public static String createFillerNumber(String mrn, String sender_num, String order_content) throws SQLException {
            
            String filler_num = "";
            String cis_id = "";
            
            try {

                /*
                 *  The Prepared Statement will insert sender_id, order content,
                 */
                prepStmt = connection.prepareStatement(
                        "SELECT cis_id FROM patient WHERE ident_list = " + mrn);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet rs = prepStmt.executeQuery();
                
                while(rs.next())
                {
                    cis_id = rs.getString(1);
                }
                                
                prepStmt = connection.prepareStatement(
                        "INSERT INTO TABLE order (cpoe_id, order_content, cis_id) VALUES (" + sender_num + ", " + order_content + ", " + cis_id + ")");

                prepStmt = connection.prepareStatement(
                        "SELECT cis_order_id FROM order WHERE cis_id = " + cis_id); 
                
                rs = prepStmt.executeQuery();
                
                while(rs.next())
                {
                    filler_num = rs.getString(1);
                }
                
            } catch (SQLException se) {
                System.out.println("Error in DB_Manipulator.createFillerNumber: " + se);
            } finally {
                prepStmt.close();            
            }
            return filler_num;
        }
        
        public static ArrayList<String> getDataSN(String mrn, String filler_num) throws SQLException {
            
            ArrayList<String> list = new ArrayList<String>();
            String cis_id = "";
            
            try {

                /*
                 *  The Prepared Statement will insert sender_id, order content,
                 */
                prepStmt = connection.prepareStatement(
                        "SELECT cis_id FROM patient WHERE ident_list = " + mrn);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet rs = prepStmt.executeQuery();
                
                while(rs.next())
                {
                    cis_id = rs.getString(1);
                }
                                
                prepStmt = connection.prepareStatement(
                        "INSERT INTO TABLE order (cpoe_id, order_content, cis_id) VALUES (" + sender_num + ", " + order_content + ", " + cis_id + ")");

                prepStmt = connection.prepareStatement(
                        "SELECT cis_order_id FROM order WHERE cis_id = " + cis_id); 
                
                rs = prepStmt.executeQuery();
                
                while(rs.next())
                {
                    filler_num = rs.getString(1);
                }
                
            } catch (SQLException se) {
                System.out.println("Error in DB_Manipulator.getDataSN: " + se);
            } finally {
                prepStmt.close();            
            }
            return list;
        }    
    
}
