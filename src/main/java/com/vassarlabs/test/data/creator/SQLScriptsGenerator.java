/*package com.vassarlabs.test.data.creator;

import static com.vassarlabs.iwm.utils.IWMConstants.IS_DEBUG_ENABLED;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.iwm.soilmoisture.stress.service.api.ISoilMoistureNRSCDataService;

@Component
public class SQLScriptsGenerator {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	@Autowired
	protected ISoilMoistureNRSCDataService nrscDataService;

	public static BufferedWriter bw = null;
	public static FileWriter fw = null;
	public static BufferedWriter bw1 = null;
	public static FileWriter fw1 = null;
	public static BufferedWriter bwLog = null;
	public static FileWriter fwLog = null;

	public static void main(String[] args) throws DSPException, AppInitializationException, ObjectNotFoundException {
		// long gridID = gpsToGridIDMap.get(attributes[1]+" "+attributes[2]);

		SQLScriptsGenerator script = AppContext.getApplicationContext().getBean(SQLScriptsGenerator.class);
		script.start();
	}

	public void start() throws DSPException, AppInitializationException, ObjectNotFoundException {
		applicationInitService.initialize();
		Map<String, Long> gpsToGridIDMap = nrscDataService.getAllGPSToGridIDMap();
		/// home/srikanth/Desktop/VIC_WBC_20160601-20170323

		try {
			fw = new FileWriter("/home/srikanth/Desktop/soil_moisture_nrsc_data_scripts.sql");
			bw = new BufferedWriter(fw);
			fw1 = new FileWriter("/home/srikanth/Desktop/nrsc_file_upload_scrpits.sql");
			bw1 = new BufferedWriter(fw1);

			fwLog = new FileWriter("/home/srikanth/Desktop/LogSQL.txt");
			bwLog = new BufferedWriter(fwLog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File folder = new File("/home/srikanth/Desktop/VIC_WBC_20160601-20170323");
		File[] listOfFiles = folder.listFiles();

		long fileUploadID = 1;
		for (int i = 0; i < listOfFiles.length; i++) {
			int noOfRecords = 0;
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				String csvFile = "/home/srikanth/Desktop/VIC_WBC_20160601-20170323/" + fileName;
				BufferedReader br = null;
				String line = "";
				String lineSplitBy = " ";

				try {
					boolean validData;
					br = new BufferedReader(new FileReader(csvFile));
					while ((line = br.readLine()) != null) {
						noOfRecords++;
						String[] attributes = line.split(lineSplitBy);
						validData = validateData(attributes);
				    	if (!validData) {
							if (IS_DEBUG_ENABLED) 
							{
								System.out.println("Invalid record found (ignoring record) : " + line);
							}
							continue;
							
				    	}
						long gridID = -1;
						try {
							gridID = gpsToGridIDMap.get(attributes[1] + " " + attributes[2]);
						} catch (NullPointerException e) {
							bwLog.write(
									"File=" + fileName + " Lat Lang not found for grid id =" + attributes[0] +"\n");
							
						}
						if(gridID == -1){
							continue;
						}
						// fileName =NRSC_VIC_AP_3min_WBC_20160601.txt
						String str = fileName.split("_")[5];
						long modelDate = Integer.parseInt(str.substring(0, str.indexOf('.')));
						double evapotranspiration = Double.parseDouble(attributes[3]);
						double runoff = Double.parseDouble(attributes[4]);
						double l1 = Double.parseDouble(attributes[5]);
						double l2 = Double.parseDouble(attributes[6]);
						double l3 = Double.parseDouble(attributes[7]);
						String insertTS = "(select unix_timestamp()*1000)";
						int deleted = 0;
						int user_session_id = 0;

						String query = "insert into business_data.soil_moisture_nrsc_data("
								+ "grid_id, nrsc_file_upload_id, model_date, evapotranspiration, runoff, soil_moisture_L1,"
								+ "soil_moisture_L2, soil_moisture_L3, insert_ts, deleted, user_session_id)" + "values("
								+ gridID + ", " + fileUploadID + ", " + modelDate + ", " + evapotranspiration + ", "
								+ runoff + ", " + l1 + ", " + l2 + ", " + l3 + ", " + insertTS + ", " + deleted + ", "
								+ user_session_id + ");";

						// System.out.println(query);
						bw.write(query + "\n");
					}
					//bwLog.write("\n\n");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				String insertTS = "(select unix_timestamp()*1000)";
				String query1 = "insert into business_data.nrsc_file_upload("
						+ "file_upload_ts, no_of_records, filename, insert_ts, deleted, user_session_id)"
						+ "values("
						+ insertTS + ", " + noOfRecords + ", '" + fileName + "', " + insertTS + ", "
						+ 0 + ", " + 0 + ");";
				try {
					bw1.write(query1+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fileUploadID++;
			
		}
		try {
			bw.flush();
			bw.close();
			fw.close();
			bw1.flush();
			bw1.close();
			fw1.close();
			bwLog.flush();
			bwLog.close();
			fwLog.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private boolean validateData(String[] attributes) {

		boolean validData =  true;
		if (attributes == null || attributes.length == 0 || attributes.length < 8) {
			
			if (IS_DEBUG_ENABLED) {
				System.out.println("Invalid record with attributes[] length : " + attributes);
			}
			validData = false;
			return validData;
		}
		
		Double dValue = null;
		for (int i=3; i< attributes.length; i++) {
			try {
				dValue = Double.parseDouble(attributes[i]);
				if(dValue.isNaN()) {
					if (IS_DEBUG_ENABLED) {
						System.out.println("Invalid record data (isNan) : " + dValue);
					}
					//System.out.println("Found NaN");
					validData = false;
					return validData;
				}
				if (dValue < 0) {
					if (IS_DEBUG_ENABLED) {
						System.out.println("Invalid record data (negative value) : " + dValue);
					}
					validData = false;
					return validData;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid record data :  " + attributes + " : " + nfe.getMessage());
				validData = false;
				return validData;
			}
    	}
		return validData;
	}

}
*/