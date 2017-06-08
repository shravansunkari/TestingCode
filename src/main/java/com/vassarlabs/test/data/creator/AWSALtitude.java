/*package com.vassarlabs.test.data.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.location.pojo.api.ILocation;
import com.vassarlabs.location.service.api.ILocationHierarchyService;

@Component
public class AWSALtitude {
	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	@Autowired
	// @Qualifier("StormInitServiceImpl")
	protected ILocationHierarchyService locService;

	void test() {
		try {
			applicationInitService.initialize();
		} catch (AppInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Start of IWM TEST");
		AWSALtitude awsAltitudeCreator = AppContext.getApplicationContext().getBean(AWSALtitude.class);
		awsAltitudeCreator.test();
		String awsInputFilePath = "/home/srikanth/CodeBaseGit/iwm/AWD_ElevationData_LatLng.csv.xlsx";
		String awsOutputFilePath = "/home/srikanth/CodeBaseGit/iwm/AWS_altitude.sql";

		awsAltitudeCreator.generateInsertStatements(awsInputFilePath, awsOutputFilePath);
	}

	private void generateInsertStatements(String awsInputFilePath, String awsOutputFilePath) {
		FileInputStream inputStream;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(awsOutputFilePath, true);
			inputStream = new FileInputStream(new File(awsInputFilePath));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			System.out.println("File Read");
			// Skip Header
			iterator.next();
			int i = 0;
			long timeStamp = System.currentTimeMillis();
			int EXTERNAL_ID_COLUMN = 3;
			int ALTITUDE_COLUMN = 2;
			// String replaceKey_EXT_ID = "XXXXXXXXXXXXXXXXXXXXXXX";
			String insertGroupMDInstance = "INSERT INTO platform_data.extension_group_instance"
					+ "(extension_group_md_id,extension_entity_map_id,insert_ts,deleted,user_session_id)"
					+ "VALUES(((select extension_group_md_id from extension_group_md where name = 'PHYSICAL' )),"
					+ "(select extension_entity_map_id from location where external_id  = 'XXXXXXXXXXXXXXXXXXXXXXX')"
					+ "," + timeStamp + ",0,1);";

			// String replaceKey_ALT_ID = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA";
			String insertExtensionValue = "INSERT INTO platform_data.extension_value(extension_group_instance_id,extension_md_id,"
					+ "insert_ts,deleted,user_session_id,value) values"
					+ "((select extension_group_instance_id from extension_group_instance where extension_entity_map_id = "
					+ "(select extension_entity_map_id from location where external_id  = 'XXXXXXXXXXXXXXXXXXXXXXX') and "
					+ "extension_group_md_id = ((select extension_group_md_id from extension_group_md where name ='altitude'))),"
					+ "(select extension_md_id from extension_md where name = 'altitude')," + timeStamp
					+ ",0,1,'AAAAAAAAAAAAAAAAAAAAAAAAAAAA');";
			while (iterator.hasNext()) {
				i++;
				if (i > 4)
					break;
				Row row = iterator.next();
				int dd = new Double(row.getCell(EXTERNAL_ID_COLUMN).getNumericCellValue()).intValue();
				String externalId = String.valueOf(dd);
				ILocation loc;
				try {
					loc = locService.getLocationByExternalId(externalId);
					if (loc != null) {
						if (row.getCell(ALTITUDE_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK) {
							double av = row.getCell(ALTITUDE_COLUMN).getNumericCellValue();
							String altitudeValue = String.valueOf(av);
							String currGroupInstanceInsert = "INSERT INTO platform_data.extension_group_instance"
									+ "(extension_group_md_id,extension_entity_map_id,insert_ts,deleted,user_session_id)"
									+ "VALUES(((select extension_group_md_id from extension_group_md where name = 'PHYSICAL' )),"
									+ "(select extension_entity_map_id from location where external_id  = '"
									+ externalId + "')" + "," + timeStamp + ",0,1);";

							String currValueInsert = "INSERT INTO platform_data.extension_value(extension_group_instance_id,extension_md_id,"
									+ "insert_ts,deleted,user_session_id,value) values"
									+ "((select extension_group_instance_id from extension_group_instance where extension_entity_map_id = "
									+ "(select extension_entity_map_id from location where external_id  = '"
									+ externalId + "') and "
									+ "extension_group_md_id = ((select extension_group_md_id from extension_group_md where name ='PHYSICAL'))),"
									+ "(select extension_md_id from extension_md where name = 'altitude')," + timeStamp
									+ ",0,1,'" + altitudeValue + "');";

							System.out.println(currGroupInstanceInsert);
							System.out.println(currValueInsert);

							fileWriter.append("/* External ID " + externalId + " ");
							fileWriter.append("\n");
							fileWriter.append(currGroupInstanceInsert);
							fileWriter.append("\n");
							fileWriter.append(currValueInsert);
							fileWriter.append("\n");
							fileWriter.flush();
						} else {
							System.out.println("Missing altitude info");
							continue;
						}

					} else {
						System.out.println("NO Location Found for external Id " + externalId);
					}
				} catch (DSPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ObjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			fileWriter.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
*/