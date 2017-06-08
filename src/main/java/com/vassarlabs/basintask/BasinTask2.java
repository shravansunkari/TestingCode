/*package com.vassarlabs.basintask;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vassarlabs.location.utils.LocationConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BasinTask2 {
	public static BufferedWriter bw = null;
	public static FileWriter fw = null;
	public Connection con = null;
	
	@Autowired
	protected ILocationHierarchyService locationHierarchyService;
	
	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	
	public void start() throws IOException, AppInitializationException, DSPException {
		applicationInitService.initialize();
		try{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/platform_data", "root", "root");
		}
		catch(Exception e){
			System.out.println("error = "+e.getMessage());
		}

		try {
			fw = new FileWriter("/home/srikanth/Desktop/MicroBasinLevel.java");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}
		fw.write("static {\n\n");
		fw.write("Map<String, ILocationStructureInfo> locToMasterDataMap = null;\n\n");

		File folder = new File("/home/srikanth/Desktop/csv_files");
		File[] listOfFiles = folder.listFiles();
		
		//Services
		 // Mapping from stateUUID to all BasinUUIDs
		Map<String, List<String>> stateToBasinUUIDMap = locationHierarchyService.getAllLocForParentChildTypes(LocationConstants.STATE, LocationConstants.BASIN); 
		// Mapping from BasinUUID to subBasinUUIDs
		Map<String, List<String>> basinToSubBasinUUIDMap = locationHierarchyService.getAllLocForParentChildTypes(LocationConstants.BASIN, LocationConstants.SUBBASIN); 
		// Mapping from subbasin to microbasin
		Map<String, List<String>> subBasinToMicroBasinUUIDMap = locationHierarchyService.getAllLocForParentChildTypes(LocationConstants.SUBBASIN, LocationConstants.MICROBASIN);

		folder = new File("/home/srikanth/Desktop/counts2");
		listOfFiles = folder.listFiles();

		Map<Integer, basin_class> total_map = new HashMap<>();

		// Checkdam
		String csvFile = "/home/srikanth/Desktop/counts2/CHECK_DAM.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				// Write here
				// Read from excel files
				try {
					int micro_basin_code1 = Integer.parseInt(row[0]);
					int micro_basin_count1 = Integer.parseInt(row[1]);
					double micro_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(micro_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(micro_basin_code1);
					}
					
					String MicroBasinName = getIWMLocationNameForMicroBasin(micro_basin_code1, con);
					String SubBasinName = getUUIDForSubBasinFromMicroBasinCode(micro_basin_code1, con, subBasinToMicroBasinUUIDMap);  
					String BasinName = getUUIDForBasinFromSubBasinCode(SubBasinName, basinToSubBasinUUIDMap);
					String name = "Andhra Pradesh" + "##" 
							+ BasinName + "##" 
							+ SubBasinName + "##"
							+ MicroBasinName;
					
					basinData.setLocName(name);
					basinData.set_check_dam_count(micro_basin_count1);
					basinData.set_check_dam_capacity(micro_basin_capacity1);
					total_map.put(micro_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in1 " + line );
				}
			}
			try{
				br.close();
			}
			catch(Exception e){
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Farm_pond
		csvFile = "/home/srikanth/Desktop/counts2/FARM_PONDS.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				// Write here
				// Read from excel files
				try {
					int micro_basin_code1 = Integer.parseInt(row[0]);
					int micro_basin_count1 = Integer.parseInt(row[1]);
					double micro_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(micro_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(micro_basin_code1);
					}
					 
					String MicroBasinName = getIWMLocationNameForMicroBasin(micro_basin_code1, con);
					String SubBasinName = getUUIDForSubBasinFromMicroBasinCode(micro_basin_code1, con, subBasinToMicroBasinUUIDMap);  
					String BasinName = getUUIDForBasinFromSubBasinCode(SubBasinName, basinToSubBasinUUIDMap);
					String name = "Andhra Pradesh" + "##" 
							+ BasinName + "##" 
							+ SubBasinName + "##"
							+ MicroBasinName;
					
					basinData.setLocName(name);
					basinData.set_farm_pond_count(micro_basin_count1);
					basinData.set_farm_pond_capacity(micro_basin_capacity1);
					total_map.put(micro_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in2 " + line);
				}
			}
			try{
				br.close();
			}
			catch(Exception e){
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 3
		csvFile = "/home/srikanth/Desktop/counts2/MI_TANK.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				// Write here
				// Read from excel files
				try {
					int micro_basin_code1 = Integer.parseInt(row[0]);
					int micro_basin_count1 = Integer.parseInt(row[1]);
					double micro_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(micro_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(micro_basin_code1);
					}
				
					String MicroBasinName = getIWMLocationNameForMicroBasin(micro_basin_code1, con);
					String SubBasinName = getUUIDForSubBasinFromMicroBasinCode(micro_basin_code1, con, subBasinToMicroBasinUUIDMap);  
					String BasinName = getUUIDForBasinFromSubBasinCode(SubBasinName, basinToSubBasinUUIDMap);
					String name = "Andhra Pradesh" + "##" 
							+ BasinName + "##" 
							+ SubBasinName + "##"
							+ MicroBasinName;
					
					basinData.setLocName(name);
					basinData.set_mi_tank_count(micro_basin_count1);
					basinData.set_mi_tank_capacity(micro_basin_capacity1);
					total_map.put(micro_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in3 " + line + e.getMessage());
				}
			}
			try{
				br.close();
			}
			catch(Exception e){
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 4
		csvFile = "/home/srikanth/Desktop/counts2/OTHERS.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				// Write here
				// Read from excel files
				try {
					int micro_basin_code1 = Integer.parseInt(row[0]);
					int micro_basin_count1 = Integer.parseInt(row[1]);
					double micro_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(micro_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(micro_basin_code1);
					}
					
					String MicroBasinName = getIWMLocationNameForMicroBasin(micro_basin_code1, con);
					String SubBasinName = getUUIDForSubBasinFromMicroBasinCode(micro_basin_code1, con, subBasinToMicroBasinUUIDMap);  
					String BasinName = getUUIDForBasinFromSubBasinCode(SubBasinName, basinToSubBasinUUIDMap);
					String name = "Andhra Pradesh" + "##" 
							+ BasinName + "##" 
							+ SubBasinName + "##"
							+ MicroBasinName;
					
					basinData.setLocName(name);
					basinData.set_others_count(micro_basin_count1);
					basinData.set_others_capacity(micro_basin_capacity1);
					total_map.put(micro_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in4 " + line);
				}
			}
			try{
				br.close();
			}
			catch(Exception e){
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 5
		csvFile = "/home/srikanth/Desktop/counts2/PERCULATION_TANK.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				// Write here
				// Read from excel files
				try {
					int micro_basin_code1 = Integer.parseInt(row[0]);
					int micro_basin_count1 = Integer.parseInt(row[1]);
					double micro_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(micro_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(micro_basin_code1);
					}
					
					String MicroBasinName = getIWMLocationNameForMicroBasin(micro_basin_code1, con);
					String SubBasinName = getUUIDForSubBasinFromMicroBasinCode(micro_basin_code1, con, subBasinToMicroBasinUUIDMap);  
					String BasinName = getUUIDForBasinFromSubBasinCode(SubBasinName, basinToSubBasinUUIDMap);
					String name = "Andhra Pradesh" + "##" 
							+ BasinName + "##" 
							+ SubBasinName + "##"
							+ MicroBasinName;
					basinData.setLocName(name);
					basinData.set_perculation_tank_count(micro_basin_count1);
					basinData.set_perculation_tank_capacity(micro_basin_capacity1);
					total_map.put(micro_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in5 " + line);
				}
			}
			try{
				br.close();
			}
			catch(Exception e){
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		for (int code : total_map.keySet()) {

			int total_count =0;
			double total_capacity = 0.0;
			
			basin_class obj = total_map.get(code);
			
			total_count += obj.get_check_dam_count();
			total_count += obj.get_farm_pond_count();
			total_count += obj.get_mi_tank_count();
			total_count += obj.get_perculation_tank_count();
			total_count += obj.get_others_count();
			
			total_capacity += obj.get_check_dam_capacity();
			total_capacity += obj.get_farm_pond_capacity();
			total_capacity += obj.get_mi_tank_capacity();
			total_capacity += obj.get_perculation_tank_capacity();
			total_capacity += obj.get_others_capacity();
			
			fw.write("structTypeToMasterDataMap = new HashMap<>();\n");
			fw.write("locToMasterDataMap.put(\"" + obj.getLocName() + "\", structTypeToMasterDataMap);\n");
			fw.write("structTypeToMasterDataMap.put(CHECK_DAM, new LocationStructureInfo(" + obj.get_check_dam_count()
					+ ", " + obj.get_check_dam_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(FARM_POND, new LocationStructureInfo(" + obj.get_farm_pond_count()
					+ ", " + obj.get_farm_pond_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(PERCULATION_TANK, new LocationStructureInfo("
					+ obj.get_perculation_tank_count() + ", " + obj.get_perculation_tank_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(MI_TANK, new LocationStructureInfo(" + obj.get_mi_tank_count()
					+ ", " + obj.get_mi_tank_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(CHECK_DAM_PROPOSED, new LocationStructureInfo(" + obj.get_check_dam_proposed_count()
			+ ", " + obj.get_check_dam_proposed_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(OTHER_STRUCTURES, new LocationStructureInfo(" + obj.get_others_count() + ", "
					+ obj.get_others_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(TOTAL_STRUCTURES, new LocationStructureInfo(" + total_count + ", "
					+ total_capacity + "));\n\n");

		}
		
		fw.write("\n}");
		
		try {
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			con.close();
		}
		catch(Exception e){}
	}

	public class basin_class {
		String loc_name = "";
		int check_dam_count = 0;
		double check_dam_capacity = 0.0;
		int check_dam_proposed_count = 0;
		double check_dam_proposed_capacity = 0.0;
		int farm_pond_count = 0;
		double farm_pond_capacity = 0.0;
		int mi_tank_count = 0;
		double mi_tank_capacity = 0.0;
		int perculation_tank_count = 0;
		double perculation_tank_capacity = 0.0;
		int others_count = 0;
		double others_capacity = 0.0;

		public void setLocName(String name) {
			this.loc_name = name;
		}

		public String getLocName() {
			return loc_name;
		}

		public void set_check_dam_count(int count) {
			this.check_dam_count += count;
		}

		public void set_check_dam_capacity(double capacity) {
			this.check_dam_capacity += capacity;
		}

		public int get_check_dam_count() {
			return check_dam_count;
		}

		public double get_check_dam_capacity() {
			return check_dam_capacity;
		}
		
		public void set_check_dam_proposed_count(int count) {
			this.check_dam_proposed_count += count;
		}

		public void set_check_dam_proposed_capacity(double capacity) {
			this.check_dam_proposed_capacity += capacity;
		}

		public int get_check_dam_proposed_count() {
			return check_dam_proposed_count;
		}

		public double get_check_dam_proposed_capacity() {
			return check_dam_proposed_capacity;
		}

		public void set_farm_pond_count(int count) {
			this.farm_pond_count += count;
		}

		public void set_farm_pond_capacity(double capacity) {
			this.farm_pond_capacity += capacity;
		}

		public int get_farm_pond_count() {
			return farm_pond_count;
		}

		public double get_farm_pond_capacity() {
			return farm_pond_capacity;
		}

		public void set_perculation_tank_count(int count) {
			this.perculation_tank_count += count;
		}

		public void set_perculation_tank_capacity(double capacity) {
			this.perculation_tank_capacity += capacity;
		}

		public int get_perculation_tank_count() {
			return perculation_tank_count;
		}

		public double get_perculation_tank_capacity() {
			return perculation_tank_capacity;
		}

		public void set_mi_tank_count(int count) {
			this.mi_tank_count += count;
		}

		public void set_mi_tank_capacity(double capacity) {
			this.mi_tank_capacity += capacity;
		}

		public int get_mi_tank_count() {
			return mi_tank_count;
		}

		public double get_mi_tank_capacity() {
			return mi_tank_capacity;
		}

		public void set_others_count(int count) {
			this.others_count += count;
		}

		public void set_others_capacity(double capacity) {
			this.others_capacity += capacity;
		}

		public int get_others_count() {
			return others_count;
		}

		public double get_others_capacity() {
			return others_capacity;
		}
	}
	
	public static String getIWMLocationNameForBasin(int code, Connection con){
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/platform_data", "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select display_name from location where external_id = '" + Integer.toString(code) + "-B';");
			if (rs.next()) {
				String name = rs.getString(1);
				System.out.println("Found name = " + rs.getString(1));
				//con.close();
				return name;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\t" + code + "-B");
		}
		return "";
	}
	
	public static String getIWMLocationNameForSubBasin(int code, Connection con){
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/platform_data", "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select display_name from location where external_id = '" + Integer.toString(code) + "-SB';");
			if (rs.next()) {
				String name = rs.getString(1);
				System.out.println("Found name = " + rs.getString(1) + "-SB");
				//con.close();
				return name;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\t" + code);
		}
		return "";
	}
	
	public static String getIWMLocationNameForMicroBasin(int code, Connection con){
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select display_name from location where external_id = '" + Integer.toString(code) + "-MB';");
			if (rs.next()) {
				String name = rs.getString(1);
				System.out.println("Found name = " + rs.getString(1));
				//con.close();
				return name;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\t" + code + "-MB");
		}
		return "";
	}
	
	public String getBasinName(int sub_basin_code, Connection con, Map<String, List<String>> basinToSubBasinUUIDMap) throws DSPException{
		String name = getIWMLocationNameForSubBasin(sub_basin_code, con);
		
		for(String str: basinToSubBasinUUIDMap.keySet()){
			List<String> uuids = basinToSubBasinUUIDMap.get(str);
			
			for(String s : uuids){
				if(name.equalsIgnoreCase(locationHierarchyService.getLocNameForLocUUID(s))) {
					return locationHierarchyService.getLocNameForLocUUID(str);
				}
			}
		}
		return "";
	}
	
	public String getUUIDForSubBasinFromMicroBasinCode(int micro_basin_code, Connection con, Map<String, List<String>> subBasinToMicroBasinUUIDMap) throws DSPException{
		String name = getIWMLocationNameForMicroBasin(micro_basin_code, con);
		
		for(String str: subBasinToMicroBasinUUIDMap.keySet()){
			List<String> uuids = subBasinToMicroBasinUUIDMap.get(str);
			
			for(String s : uuids){
				if(name.equalsIgnoreCase(locationHierarchyService.getLocNameForLocUUID(s))) {
					return locationHierarchyService.getLocNameForLocUUID(str);
				}
			}
		}
		return "";
	}
	
	public String getUUIDForBasinFromSubBasinCode(String name, Map<String, List<String>> basinToSubBasinUUIDMap) throws DSPException{
		
		for(String str: basinToSubBasinUUIDMap.keySet()){
			List<String> uuids = basinToSubBasinUUIDMap.get(str);
			
			for(String s : uuids){
				if(name.equalsIgnoreCase(locationHierarchyService.getLocNameForLocUUID(s))) {
					return locationHierarchyService.getLocNameForLocUUID(str);
				}
			}
		}
		return "";
	}

	public static void main(String[] args) throws IOException, AppInitializationException, DSPException {
		BasinTask2 task = AppContext.getApplicationContext().getBean(BasinTask2.class);
		System.out.println("Starting to write into file...");
		task.start();
		System.out.println("Done writing");
	}
}
*/