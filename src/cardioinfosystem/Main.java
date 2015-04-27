/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Chris
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		ArrayList<String> params = new ArrayList<>();
		params.addAll(Arrays.asList("20150427","20150427","12345^Lambeer^Bill^^^^^^^^^^^^^^^^^MD",
				"1274499^^^^MR^Strong Memorial Hospital~800999^^^^MR^Highland Hospital",
				"Smith^John","19831002","M","742 Evergreen Terrace^^Springfield^XX^99999",
				"^^^^^585^5550199","^^^^^585^2752100","329123456","83400^101^01",
				"12345^Lambeer^Bill^^^^^^^^^^^^^^^^^MD","MED","20150421","","123",
				"20150427","123","ECHO^Echocardiogram","","12345",""));
		MsgBuilder.sendFillerNumber(params);
	}
	
	/**
	 * Generate new filler number.
	 * @return
	 */
	private int assignOrderNumber() {
		//should probably find highest value stored in orders table, then return that+1
		return 0;
	}
	
}
