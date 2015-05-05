/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools $ Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Ethan Applebee
 */
public class DB_Manipulator {

	private static ResultSet rs = null;
	private static Connection connection;
	private static PreparedStatement prepStmt;
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm"); //HL7 standard time format

	private DB_Manipulator(){}

	public static void init() throws SQLException, ClassNotFoundException {
		//Register the JDBC driver for MySQL.
		Class.forName("com.mysql.jdbc.Driver");
		// Use localhost or 127.0.0.1 to point to your machine
		String url = "jdbc:mysql://127.0.0.1:3306/CIS_DB";
		String userName = "cisman";
		String password = "cisman";
		connection = DriverManager.getConnection(url, userName, password);
	}

	public static boolean checkCISID(String cis_id) throws SQLException {
		boolean exists = true;

		try {

			/*
			 *  The Prepared Statement will pull out resultset of MRN
			 */
			prepStmt = connection.prepareStatement("SELECT cis_id FROM patient_info WHERE cis_id = " + cis_id);
			rs = prepStmt.executeQuery();
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

	public static String createFillerNumber(String cis_id) throws SQLException {
		String filler_num = "";
                String PT_MRN = "";
                String PROVIDER_ID = "";
		try {

			/*
			 *  The Prepared Statement will insert sender_id, order content,
			 */
                        prepStmt = connection.prepareStatement(
                                        "Select PT_ID from patient_info where CIS_ID = " + cis_id);
                        rs = prepStmt.executeQuery();
                        while (rs.next()){
                            PT_MRN = rs.getString(1);
                        }
                        prepStmt = connection.prepareStatement(
                                        "Select DOCTOR_ID from visit where PT_ID = " + PT_MRN);
                        rs = prepStmt.executeQuery();
                        while (rs.next()){
                            PROVIDER_ID = rs.getString(1);
                        }
                        
			prepStmt = connection.prepareStatement(
					"INSERT INTO cis_order (PT_ID, TEST_CODE, TEST_DESCRIPTION, OBS_DATE, PROVIDER_ID, RESULT_STATUS) VALUES ('"
					+ PT_MRN + "', 'ECHO', 'ECHOCARDIOGRAM', '201504191955', " + PROVIDER_ID + ", 'P')");
			prepStmt.execute();

			prepStmt = connection.prepareStatement(
					"SELECT FILLER_ID FROM cis_order WHERE PT_ID = " + PT_MRN);
			rs = prepStmt.executeQuery();

			while (rs.next()) {
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
		String filler = createFillerNumber(cis_id); // generates filler number

		String mrn = "";
		String name = "";
		String dob = "";
		String sex = "";
		String address = "";
		String homeph = "";
		String workph = "";
		String patient_acc = "";
		String orc_filler = filler;
		String orc_dttrans = assignDateTime();
		String obr_filler = filler;
		String obr_uid = "ECHO";
//		String obr_obsdt = ""; Variable is not used, time is generated when msg is sent
		String provider_id = "";

		try {
			// get the patient data
			prepStmt = connection.prepareStatement("SELECT PT_ID, Pt_Name, Pt_DOB, Pt_GENDER, Pt_Address, Home_Phone, Work_Phone, Pt_Acct_Num FROM patient_info WHERE cis_id = " + cis_id);
			rs = prepStmt.executeQuery();

			while (rs.next()) {
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
					"SELECT Doctor_ID FROM visit WHERE PT_ID = " + mrn);
			rs = prepStmt.executeQuery();

			while (rs.next()) {
				provider_id = rs.getString(1);
			}
                        

		} catch (SQLException se) {
			System.out.println("Error in DB_Manipulator.getDataSN: " + se);
		} finally {
			prepStmt.close();
		}

		return mrn + "$" + name + "$" + dob + "$" + sex + "$" + address + "$" + homeph + "$" + workph + "$"
				+ patient_acc + "$" + orc_filler + "$" + orc_dttrans + "$" + obr_filler + "$" + obr_uid + "$"
				+ assignDateTime() + "$" + provider_id; //assignDateTime() here is OBR7_OBSDT
	}

	public static String getResults(String cis_id) throws SQLException {

		String mrn = "";
		String name = "";
		String dob = "";
		String sex = "";
		String address = "";
		String homeph = "";
		String workph = "";
		String patient_acc = "";
		String obr_filler = "";
		String obr_uid = "";
		String status = "";
//		String obr_obsdt = ""; Variable is not used, time is generated when msg is sent
		String provider_id = "";
		String result_value = "";

		try {
			// getting the patient data
			prepStmt = connection.prepareStatement(
					"SELECT PT_ID, Pt_Name, Pt_DOB, Pt_GENDER, Pt_Address, Home_Phone, Work_Phone, Pt_Acct_Num FROM patient_info WHERE CIS_ID = " + cis_id);

			rs = prepStmt.executeQuery();

			while (rs.next()) {
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
					"SELECT FILLER_ID, provider_id, test_code, result_status, result_value FROM cis_order WHERE PT_ID = " + mrn);
			rs = prepStmt.executeQuery();

			while (rs.next()) {
				obr_filler = rs.getString(1);
				provider_id = rs.getString(2);
				obr_uid = rs.getString(3);
				status = rs.getString(4);
				result_value = rs.getString(5);
			}

		} catch (SQLException se) {
			System.out.println("Error in DB_Manipulator.getDataSN: " + se.getMessage());
		} finally {
			prepStmt.close();
		}
		return mrn + "$" + name + "$" + dob + "$" + sex + "$" + address + "$" + homeph + "$" + workph + "$"
				+ patient_acc + "$" + "$" + obr_filler + "$" + obr_uid + "$"
				+ assignDateTime() + "$" + provider_id + "$" + status + "$" + result_value;
				//assignDateTime() here is OBR7_OBSDT
	}

	private static String assignDateTime() {
		Date currentDateTime = new Date();
		return formatter.format(currentDateTime);
	}
}