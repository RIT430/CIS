/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

/**
 *
 * @author Chris
 */
public class Main {

	private static FileWriter writer;
	private static String mirthInputDir; //replace with appropriate path

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		String cis_id = "";
		String input = "";

		try {
			DB_Manipulator.init();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Welcome to the Cardiology Information System.");

		do {
			System.out.println("Please enter a patient's ID or zero to quit.\n");
			try {
				cis_id = br.readLine();
			} catch (IOException ioe) {
				System.out.print(ioe.getMessage());
			}

			/*
			 Validate MRN
			 */
			try {
				if (DB_Manipulator.checkCISID(cis_id)) {
					System.out.println("What would you like to do?\n");
					System.out.println("1 - Create SN Order\n"
							+ "2 - Send Results\n"
							+ "3 - Select Another Patient\n");

					try {
						input = br.readLine();
					} catch (IOException ioe) {
						System.out.print(ioe.getMessage());
					}
					switch (input) {
						case "1":
							sendMsg(DB_Manipulator.getDataSN(cis_id));
                                                        break;
						case "2":
							sendMsg(DB_Manipulator.getResults(cis_id));
                                                        break;
						case "3":
							break;
						default:
							System.out.println("Invalid input.\n");
							break;
					}
				} else {
					System.out.println("That patient does not exist in the database.\n");
					break;
				}
			} catch (SQLException sqle) {
				System.out.print(sqle.getMessage());
			}

		} while (true);

		try {
			br.close();
		} catch (IOException ioe) {
			System.out.print(ioe.getMessage());
		}

		System.out.println("Thank you, come again!");
		System.exit(0);
	}

	private static void sendMsg(String msg) {
            if(msg.contains("OBX") && msg.contains("ECHO")){
                mirthInputDir = "ORU_ECHO.txt";
            }else if(msg.contains("OBX") && msg.contains("ECG")){
                mirthInputDir = "ORU_ECG.txt";
            }else{
                mirthInputDir = "ORM_SN.txt";
            }
		try {
			writer = new FileWriter(mirthInputDir);
			writer.write(msg);
			writer.close();
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
