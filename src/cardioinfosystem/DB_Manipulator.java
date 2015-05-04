/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools $ Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author Student
 */
public class DB_Manipulator 
{
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static Connection connection;
    private static PreparedStatement prepStmt;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm"); //HL7 standard time format

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
       
        public static boolean checkCISID(String cis_id) throws SQLException {
            
            boolean exists = true;

            try {

                /*
                 *  The Prepared Statement will pull out resultset of MRN
                 */
                prepStmt = connection.prepareStatement("SELECT cis_id FROM patient_info WHERE ident_list = " + cis_id);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet rs = prepStmt.executeQuery();
                
                if (!rs.next()) {
                    exists = false;
                }
                
            } catch (SQLException se) {
                System.out.println("Error in DB_Manipulator.checkCSID: " + se);
            } finally {
                prepStmt.close();            
            }
            return exists;
        }
        
        public static String createFillerNumber(String cis_id, String sender_num, String order_content) throws SQLException {
            
            String filler_num = "";
            
            try {

                /*
                 *  The Prepared Statement will insert sender_id, order content,
                 */
                                
                prepStmt = connection.prepareStatement(
                        "INSERT INTO TABLE order (cpoe_id, order_content, cis_id) VALUES (" + sender_num + ", " + order_content + ", " + cis_id + " where cis_id = " + cis_id + ")");

                rs = prepStmt.executeQuery();
                
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
        
        public static String getDataSN(String cis_id) throws SQLException {

            String filler = createFillerNumber(cis_id, "", ""); // generates filler number
            
            String mrn = "";
            String name = "";
            String dob = "";
            String sex = "";
            String address ="";
            String homeph = "";
            String workph = "";
            String patient_acc = "";
            String orc_filler = filler;
            String orc_dttrans = assignDateTime();
            String obr_filler = filler;
            String obr_uid = "Echo";
            String obr_obsdt = "";
            String provider_id = "";


            try {

                // get the patient data
                
                prepStmt = connection.prepareStatement("SELECT Ident_List, Pt_Name, Pt_DOB, Pt_DOB, Pt_Address, Home_Phone, Work_Phone, Pt_Acct_Num FROM patient WHERE cis_id = " + cis_id);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet rs = prepStmt.executeQuery();
                
                while(rs.next())
                {
                    mrn = rs.getString(1);
                    name = rs.getString(2);
                    dob = rs.getString(3);
                    sex = rs.getString(4);
                    address = rs.getString(5);
                    homeph = rs.getString(6);
                    workph = rs.getString(7);
                    patient_acc = rs.getString(8);
                }
                
                // get the doctor id

                prepStmt = connection.prepareStatement(
                "SELECT Doctor_ID FROM visit WHERE CIS_ID = " + cis_id);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet second_rs = prepStmt.executeQuery();
                
                while(second_rs.next())
                {
                    provider_id = rs.getString(1);
                }

            } catch (SQLException se) {
                System.out.println("Error in DB_Manipulator.getDataSN: " + se);
            } finally {
                prepStmt.close();            
            }
            return mrn + "$" + name + "$" + dob + "$" + sex + "$" + address + "$" + homeph + "$" + workph + "$" 
                    + patient_acc + "$" + orc_filler + "$" + orc_dttrans + "$" + obr_filler + "$" + obr_uid + "$" + assignDateTime() + "$" + provider_id;
        }   
        
        public static String getResults(String cis_id) throws SQLException {

            String filler = createFillerNumber(mrn, "", ""); // generates filler number
            
            String mrn = "";
            String name = "";
            String dob = "";
            String sex = "";
            String address ="";
            String homeph = "";
            String workph = "";
            String patient_acc = "";
            String orc_filler = "";
            String orc_dttrans = assignDateTime();
            String obr_filler = "";
            String obr_uid = "";
            String status = "";
            String obr_obsdt = "";
            String provider_id = "";
            String result_value = "";


            try {
                
                // getting the patient data
                                
                prepStmt = connection.prepareStatement(
                        "SELECT Ident_List, Pt_Name, Pt_DOB, Pt_DOB, Pt_Address, Home_Phone, Work_Phone, Pt_Acct_Num FROM patient WHERE ident_list = " + cis_id);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet rs = prepStmt.executeQuery();
                
                while(rs.next())
                {
                    mrn = rs.getString(1);
                    name = rs.getString(2);
                    dob = rs.getString(3);
                    sex = rs.getString(4);
                    address = rs.getString(5);
                    homeph = rs.getString(6);
                    workph = rs.getString(7);
                    patient_acc = rs.getString(8);
                }
                
                // getting the doctor id               
                // getting the filler number
                
                prepStmt = connection.prepareStatement(
                "SELECT cis_order_id, provider_id, test_code, result_status, result_value FROM cis_order WHERE cis_id = " + cis_id);

                @SuppressWarnings("LocalVariableHidesMemberVariable")
                ResultSet second_rs = prepStmt.executeQuery();
                
                while(second_rs.next())
                {
                    orc_filler = second_rs.getString(1);
                    obr_filler = second_rs.getString(1);
                    provider_id = second_rs.getString(2);
                    obr_uid = second_rs.getString(3);
                    status = second_rs.getString(4);
                    result_value = second_rs.getString(5);
                }

            } catch (SQLException se) {
                System.out.println("Error in DB_Manipulator.getDataSN: " + se.getMessage());
            } finally {
                prepStmt.close();            
            }
            return mrn + "$" + name + "$" + dob + "$" + sex + "$" + address + "$" + homeph + "$" + workph + "$" 
                    + patient_acc + "$" + orc_filler + "$" + orc_dttrans + "$" + obr_filler + "$" + obr_uid + "$" + assignDateTime() + "$" + provider_id + "$" + status + "$" + result_value;
        }   
        
	private static String assignDateTime() {
		Date currentDateTime = new Date();
		return formatter.format(currentDateTime);
	}
    
}
