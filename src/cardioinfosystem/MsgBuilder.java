/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardioinfosystem;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Christopher Penepent
 * @author Ethan Applebee
 */
public final class MsgBuilder {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); //HL7 standard time format
	private static ArrayList<String> MSH_fields;
	private static ArrayList<String> PID_fields;
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
	 * @param fieldsPerSeg list of number of supported fields per segment
	 * @return <code>msg</code> constructed HL7 message
	 */
	private static String constructEmptyMsg(List<String> segList, List<Integer> fieldsPerSeg) {
		String msg = "";
		int segIndex = 0;
		for (String seg : segList) {
			msg = msg.concat(seg);
			int segFields = fieldsPerSeg.get(segIndex).intValue();
			for (int i=0; i < segFields; i++) {
				msg = msg.concat("|{" + i + "}");
			}
			msg = msg.concat("\n");
			segIndex++;
		}
		
		return msg;
	}
	
	private static void padEmptyFields(ArrayList<String> segList, ArrayList<String> data) {
	
		do {
			for (String seg : segList) {
				switch (seg) {
						case "MSH":
							data.addAll(0,Arrays.asList("^~\\&", "CIS", "RIT430", "BEDBOARD", "SMH", "P", "2.3"));
							data.addAll(5, Collections.nCopies(4, ""));
						case "PID":
							data.add(11,"1");
							data.add(13,"");
							data.add(15,"");
							data.add(17,"");
							data.add(20,"");
							data.add(21,"");
							data.add(23,"");
							data.addAll(26,Collections.nCopies(3, ""));
						case "ORC":
							data.addAll(32,Collections.nCopies(5, ""));
						case "OBR":
							data.add(38,"1");
							
							//38
							
						case "OBX":
							continue;
						default:
							break;
			
				}
			}
		} while(true);
		
	

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
	 * Generate a new ORM-SN message.<br>
	 * Expected order of msgData: 
	 * PID3_IDLIST, PID5_PTNAME, PID7_DOB, PID8_SEX, PID11_ADDRESS,
	 * PID13_HOMEPH, PID14_WORKPH, PID18_ACC, ORC3_FILLER, ORC9_DTTRANS,
	 * OBR3_FILLER, OBR4_UID, OBR7_OBSDT, OBR16_PROV, OBR25_STATUS
	 * @param msgData Parameters pulled from database for <code>constructEmptyMessage()</code>.
	 * @return 
	 */
	public static String createMsgSN(ArrayList<String> msgData) {
		List<String> segList = Arrays.asList("MSH","PID","ORC","OBR");
		padEmptyFields(msgData);
//		List<List<String>> fieldsList = Arrays.asList(MSH_fields,PID_fields,ORC_fields,OBR_fields);

		List<Integer> fieldsPerSeg = Arrays.asList(new Integer(11), new Integer(9), new Integer(4), new Integer(7));
		String msgPattern = MsgBuilder.constructEmptyMsg(segList, fieldsPerSeg);
		
		String msgSN = MessageFormat.format(msgPattern, msgData.toArray());
		return msgSN;
		
		/*
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
		
		String orderMsg = constructEmptyMessage(segList, fieldsList);
		*/
	}

	/**
	 * Generate a new ORU message. (Send results to CPOE.)
	 * Expected order of msgData: EVN2_RECDT, EVN3_DTPLAN, EVN5_OPERID,
	 * PID3_IDLIST, PID5_PTNAME, PID7_DOB, PID8_SEX, PID11_ADDRESS,
	 * PID13_HOMEPH, PID14_WORKPH, PID18_ACC, PV1-3_LOC, PV1-7_ATTENDING,
	 * PV1-10_SERVICE, PV1-44_ADMIT, PV1-45_DSCHG, ORC3_FILLER, ORC9_DTTRANS,
	 * OBR3_FILLER, OBR4_UID, OBR7_OBSDT, OBR16_PROV, OBR25_STATUS
	 * @param msgData Parameters pulled from database for <code>constructEmptyMessage()</code>.
	 */
	public static void sendResults(ArrayList<String> msgData) {
		List<String> segList = Arrays.asList("MSH","PID","PV1","OBR");
		//figure out how many OBX here
		String msgData.get(20);
		padEmptyFields();
		List<List<String>> fieldsList = Arrays.asList(MSH_fields,EVN_fields,PID_fields,PV1_fields,ORC_fields,OBR_fields);
	}
		
}
