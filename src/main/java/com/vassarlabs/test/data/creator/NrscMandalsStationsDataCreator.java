/*package com.vassarlabs.test.data.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
public class NrscMandalsStationsDataCreator {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	@Autowired
	protected ILocationHierarchyService locService;
	
	void test() {
		try {
			applicationInitService.initialize();
		} catch (AppInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateSQLStatements(String filePath, String outputFile){
		FileInputStream inputStream;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputFile, true);
			inputStream = new FileInputStream(new File(filePath));
			Workbook workbook = new HSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			System.out.println("File Read");
			// Skip Header
			iterator.next();
			String insertStatement = "INSERT INTO platform_data.grid_entity_map("
					+ "grid_latitude, "
					+ "grid_longitude, "
					+ "grid_source, "
					+ "grid_size, "
					+ "location_intersection, "
					+ "rf_station_association, "
					+ "aws_station_association, "
					+ "gw_station_association, "
					+ "grid_to_grid_association, "
					+ "insert_ts, "
					+ "deleted, "
					+ "user_session_id)";
			
			
			// example
			
			
			
				select concat_ws( ",", concat_ws("#", (select location_id
				 from platform_data.location where external_id like '0137'),'3.48762954170112'),
				 concat_ws("#", (select location_id
				 from platform_data.location where external_id like '0136'),'5.48891292958226'));
			 
			String locInsStart = " ( select concat_ws( ',',  ";
			String concatLocStart = "concat_ws('##', (select location_id"
					+ " from platform_data.location where location_uuid = ";
			String concatLocMid = "),";
			String concatLocEnd = ")";
			String locInsEnd = "))";
			
			
			
			
			
			String groupSelectStart = "(select GROUP_CONCAT(location_id separator ',') "
					+ "from platform_data.location "
					+ "where location_uuid in (";
			
			String groupSelectMid = ") ORDER BY FIELD(location_uuid,";
			String groupSelectEnd = "))";
			
			
			int LAT_COLUMN = 1;
			int LONG_COLUMN = 2;
			int MANDAL_INTERSECTION_COLUMN = 3;
			int RF_STATIONS_COLUMN = 4;
			int AWS_STATIONS_COLUMN = 5;
			int GW_STATIONS_COLUMN = 6;
			int GRIDS_COLUMN = 7;
			int i = 0;
			while (iterator.hasNext()) {
				i++;
				if (i > 2)
					break;
				Row row = iterator.next();
				StringBuilder builder = new StringBuilder();
				builder.append(insertStatement);
				
				builder.append(" VALUES ( ");
				// if lat and long are valid then only create else continue
				if(row.getCell(LAT_COLUMN).getCellType() == Cell.CELL_TYPE_BLANK || 
						row.getCell(LONG_COLUMN).getCellType() == Cell.CELL_TYPE_BLANK )
					continue;
					
					
				double latitude = row.getCell(LAT_COLUMN).getNumericCellValue(); 
				double longitude = row.getCell(LONG_COLUMN).getNumericCellValue();
				// add latitude and longitude
				builder.append(latitude);
				builder.append(",");
				builder.append(longitude);
				builder.append(",");
				
				
				// grid source and size
				builder.append("'NRSC'");
				builder.append(",");
				builder.append("0.050");
				builder.append(",");
				
				
				System.out.println("lat "+latitude+" long "+longitude);
				String mandalInsString = new String();
				StringBuffer finalLocItersectIns = new StringBuffer();
				if (row.getCell(MANDAL_INTERSECTION_COLUMN)!= null && row.getCell(MANDAL_INTERSECTION_COLUMN).getCellType() != Cell.CELL_TYPE_BLANK ){
					mandalInsString = row.getCell(MANDAL_INTERSECTION_COLUMN).getStringCellValue();

					//StringTokenizer mandalTemp = new StringTokenizer(mandalInsString, "$");
					
					String mandalsArray[] = mandalInsString.trim().split(" \\$ ");
					
					StringBuffer locationInsertionTemp = new StringBuffer();
					// open single tag for string
					//locationInsertionTemp.append("");
					
					for (int j = 0; j < mandalsArray.length; j++) {
						String mandalInfo[] = mandalsArray[j].trim().split("\\|");
						System.out.println("mandals Info "+mandalsArray[j]);
						System.out.println("Mandal External ID "+mandalInfo[0]);
						try {
							ILocation loc = locService.getLocationByExternalId(mandalInfo[0]);
							
							System.out.println("Mandal UUID  "+loc.getLocationUuid());
							System.out.println("area of intersection  "+mandalInfo[1]);
							//locationInsertionTemp.append(loc.getLocationUuid()+IWMConstants.DOUBLE_HASH_DELIMITER+mandalInfo[1]);
							
							
							locationInsertionTemp.append(concatLocStart);
							locationInsertionTemp.append("'" + loc.getLocationUuid() +"'");
							locationInsertionTemp.append(concatLocMid);
							locationInsertionTemp.append("'" + mandalInfo[1] + "'");
							locationInsertionTemp.append(concatLocEnd);
							if(j != mandalsArray.length -1)
								locationInsertionTemp.append(",");
							
						} catch (DSPException e) {
							e.printStackTrace();
							continue;
						} catch (ObjectNotFoundException e) {
							e.printStackTrace();
							continue;
						}
					}
					// close single tag for string
					//locationInsertionTemp.append("'");
					
					finalLocItersectIns.append(locInsStart);
					finalLocItersectIns.append(locationInsertionTemp);
					finalLocItersectIns.append(locInsEnd);
				} else {
					finalLocItersectIns.append("NULL");
				}
				
				System.out.println(finalLocItersectIns);
				// append location_intersection
				builder.append(finalLocItersectIns);
				builder.append(",");
				
				
				{

					StringBuffer rfTemp = new StringBuffer();
					// Rf stations
					String rfStationsStr = row.getCell(RF_STATIONS_COLUMN).getStringCellValue();
					String rfStationsArray[] = rfStationsStr.trim().split(",");
					for (int j = 0; j < rfStationsArray.length; j++) {
						System.out.println("Rf station id "+rfStationsArray[j]);
						rfTemp.append("'"+rfStationsArray[j].trim()+"'");
						if(j != rfStationsArray.length -1)
							rfTemp.append(",");
					}
					StringBuffer rf_string = new StringBuffer();
					rf_string.append(groupSelectStart);
					rf_string.append(rfTemp);
					rf_string.append(groupSelectMid);
					rf_string.append(rfTemp);
					rf_string.append(groupSelectEnd);

					//System.out.println(rf_string);
					builder.append(rf_string);
					builder.append(",");
				}
				// AWS Stations
				{
					StringBuffer awsTemp = new StringBuffer();
					String awsStationsStr = row.getCell(AWS_STATIONS_COLUMN).getStringCellValue();
					String awsStationsArray[] = awsStationsStr.trim().split(",");
					for (int j = 0; j < awsStationsArray.length; j++) {
						System.out.println("AWS station id "+awsStationsArray[j]);
						awsTemp.append("'"+awsStationsArray[j].trim()+"'");
						if(j != awsStationsArray.length -1)
							awsTemp.append(",");
					}
					StringBuffer aws_string = new StringBuffer();
					aws_string.append(groupSelectStart);
					aws_string.append(awsTemp);
					aws_string.append(groupSelectMid);
					aws_string.append(awsTemp);
					aws_string.append(groupSelectEnd);

					//System.out.println(aws_string);
					builder.append(aws_string);
					builder.append(",");
				}
				// GW Stations
				{
					StringBuffer gwTemp = new StringBuffer();
					String gwStationsStr = row.getCell(GW_STATIONS_COLUMN).getStringCellValue();
					String gwStationsArray[] = gwStationsStr.trim().split(",");
					for (int j = 0; j < gwStationsArray.length; j++) {
						System.out.println("GW station id "+gwStationsArray[j]);
						gwTemp.append("'"+gwStationsArray[j].trim()+"'");
						if(j != gwStationsArray.length -1)
							gwTemp.append(",");
					}
					StringBuffer gw_string = new StringBuffer();
					gw_string.append(groupSelectStart);
					gw_string.append(gwTemp);
					gw_string.append(groupSelectMid);
					gw_string.append(gwTemp);
					gw_string.append(groupSelectEnd);

					//System.out.println(gw_string);
					builder.append(gw_string);
					builder.append(",");
					
				}
				// grid_to_grid_association
				// grid_id#lat#long#area
				// old = 1051#19.12500381#83.625#22.2041307105053
				// new = ISRO##19.12500381##83.625##22.2041307105053

				{
					StringBuffer gridAssoc = new StringBuffer();
					gridAssoc.append("'");
					String excelGridAssocStr = row.getCell(GRIDS_COLUMN).getStringCellValue();
					String isroArray[] = excelGridAssocStr.trim().split(",");
					for (int j = 0; j < isroArray.length; j++) {
						String isroGridInfo[] = isroArray[j].split("##");
						gridAssoc.append("ISRO");
						gridAssoc.append("##");
						gridAssoc.append("0.125");
						gridAssoc.append("##");
						gridAssoc.append(isroGridInfo[1]);
						gridAssoc.append("##");
						gridAssoc.append(isroGridInfo[2]);
						gridAssoc.append("##");
						gridAssoc.append(isroGridInfo[3]);
						
						
						if(j != isroArray.length -1)
							gridAssoc.append(",");
					}
					
					gridAssoc.append("'");
					builder.append(gridAssoc);
				}
				//insert_ts, deleted, user_session_id
				//  03/01/2017, 17:30:00
				builder.append(", ");
				builder.append("1483444800000, ");
				builder.append("0, ");
				builder.append("0 ");
				builder.append(");");
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
	public static void main(String[] args) throws InterruptedException, DSPException, ObjectNotFoundException {
		System.out.println("Start of IWM TEST");
		NrscMandalsStationsDataCreator rfDataInsertion = AppContext.getApplicationContext().getBean(NrscMandalsStationsDataCreator.class);
		rfDataInsertion.test();

		String awsInputFilePath = "/home/srikanth/workFiles/28-12-2016/final data/nrsc_custom_all_mapped.xls";
		String awsOutputFilePath = "/home/srikanth/workFiles/28-12-2016/final data/nrsc_custom_all_mapped.sql";
		rfDataInsertion.generateSQLStatements(awsInputFilePath, awsOutputFilePath);
	}
}
*/