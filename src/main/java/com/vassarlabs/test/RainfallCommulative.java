package com.vassarlabs.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.dsp.utils.DSPConstants;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.DateUtils;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.eventmapper.utils.EventConstants;
import com.vassarlabs.iwm.dss.wbs.rf.dsp.api.IRFWBSDSP;
import com.vassarlabs.iwm.service.api.IIWMService;
import com.vassarlabs.iwm.soilmoisture.stress.service.api.ISoilMoistureNRSCDataService;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vassarlabs.location.utils.LocationConstants;
import com.vassarlabs.spatial.ext.service.api.IGPSExtensionService;
import com.vividsolutions.jts.geom.Geometry;

@Component
public class RainfallCommulative {

	public static BufferedWriter bw = null;
	public static FileWriter fw = null;
	public static BufferedWriter bwLog = null;
	public static FileWriter fwLog = null;

	@Autowired
	IIWMService iwmService;

	@Autowired
	ISoilMoistureNRSCDataService nrscDataService;

	@Autowired
	protected IRFWBSDSP rainfallIWMDSP;

	@Autowired
	ILocationHierarchyService locationHierarchyService;

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	@Autowired
	ILocationHierarchyService locService;

	@Autowired
	protected IGPSExtensionService gpsExtensionService;

	public static void main(String[] args) throws AppInitializationException, DSPException,
			com.vividsolutions.jts.io.ParseException, ObjectNotFoundException {

		int[] dates = new int[] { 20170201, 20170205, 20170209, 20170213, 20170217, 20170221, 20170225, 20170301,
				20170305, 20170309, 20170313, 20170317, 20170321, 20170325, 20170329, 20170402, 20170406, 20170410,
				20170414, 20170418 };

		RainfallCommulative task = AppContext.getApplicationContext().getBean(RainfallCommulative.class);
		// task.getRFValue(dates);
		task.checkSubBasin("");
	}

	public void getRFValue(int[] dates)
			throws AppInitializationException, DSPException, com.vividsolutions.jts.io.ParseException {
		applicationInitService.initialize();
		Map<String, List<String>> stateToRainfallStationUUID = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.STATE, LocationConstants.RAINFALL);
		Map<String, Geometry> locUUIDtoGeometry = getGPSMappingForLocUUIDs();
		List<Integer> locIDList = new ArrayList<>();

		for (String state : stateToRainfallStationUUID.keySet()) {

			List<String> stationUUIDs = stateToRainfallStationUUID.get(state);
			for (String stationUUID : stationUUIDs) {
				locIDList.add(locationHierarchyService.getLocIdForLocUUID(stationUUID));
			}
		}

		for (String state : stateToRainfallStationUUID.keySet()) {

			List<String> stationUUIDs = stateToRainfallStationUUID.get(state);
			for (int i = 0; i < dates.length; i++) {

				try {
					fw = new FileWriter("/home/srikanth/Desktop/DATA/" + dates[i]);
					bw = new BufferedWriter(fw);

					fwLog = new FileWriter("/home/srikanth/Desktop/Log.txt");
					bwLog = new BufferedWriter(fwLog);

				} catch (IOException e) {
					e.printStackTrace();
				}
				long startTs = DateUtils.getModelDateInMillis(dates[i] - 3);
				// startTs =
				// DateUtils.getModelDateInMillis(DateUtils.getStartOfMonsoon(dates[i]-3));
				long endTs = DateUtils.getEndOfDay(DateUtils.getModelDateInMillis(dates[i]));

				Map<Integer, Double> rfData = rainfallIWMDSP.getRFData(locIDList, EventConstants.COMPUTED_EVENT,
						startTs, endTs);

				try {
					fw.write("LAT, LONG, VALUE\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (String stationUUID : stationUUIDs) {

					Geometry obj = locUUIDtoGeometry.get(stationUUID);

					double actualRainfallValue = 0;
					try {
						actualRainfallValue = rfData.get(locationHierarchyService.getLocIdForLocUUID(stationUUID));
					} catch (Exception e) {

					}
					try {
						fw.write(obj.getCoordinate().x + ", " + obj.getCoordinate().y + ", " + actualRainfallValue
								+ "\n");
						System.out.println(stationUUID + ", " + obj.getCoordinate().x + ", " + obj.getCoordinate().y
								+ ", " + actualRainfallValue);
						fw.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (Exception e) {
						System.out.println(e.getMessage());
						try {
							fwLog.write("StationUUID = " + stationUUID + "Date = " + dates[i]);
							fwLog.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
				try {
					fw.flush();
					bw.close();
					fw.close();
					fwLog.flush();
					bwLog.close();
					fwLog.close();
				} catch (Exception e) {
				}

			}
		}
	}

	public double getRFDataFromDB(int date, String locationUUID) {
		/*
		 * int calendar_month = date / 100; double result = 0; Connection con =
		 * null; try { Class.forName("com.mysql.jdbc.Driver"); con =
		 * DriverManager.getConnection("jdbc:mysql://localhost/platform_data",
		 * "root", "root");
		 * 
		 * Statement stmt = con.createStatement(); ResultSet rs =
		 * stmt.executeQuery(
		 * "select daily_data_values_1 from water_quantity_aggr_rf where location_uuid='"
		 * + locationUUID + "' and calendar_year_month =" + calendar_month); if
		 * (rs.next()) { String[] data = rs.getString(1).split(",");
		 * 
		 * } } catch (Exception e) { System.out.println("error = " +
		 * e.getMessage()); }
		 */
		return 0;
	}

	protected Map<String, Geometry> getGPSMappingForLocUUIDs()
			throws DSPException, com.vividsolutions.jts.io.ParseException {

		try {
			applicationInitService.initialize();
		} catch (AppInitializationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Map<String, List<String>> allSMLocMap = locService.getAllLocForParentChildTypes(LocationConstants.STATE,
				LocationConstants.RAINFALL);
		List<String> allSMLocUUIDList = allSMLocMap.get(
				locService.getLocUUIdForLocID(1)); /** For Andhra Pradesh **/

		Map<Integer, String> entityLocationMap = null;

		try {
			entityLocationMap = locService.getEntityIdsForLocations(allSMLocUUIDList);

		} catch (DSPException | ObjectNotFoundException e) {
			e.printStackTrace();
		}

		List<Integer> entityList = new ArrayList<>();
		entityList.addAll(entityLocationMap.keySet());
		Map<Integer, Geometry> entityGPSMap = null;

		try {
			entityGPSMap = gpsExtensionService.getGPSData(entityList);
		} catch (ObjectNotFoundException | ParseException | DSPException e) {
			e.printStackTrace();
		}

		Map<String, Geometry> locationGPSMap = new HashMap<>();

		for (Integer entityId : entityList) {
			String locationUUID = entityLocationMap.get(entityId);
			Geometry gps = entityGPSMap.get(entityId);
			if (gps == null) {
				System.out.println("ZBXXX : No GPS For entity = " + entityId);
				continue;
			}
			locationGPSMap.put(locationUUID, gps);
		}

		Map<String, Geometry> locUUIDToGPSMap = new HashMap<>();

		for (String locUUID : allSMLocUUIDList) {
			Geometry gps = locationGPSMap.get(locUUID);
			if (gps != null)
				locUUIDToGPSMap.put(locUUID, gps);
		}
		return locUUIDToGPSMap;
	}

	public void checkSubBasin(String subBasinName) throws DSPException, ObjectNotFoundException {
		try {
			applicationInitService.initialize();
		} catch (AppInitializationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fw = new FileWriter("/home/srikanth/Desktop/data.csv");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] dates = new int[] { 20170401, 20170402, 20170403, 20170404, 20170405, 20170406, 20170407, 20170408,
				20170409, 201704010, 201704011, 201704012, 201704013, 201704014, 201704015, 201704016, 201704017,
				201704018, 201704019, };
		subBasinName = "TADAKALERU-SB";
		try {
			fw.write(subBasinName + ",day1,day2,day3,day4,day5,day6,day7,day8,day9,day10,"
					+ "day11,day12,day13,day14,day15,day16,day17,day18,day19\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, List<String>> subBasinToMicroBasinMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.SUBBASIN, LocationConstants.MICROBASIN);

		for (String subBasinUUID : subBasinToMicroBasinMap.keySet()) {
			String subBasinName1 = locationHierarchyService.getLocationNameFromUUID(subBasinUUID);
			if (subBasinName.equals(subBasinName1)) {
				List<String> microBasinUnderSubBasin = subBasinToMicroBasinMap.get(subBasinUUID);
				for (int i = 0; i < dates.length; i++) {

					Map<String, Map<Integer, Double>> microBasinTorunOffDataMap = nrscDataService
							.getMicroBasinLevelRunOffData(dates[i], dates[i], null);
					double runOff = 0;
					for (String microBasin : microBasinUnderSubBasin) {
						Map<Integer, Double> microBasinRunOffData = microBasinTorunOffDataMap.get(microBasin);

						if (microBasinRunOffData.get(dates[i]) > 0
								&& microBasinRunOffData.get(dates[i]) != DSPConstants.NO_DATA) {
							runOff += microBasinRunOffData.get(dates[i]);
						}

					}
					if (runOff > 0) {
						try {
							fw.write(runOff + ",");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							fw.write(DSPConstants.NO_DATA + ",");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				// microbasin,date,runoff

			}
		}
		try {
			fw.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw.flush();
			bw.close();
			fw.close();
		} catch (Exception e) {
		}

	}
}
