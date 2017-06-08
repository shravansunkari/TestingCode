/*package com.vassarlabs.test.data.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
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
import com.vassarlabs.common.dsp.utils.DSPConstants;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.DateUtils;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.eventmapper.utils.EventConstants;
import com.vassarlabs.iwm.utils.SourceTypeConstants;
import com.vassarlabs.location.pojo.api.ILocation;
import com.vassarlabs.location.service.api.ILocationHierarchyService;

@Component
public class RFDataInsertion {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	@Autowired
	// @Qualifier("StormInitServiceImpl")
	protected ILocationHierarchyService locService;

	
	 * private void generateInsertStatements(String excelFilePath, String
	 * insertOutputFile) {
	 * 
	 * // String excelFilePath = //
	 * "/home/srikanth/Downloads/rf/AWSData(01.08.2016 to 27.09.2016).xlsx";
	 * FileInputStream inputStream; FileWriter fileWriter = null; try {
	 * fileWriter = new FileWriter(insertOutputFile, true); inputStream = new
	 * FileInputStream(new File(excelFilePath)); Workbook workbook = new
	 * XSSFWorkbook(inputStream); Sheet firstSheet = workbook.getSheetAt(0);
	 * Iterator<Row> iterator = firstSheet.iterator();
	 * System.out.println("File Read"); // Skip Header iterator.next(); String
	 * insertStatement = "INSERT into water_quantity_rf" +
	 * "(location_uuid, location_id, source_type, event_gen_ts, event_gen_year, "
	 * +
	 * "start_ts, expiration_ts, event_value_type, level_value_1, location_type_id, "
	 * +
	 * "calendar_year, monsoon_year, insert_ts, deleted, user_session_id)values"
	 * ; fileWriter.append(insertStatement); fileWriter.append("\n"); int i = 0;
	 * System.out.println(insertStatement); while (iterator.hasNext()) { Row row
	 * = iterator.next(); StringBuilder builder = new StringBuilder(); String
	 * locationUUID = row.getCell(6).getStringCellValue(); builder.append("( '"
	 * + locationUUID + "', "); builder = builder.
	 * append("(select location_id from platform_data.location where location_uuid = '"
	 * + locationUUID + "'), "); builder =
	 * builder.append(SourceTypeConstants.RAINFALL + ", "); Date date =
	 * row.getCell(4).getDateCellValue(); long startOfDay = date.getTime(); long
	 * endOfDay = DateUtils.getEndOfDay(startOfDay); long startTs =
	 * DateUtils.getIntervalOfDay(startOfDay, 8, 30, 0); int eventGenYear =
	 * DateUtils.getEventGenYear(startTs); // next day 8:30 long expirationTs =
	 * DateUtils.getIntervalOfDay(endOfDay + 10, 8, 30, 0); builder =
	 * builder.append(startTs + ", "); builder = builder.append(eventGenYear +
	 * ", "); builder = builder.append(startTs + ", "); builder =
	 * builder.append(expirationTs + ", "); builder =
	 * builder.append(EventConstants.COMPUTED_EVENT + ", "); double rain =
	 * row.getCell(5).getNumericCellValue(); builder = builder.append(rain +
	 * ", "); builder = builder.append(7 + ", "); builder =
	 * builder.append("2016, "); builder = builder.append("2016, "); builder =
	 * builder.append("UNIX_TIMESTAMP() * 1000, "); builder =
	 * builder.append("0, "); builder = builder.append("0 )"); if
	 * (iterator.hasNext()) builder = builder.append(", "); else builder =
	 * builder.append(";"); fileWriter.append(builder); fileWriter.append("\n");
	 * fileWriter.flush(); System.out.println(builder); } fileWriter.close(); }
	 * catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * }
	 
	private void generateRFInsertStatements(String awsInputFilePath, String awsOutputFilePath)
			throws DSPException, ObjectNotFoundException {

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
			String insertStatement = "INSERT into water_quantity_rf"
					+ "(location_uuid, location_id, source_type, event_gen_ts, event_gen_year, "
					+ "start_ts, expiration_ts, event_value_type, level_value_1, location_type_id, "
					+ "calendar_year, monsoon_year, custom_string_1, insert_ts, update_ts, deleted, user_session_id)values";
			// fileWriter.append(insertStatement);
			// fileWriter.append("\n");
			int i = 0;
			long currentTs = System.currentTimeMillis();
			System.out.println(insertStatement);
			while (iterator.hasNext()) {

				Row row = iterator.next();
				StringBuilder builder = new StringBuilder();
				builder.append(insertStatement);
				int ext = new Double(row.getCell(0).getNumericCellValue()).intValue();
				String externalId = String.valueOf(ext);
				
				ILocation loc =
				locService.getLocationByExternalId(externalId);
				String locationUUID =loc.getLocationUuid();

				builder.append("('" + locationUUID + "' , ");
				builder = builder.append("(select location_id from platform_data.location where location_uuid = '"
						+ locationUUID + "'), ");
				builder = builder.append(SourceTypeConstants.RAINFALL + ", ");
				Date date = row.getCell(4).getDateCellValue();
				long startOfDay = date.getTime();
				long endOfDay = DateUtils.getEndOfDay(startOfDay);
				long startTs = DateUtils.getIntervalOfDay(startOfDay, 8, 30, 0);
				long eventGenTs = startTs + 60*60*1000;
				int eventGenYear = DateUtils.getEventGenYear(startTs);
				// next day 8:30
				
				long expirationTs = DateUtils.getIntervalOfDay(startTs + DateUtils.TWENTY_FOUR_HOURS_IN_SECONDS, 8, 30,
						0);
				builder = builder.append(eventGenTs + ", ");
				builder = builder.append(eventGenYear + ", ");
				builder = builder.append(startTs + ", ");
				builder = builder.append(expirationTs + ", ");
				builder = builder.append(EventConstants.COMPUTED_EVENT + ", ");
				if (row.getCell(11).getCellType() == Cell.CELL_TYPE_BLANK)
					continue;
				double rain = row.getCell(11).getNumericCellValue();
				builder = builder.append(rain + ", ");
				builder = builder.append(7 + ", ");
				builder = builder.append("2016, ");
				builder = builder.append("2016, ");
				// custom string
				builder = builder.append("'replacement for old data, this data received from AWS file', ");
				builder = builder.append(currentTs + ", ");
				builder = builder.append("UNIX_TIMESTAMP() * 1000, ");
				builder = builder.append("0, ");
				builder = builder.append("0 )");
				
				 * if (iterator.hasNext()) builder = builder.append(", "); else
				 
				builder = builder.append(";");
				fileWriter.append(builder);
				fileWriter.append("\n");
				fileWriter.flush();
				System.out.println(builder);
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

	private void generateAWSInsertStatements(String awsInputFilePath, String awsOutputFilePath) {

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
			String insertStatement = "INSERT INTO aws_data (aws_station_id,location_uuid,location_id,event_gen_year,calendar_year,"
					+ "monsoon_year,event_gen_ts,start_ts,expiration_ts,current_rainfall,avg_temperature,"
					+ "min_temperature,max_temperature,avg_humidity,min_humidity,max_humidity,msl_pressure,"
					+ "wind_direction,insert_ts,update_ts,deleted,user_session_id)";
			// fileWriter.append(insertStatement);
			// fileWriter.append("\n");
			int i = 0;
			long timeStamp =System.currentTimeMillis();
			System.out.println(insertStatement);
			int DATE_COLUMN = 4;
			int RAIN_COLUMN = 11;
			int TEMP_MIN_COLUMN = 7;
			int TEMP_MAX_COLUMN = 8;
			int HUM_MIN_COLUMN = 9;
			int HUM_MAX_COLUMN = 10;
			
			while (iterator.hasNext()) {
				// i++;
				// if(i>4)
				// break;
				Row row = iterator.next();
				StringBuilder builder = new StringBuilder();
				builder.append(insertStatement);
				int ext = new Double(row.getCell(0).getNumericCellValue()).intValue();
				String externalId = String.valueOf(ext);
				ILocation loc;
				try {
					loc = locService.getLocationByExternalId(externalId);
					if (loc != null) {
						String locationUUID = loc.getLocationUuid(); // row.getCell(19).getStringCellValue();//
						// new
						// Double(row.getCell(0).getNumericCellValue()).intValue();
						builder.append(" values('" + externalId + "' , ");
						builder.append(" '" + locationUUID + "' , ");
						builder = builder
								.append("(select location_id from platform_data.location where location_uuid = '"
										+ locationUUID + "'), ");

						Date date = row.getCell(DATE_COLUMN).getDateCellValue();
						long startOfDay = date.getTime();
						long endOfDay = DateUtils.getEndOfDay(startOfDay);
						long startTs = DateUtils.getIntervalOfDay(startOfDay, 8, 30, 0);
						int eventGenYear = DateUtils.getEventGenYear(startTs);
						// next day 8:30
						long expirationTs = DateUtils.getIntervalOfDay(startTs + DateUtils.TWENTY_FOUR_HOURS_IN_SECONDS, 8, 30, 0);
						
						builder = builder.append(eventGenYear + ", ");
						builder = builder.append(2016 + ", ");
						builder = builder.append(2016 + ", ");
						builder = builder.append(startTs + ", ");
						builder = builder.append(startTs + ", ");
						builder = builder.append(expirationTs + ", ");
						if (row.getCell(RAIN_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK) {
							double rain = row.getCell(RAIN_COLUMN).getNumericCellValue();
							builder = builder.append(rain + ", ");
						} else {
							builder = builder.append("null, ");
						}

						double temp;
						if (row.getCell(TEMP_MIN_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK
								&& row.getCell(TEMP_MAX_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK) {
							double minTemp = row.getCell(TEMP_MIN_COLUMN).getNumericCellValue();
							double maxTemp = row.getCell(TEMP_MAX_COLUMN).getNumericCellValue();
							temp = (minTemp + maxTemp) / 2;
							builder = builder.append(temp + ", ");
							builder = builder.append(minTemp + ", ");
							builder = builder.append(maxTemp + ", ");
						} else {
							builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
							builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
							builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
						}
						if (row.getCell(HUM_MIN_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK
								&& row.getCell(HUM_MAX_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK) {
							double minHum = row.getCell(HUM_MIN_COLUMN).getNumericCellValue();
							double maxHum = row.getCell(HUM_MAX_COLUMN).getNumericCellValue();
							double hum = (minHum + maxHum) / 2;
							builder = builder.append(hum + ", ");
							builder = builder.append(minHum + ", ");
							builder = builder.append(maxHum + ", ");
						} else {
							builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
							builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
							builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
						}
						// msl and wind null
						builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
						builder = builder.append(DSPConstants.NO_DATA_ROUNDED+", ");
						builder = builder.append(timeStamp+", ");
						builder = builder.append("UNIX_TIMESTAMP() * 1000, ");
						builder = builder.append("0, ");
						builder = builder.append("0 )");
						
						 * if (iterator.hasNext()) builder =
						 * builder.append(", "); else
						 
						builder = builder.append(";");
						fileWriter.append(builder);
						fileWriter.append("\n");
						fileWriter.flush();
						System.out.println(builder);
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

	
	 * private static void generateUpdateStatements() {
	 * 
	 * String excelFilePath = "/home/srikanth/Downloads/rf/NotPresent.csv.xlsx";
	 * String updateOutputFileName =
	 * "/home/srikanth/Downloads/rf/update_water_quantity_rf.sql";
	 * FileInputStream inputStream; try { inputStream = new FileInputStream(new
	 * File(excelFilePath)); Workbook workbook = new XSSFWorkbook(inputStream);
	 * Sheet firstSheet = workbook.getSheetAt(0); Iterator<Row> iterator =
	 * firstSheet.iterator(); System.out.println("File Read"); // Skip Header
	 * iterator.next();
	 * 
	 * String updateStatement =
	 * "update water_quantity_rf set level_value_1 = rain" +
	 * "update_ts = UNIX_TIMESTAMP() * 1000" +
	 * "where start_ts >= 'start_ts' and expiration_ts <= 'expiration_ts';" ;
	 * FileWriter fileWriter = null; try { fileWriter = new
	 * FileWriter(updateOutputFileName, true); int i = 0; //
	 * System.out.println(updateStatement); while (iterator.hasNext()) { i++; if
	 * (i > 3) break; Row row = iterator.next(); StringBuilder builder = new
	 * StringBuilder(); builder =
	 * builder.append("update water_quantity_rf set level_value_1 = "); builder
	 * = builder.append(row.getCell(24).getNumericCellValue());// RF // VALUE
	 * Date date = row.getCell(6).getDateCellValue(); long startOfDay =
	 * date.getTime(); long endOfDay = DateUtils.getEndOfDay(startOfDay); long
	 * startTs = DateUtils.getIntervalOfDay(startOfDay, 8, 30, 0); // next day
	 * 8:30 long expirationTs = DateUtils.getIntervalOfDay(endOfDay + 10, 8, 30,
	 * 0); builder = builder.append(", update_ts = UNIX_TIMESTAMP() * 1000");
	 * builder = builder.append("where start_ts >= " + startTs);// start_ts
	 * builder = builder.append("and expiration_ts <= " + expirationTs + ";");
	 * System.out.println(builder); fileWriter.append(builder.toString());
	 * fileWriter.flush(); fileWriter.close();
	 * 
	 * } } catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * } catch (FileNotFoundException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } catch (InterruptedException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 
	void test() {
		try {
			applicationInitService.initialize();
		} catch (AppInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, DSPException, ObjectNotFoundException {
		// String excelFilePath = "/home/srikanth/IWMCode/RF_AWS_DATA/AWS Rf
		// Insert new.xlsx";
		// String outputFileName =
		// "/home/srikanth/IWMCode/RF_AWS_DATA/AWS_IWM_RF_NEW_INSERT.sql";
		// generateInsertStatements(excelFilePath, outputFileName);
		System.out.println("Start of IWM TEST");
		RFDataInsertion rfDataInsertion = AppContext.getApplicationContext().getBean(RFDataInsertion.class);
		rfDataInsertion.test();

		//String awsInputFilePath = "/home/srikanth/vassarlabs/AWSData(28.09.2016 to 06.10.2016).xlsx";
		//String awsOutputFilePath = "/home/srikanth/vassarlabs/RF_28.09.2016_to_06.10.2016.sql";
		//rfDataInsertion.generateRFInsertStatements(awsInputFilePath, awsOutputFilePath);
		//String awsInputFilePath2 = "/home/srikanth/IWMCode/AWS Data/AWSData(01.08.2016 to 27.09.2016).xlsx";
		//String awsOutputFilePath2 = "/home/srikanth/IWMCode/AWS Data/RF_01.08.2016_to_27.09.2016_final.sql";
		//rfDataInsertion.generateRFInsertStatements(awsInputFilePath2, awsOutputFilePath2);

		String awsInputFilePath = "/home/srikanth/vassarlabs/AWSData(07.10.2016 to 15.10.2016).xlsx";
		String awsOutputFilePath = "/home/srikanth/vassarlabs/AWS_data_07.10.2016_15.10.2016.sql";
		String awsInputFilePath2 = "/home/srikanth/vassarlabs/AWSData(28.09.2016 to 06.10.2016).xlsx";
		String awsOutputFilePath2 = "/home/srikanth/vassarlabs/AWS_data_28.09.2016_06.10.2016.sql";
		
		
		rfDataInsertion.generateAWSInsertStatements(awsInputFilePath, awsOutputFilePath);
		rfDataInsertion.generateAWSInsertStatements(awsInputFilePath2, awsOutputFilePath2);
		
		// generateUpdateStatements();
	}

}*/