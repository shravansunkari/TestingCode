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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vassarlabs.location.utils.LocationConstants;

@Component
public class BasinTask {
	public static BufferedWriter bw = null;
	public static FileWriter fw = null;
	public Connection con = null;
	
	@Autowired
	protected ILocationHierarchyService locationHierarchyService;
	
	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	
	public void start() throws IOException, DSPException, AppInitializationException {
		
		applicationInitService.initialize();
		try{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/platform_data", "root", "root");
		}
		catch(Exception e){
			System.out.println("error = "+e.getMessage());
		}

		try {
			fw = new FileWriter("/home/srikanth/Desktop/SubBasinLevel.java");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}
		fw.write("static {\n\n");
		fw.write("Map<String, ILocationStructureInfo> locToMasterDataMap = null;\n\n");

		File folder = new File("/home/srikanth/Desktop/csv_files");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
		if (listOfFiles[i].isFile()) {

			String fileName = listOfFiles[i].getName();
			String csvFile = "/home/srikanth/Desktop/csv_files/" + fileName;
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				// Write here
				// Read from excel files
				try {
					String basin_name = row[0];
					int basin_code = Integer.parseInt(row[1]);
					String sub_basin_name = row[2];
					int sub_basin_code = Integer.parseInt(row[3]);
					String mi_basin_name = row[4];
					int mi_basin_code = Integer.parseInt(row[5]);

					if (!basin.containsKey(basin_code)) {
						basin.put(basin_code, basin_name);
					}
					if (!sub_basin.containsKey(sub_basin_code)) {
						sub_basin.put(sub_basin_code, sub_basin_name);
					}
					if (!micro_basin.containsKey(mi_basin_code)) {
						micro_basin.put(mi_basin_code, mi_basin_name);
					}

					if (!basin_subbasin.containsKey(basin_code)) {
						HashSet<Integer> subbasin = new HashSet<>();
						subbasin.add(sub_basin_code);
						basin_subbasin.put(basin_code, subbasin);
					} else {
						basin_subbasin.get(basin_code).add(sub_basin_code);
					}

					if (!subbasin_microbasin.containsKey(sub_basin_code)) {
						HashSet<Integer> mibasin = new HashSet<>();
						mibasin.add(mi_basin_code);
						subbasin_microbasin.put(sub_basin_code, mibasin);
					} else {
						subbasin_microbasin.get(sub_basin_code).add(mi_basin_code);
					}
				} catch (Exception e) {
					System.out.println("exception==>" + line);
				}
			}
			try{
				br.close();
			}
			catch(Exception e){
				
			}
		}
	}

		
		//Services
		 // Mapping from stateUUID to all BasinUUIDs
		Map<String, List<String>> stateToBasinUUIDMap = locationHierarchyService.getAllLocForParentChildTypes(LocationConstants.STATE, LocationConstants.BASIN); 
		// Mapping from BasinUUID to subBasinUUIDs
		Map<String, List<String>> basinToSubBasinUUIDMap = locationHierarchyService.getAllLocForParentChildTypes(LocationConstants.BASIN, LocationConstants.SUBBASIN); 
		// Mapping from subbasin to microbasin
		Map<String, List<String>> subBasinToMicroBasinUUIDMap = locationHierarchyService.getAllLocForParentChildTypes(LocationConstants.SUBBASIN, LocationConstants.MICROBASIN);
		   
		
		folder = new File("/home/srikanth/Desktop/counts1");
		listOfFiles = folder.listFiles();

		Map<Integer, basin_class> total_map = new HashMap<>();
			
		getData("/home/srikanth/Desktop/counts1/CHECKDAM.csv", total_map, basinToSubBasinUUIDMap, 1);
		getData("/home/srikanth/Desktop/counts1/FARM_POND.csv", total_map, basinToSubBasinUUIDMap, 2);
		getData("/home/srikanth/Desktop/counts1/PERCULATION_TANK.csv", total_map, basinToSubBasinUUIDMap, 3);
		getData("/home/srikanth/Desktop/counts1/MI_TANK.csv", total_map, basinToSubBasinUUIDMap, 4);
		getData("/home/srikanth/Desktop/counts1/OTHERS.csv", total_map, basinToSubBasinUUIDMap, 5);
		getData("/home/srikanth/Desktop/counts1/CHECK_DAM_PROPOSED.csv", total_map, basinToSubBasinUUIDMap, 6);
		
		// Checkdam
		String csvFile = "/home/srikanth/Desktop/counts1/CHECKDAM.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}
					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					basinData.set_check_dam_count(sub_basin_count1);
					basinData.set_check_dam_capacity(sub_basin_capacity1);
					total_map.put(sub_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in1 " + line);
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
		csvFile = "/home/srikanth/Desktop/counts1/FARM_POND.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
			
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}
					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					basinData.set_farm_pond_count(sub_basin_count1);
					basinData.set_farm_pond_capacity(sub_basin_capacity1);
					total_map.put(sub_basin_code1, basinData);
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
		csvFile = "/home/srikanth/Desktop/counts1/MI_TANK.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}


					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					basinData.set_mi_tank_count(sub_basin_count1);
					basinData.set_mi_tank_capacity(sub_basin_capacity1);
					total_map.put(sub_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in3 " + line);
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
		csvFile = "/home/srikanth/Desktop/counts1/OTHERS.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}

					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					basinData.set_others_count(sub_basin_count1);
					basinData.set_others_capacity(sub_basin_capacity1);

					total_map.put(sub_basin_code1, basinData);

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
		csvFile = "/home/srikanth/Desktop/counts1/PERCULATION_TANK.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}

					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					basinData.set_perculation_tank_count(sub_basin_count1);
					basinData.set_perculation_tank_capacity(sub_basin_capacity1);

					total_map.put(sub_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in5 " + line+ e.getMessage());
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
		
		//6
		csvFile = "/home/srikanth/Desktop/counts1/CHECK_DAM_PROPOSED.csv";
		br = null;
		line = "";
		cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}

					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					basinData.set_check_dam_proposed_count(sub_basin_count1);
					basinData.set_check_dam_proposed_capacity(sub_basin_capacity1);

					total_map.put(sub_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in5 " + line+ e.getMessage());
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

			int total_count = 0;
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
	
	public void getData(String csvFile, Map<Integer,basin_class> total_map, Map<String,List<String>> basinToSubBasinUUIDMap, int type) throws DSPException, AppInitializationException{
		applicationInitService.initialize();
		//String csvFile = "/home/srikanth/Desktop/counts1/CHECKDAM.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					int sub_basin_code1 = Integer.parseInt(row[0]);
					int sub_basin_count1 = Integer.parseInt(row[1]);
					double sub_basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(sub_basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(sub_basin_code1);
					}
					String BasinName = getUUIDForBasinFromSubBasinCode(sub_basin_code1, con, basinToSubBasinUUIDMap);    
					String name = "Andhra Pradesh" + "##" + BasinName + "##" + getIWMLocationNameForSubBasin(sub_basin_code1, con);
					
					basinData.setLocName(name);
					
					if(type == 1){
						basinData.set_check_dam_count(sub_basin_count1);
						basinData.set_check_dam_capacity(sub_basin_capacity1);
					}
					else if(type ==2){
						basinData.set_farm_pond_count(sub_basin_count1);
						basinData.set_farm_pond_capacity(sub_basin_capacity1);
					}
					else if (type == 3) {
						basinData.set_perculation_tank_count(sub_basin_count1);
						basinData.set_perculation_tank_capacity(sub_basin_capacity1);
					} else if (type == 4) {
						basinData.set_mi_tank_count(sub_basin_count1);
						basinData.set_mi_tank_capacity(sub_basin_capacity1);
					} else if (type == 5) {
						basinData.set_others_count(sub_basin_count1);
						basinData.set_others_capacity(sub_basin_capacity1);
					} else if (type == 6) {
						basinData.set_check_dam_proposed_count(sub_basin_count1);
						basinData.set_check_dam_proposed_capacity(sub_basin_capacity1);
					}
					
					total_map.put(sub_basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in1 " + line);
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
	
	public String getUUIDForBasinFromSubBasinCode(int basin_code, Connection con, Map<String, List<String>> basinToSubBasinUUIDMap) throws DSPException{
		String name = getIWMLocationNameForSubBasin(basin_code, con);
		
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

	public static void main(String[] args) throws IOException, DSPException, AppInitializationException {
		BasinTask task = AppContext.getApplicationContext().getBean(BasinTask.class);
		System.out.println("Starting to write into file...");
		task.start();
		System.out.println("Done writing");
	}
}
*/