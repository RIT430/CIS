/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	int mrn;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

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

		do {

			System.out.println("Welcome to the Cardiology Information System.");
			System.out.println("Please enter a patient's MRN.\n");
			int mrn = Integer.parseInt(br.readLine());

			/*
			 Validate MRN
			 */
			if (/*MRN EXISTS IN DATABASE*/) {

				System.out.println("What would you like to do?");
				System.out.println("1 - Create Order\n"
						+ "2 - Send Results\n"
						+ "3 - Quit\n");

				int input = br.readLine();

				switch (input) {

					case 1: // SQL code that retrieves filler number

					case 2: // Calls method to send result

					case 3:
						break;

				}
			} else {

				System.out.println("That patient does not exist in the database.\n");

			}

		} while (true);

		br.close();
		System.exit(0);      

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