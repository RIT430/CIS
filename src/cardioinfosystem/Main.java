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

	private FileWriter writer;
	private final String mirthInputDir = "TEST.TXT"; //replace with appropriate path


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

        String mrn = "";
        String input = "";
        String type = "";
        String status = "";
        
      // TODO code application logic here
		/*ArrayList<String> params = new ArrayList<>();
		 params.addAll(Arrays.asList("20150427","20150427","12345^Lambeer^Bill^^^^^^^^^^^^^^^^^MD",
		 "1274499^^^^MR^Strong Memorial Hospital~800999^^^^MR^Highland Hospital",
		 "Smith^John","19831002","M","742 Evergreen Terrace^^Springfield^XX^99999",
		 "^^^^^585^5550199","^^^^^585^2752100","329123456","83400^101^01",
		 "12345^Lambeer^Bill^^^^^^^^^^^^^^^^^MD","MED","20150421","","123",
		 "20150427","123","ECHO^Echocardiogram","","12345",""));
		 MsgBuilder.sendFillerNumber(params); */
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Welcome to the Cardiology Information System.");
                
		do {
			System.out.println("Please enter a patient's MRN.\n");
			try {
                            mrn = br.readLine();
                        } 
                        catch (IOException ioe) {
                            System.out.print(ioe.getMessage());
                        }
                            
			/*
			 Validate MRN
			 */
                        
                        try {
                            
                            if (DB_Manipulator.checkMRN(mrn)) {

                                System.out.println("What would you like to do?\n");
                                System.out.println("1 - Create SN Order\n"
                                                 + "2 - Send Results\n"
                                                 + "3 - Quit\n");

                                try {
                                    input = br.readLine();
                                }
                                catch (IOException ioe) {
                                    System.out.print(ioe.getMessage());
                                }

                                switch (input) {

                                    case "1": 

                                    case "2": // Calls method to send result

                                    case "3":   break;

                                    default: System.out.println("Invalid input.\n");
                                }

                            } else {

                                System.out.println("That patient does not exist in the database.\n");
                            }
                        }
                        catch (SQLException sqle) {
                            System.out.print(sqle.getMessage());
                        }

		} while (true);

		try {
                    br.close();
                }
                catch (IOException ioe)
                {
                    System.out.print(ioe.getMessage());
                }
                
                System.out.println("Thank you, come again!");
		System.exit(0);    
        }

	private static void sendMsg(String msg) {
		try {
			MsgBuilder.writer = new FileWriter(MsgBuilder.mirthInputDir);
			MsgBuilder.writer.write(msg);
			MsgBuilder.writer.close();
		} catch (IOException ex) {
			Logger.getLogger(MsgBuilder.class.getName()).log(Level.SEVERE, null, ex);
		}
		/**
		 * Generate new filler number.
		 *
		 * @return
		 */
    
	private int assignOrderNumber() {
		//should probably find highest value stored in orders table, then return that+1
		return 0;
	}

}