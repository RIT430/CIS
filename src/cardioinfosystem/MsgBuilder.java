/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Penepent
 * @author Ethan Applebee
 */
public final class MsgBuilder {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm"); //HL7 standard time format
	private static ArrayList<String> MSH_fields;
	private static ArrayList<String> EVN_fields;
	private static ArrayList<String> PID_fields;
	private static ArrayList<String> PV1_fields;
	private static ArrayList<String> ORC_fields;
	private static ArrayList<String> OBR_fields;
	private static ArrayList<String> OBX_fields;
	
	/**
	 * 
	 */
	private MsgBuilder() {
	}

	/**
	 * Generic function to construct an HL7 message.
	 * @param segList List of HL7 segments to construct message with.
	 * @param fieldsList List of fields for segments named in <code>segList</code>.
	 * @return <code>msg</code> constructed HL7 message
	 */
	private static String construct_message(List<String> segList, List<List<String>> fieldsList) {
		String msg = "";
		int segIndex = 0;
		for (String seg : segList) {
			msg = msg.concat(seg);
			List<String> fields = fieldsList.get(segIndex);
			for (String field : fields) {
				msg = msg.concat("|"+field);
			}
			msg = msg.concat("\n");
			segIndex++;
		}
//		System.out.print(msg.replace('\r', '\n'));
		return msg;
	}
	
	private static void prefillSegmentFields() {
		MSH_fields = new ArrayList<>();
		MSH_fields.addAll(Arrays.asList("^~\\&", "CIS", "RIT430", "BEDBOARD", "SMH", "P", "2.3"));
		MSH_fields.addAll(5, Collections.nCopies(4, ""));
		EVN_fields = new ArrayList<>(Arrays.asList("CIS"));
		EVN_fields.addAll(0, Collections.nCopies(4, ""));
		PID_fields = new ArrayList<>(Arrays.asList("1"));
		PID_fields.addAll(Collections.nCopies(17, ""));
		PV1_fields = new ArrayList<>(Arrays.asList("1","","","A"));
		PV1_fields.addAll(Collections.nCopies(6, ""));
		PV1_fields.addAll(Collections.nCopies(35, ""));
		ORC_fields = new ArrayList<>(Collections.nCopies(9, ""));
		OBR_fields = new ArrayList<>(Collections.nCopies(24, ""));
		OBR_fields.add(0, "1");
//		OBX_fields = Arrays.asList();
	}
	
	/**
	 * Creates a timestamp for anything that is newly created (orders,
	 * messages, etc.) in HL7 time format.
	 * @return the current date and time in HL7 standard format
	 */
	private static String assignDateTime() {
		Date currentDateTime = new Date();
		return formatter.format(currentDateTime);
	}

	/**
	 * Generate a new ORM-SN message. (Send filler number to CPOE.)<br>
	 * Expected order of msgData: EVN2_RECDT, EVN3_DTPLAN, EVN5_OPERID,
	 * PID3_IDLIST, PID5_PTNAME, PID7_DOB, PID8_SEX, PID11_ADDRESS,
	 * PID13_HOMEPH, PID14_WORKPH, PID18_ACC, PV1-3_LOC, PV1-7_ATTENDING,
	 * PV1-10_SERVICE, PV1-44_ADMIT, PV1-45_DSCHG, ORC3_FILLER, ORC9_DTTRANS,
	 * OBR3_FILLER, OBR4_UID, OBR7_OBSDT, OBR16_PROV, OBR25_STATUS
	 * @param msgData Parameters pulled from database for <code>construct_message()</code>.
	 */
	public static void sendFillerNumber(ArrayList<String> msgData) {
		List<String> segList = Arrays.asList("MSH","EVN","PID","PV1","ORC","OBR");
		prefillSegmentFields();
		List<List<String>> fieldsList = Arrays.asList(MSH_fields,EVN_fields,PID_fields,PV1_fields,ORC_fields,OBR_fields);

		//MSH
		fieldsList.get(0).set(5, assignDateTime());
		fieldsList.get(0).set(7, "ORM^O01");
		//EVN
		fieldsList.get(1).set(0, "O01");
		fieldsList.get(1).set(1, msgData.get(0));	//RECDT
		fieldsList.get(1).set(2, msgData.get(1));	//DTPLAN
		fieldsList.get(1).set(3, "02");				//Physician order
		fieldsList.get(1).set(4, msgData.get(2));	//OPERID
		//PID
		fieldsList.get(2).set(2, msgData.get(3));	//IDLIST
		fieldsList.get(2).set(4, msgData.get(4));	//PTNAME
		fieldsList.get(2).set(6, msgData.get(5));	//DOB
		fieldsList.get(2).set(7, msgData.get(6));	//SEX
		fieldsList.get(2).set(10, msgData.get(7));	//ADDRESS
		fieldsList.get(2).set(12, msgData.get(8));	//HOMEPH
		fieldsList.get(2).set(13, msgData.get(9));	//WORKPH
		fieldsList.get(2).set(17, msgData.get(10));	//ACC
		//PV1
		fieldsList.get(3).set(2, msgData.get(11));	//LOC
		fieldsList.get(3).set(6, msgData.get(12));	//ATTENDING
		fieldsList.get(3).set(9, msgData.get(13));	//SERVICE
		fieldsList.get(3).set(43, msgData.get(14)); //ADMIT
		fieldsList.get(3).set(44, msgData.get(15)); //DSCHG
		//ORC
		fieldsList.get(4).set(0, "SN");
		fieldsList.get(4).set(2, msgData.get(16));	//FILLER
		//v--what IS the date/time of transaction?
		fieldsList.get(4).set(8, msgData.get(17));	//DTTRANS
		//OBR
		fieldsList.get(5).set(2, msgData.get(18));	//FILLER
		fieldsList.get(5).set(3, msgData.get(19));	//UID
		fieldsList.get(5).set(6, msgData.get(20));	//OBSDT
		fieldsList.get(5).set(15, msgData.get(21)); //PROV
		fieldsList.get(5).set(24, msgData.get(22)); //STATUS
		
		String orderMsg = construct_message(segList, fieldsList);
	}

	/**
	 * Generate a new ORU message. (Send results to CPOE.)
	 * Expected order of msgData: EVN2_RECDT, EVN3_DTPLAN, EVN5_OPERID,
	 * PID3_IDLIST, PID5_PTNAME, PID7_DOB, PID8_SEX, PID11_ADDRESS,
	 * PID13_HOMEPH, PID14_WORKPH, PID18_ACC, PV1-3_LOC, PV1-7_ATTENDING,
	 * PV1-10_SERVICE, PV1-44_ADMIT, PV1-45_DSCHG, ORC3_FILLER, ORC9_DTTRANS,
	 * OBR3_FILLER, OBR4_UID, OBR7_OBSDT, OBR16_PROV, OBR25_STATUS
	 * @param msgData Parameters pulled from database for <code>construct_message()</code>.
	 */
	public static void sendResults(ArrayList<String> msgData) {
		List<String> segList = Arrays.asList("MSH","PID","PV1","OBR");
		//figure out how many OBX here
		String msgData.get(20);
		prefillSegmentFields();
		List<List<String>> fieldsList = Arrays.asList(MSH_fields,EVN_fields,PID_fields,PV1_fields,ORC_fields,OBR_fields);
	}
		
}
