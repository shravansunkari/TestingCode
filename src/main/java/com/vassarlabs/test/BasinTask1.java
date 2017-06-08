package com.vassarlabs.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
public class BasinTask1 {
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
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/platform_data", "root", "root");
		} catch (Exception e) {
			System.out.println("error = " + e.getMessage());
		}
		try {
			fw = new FileWriter("/home/srikanth/Desktop/BasinLevel.java");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}
		fw.write("static {\n\n");
		fw.write("Map<String, ILocationStructureInfo> locToMasterDataMap = null;\n\n");

		// Services
		// Mapping from stateUUID to all BasinUUIDs
		Map<String, List<String>> stateToBasinUUIDMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.STATE, LocationConstants.BASIN);
		// Mapping from BasinUUID to subBasinUUIDs
		Map<String, List<String>> basinToSubBasinUUIDMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.BASIN, LocationConstants.SUBBASIN);
		// Mapping from subbasin to microbasin
		Map<String, List<String>> subBasinToMicroBasinUUIDMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.SUBBASIN, LocationConstants.MICROBASIN);

		Map<Integer, basin_class> total_map = new HashMap<>();

		getData("/home/srikanth/Desktop/data/cds.csv", total_map, 1);
		getData("/home/srikanth/Desktop/data/fp.csv", total_map, 2);
		getData("/home/srikanth/Desktop/data/pt.csv", total_map, 3);
		getData1("/home/srikanth/Desktop/basin_level/MI_TANK.csv", total_map, 4);
		getData("/home/srikanth/Desktop/data/others.csv", total_map, 5);
		getData("/home/srikanth/Desktop/data/rwhs.csv", total_map, 6);

		int checkc = 0;
		int farmc = 0;
		int percuc = 0;
		int mic = 0;
		int otherc = 0;
		int checkc_pro = 0;
		double mi_capacity = 0.0;
		double per_capacity = 0.0;

		int check_c = 0;
		int farm_c = 0;
		int per_c = 0;
		int mi_c = 0;
		int check_p_c = 0;
		int other_c = 0;

		double check_ca = 0.0;
		double farm_ca = 0.0;
		double per_ca = 0.0;
		double mi_ca = 0.0;
		double check_p_ca = 0.0;
		double other_ca = 0.0;

		for (int code : total_map.keySet()) {

			int total_count = 0;
			double total_capacity = 0.0;

			basin_class obj = total_map.get(code);

			check_c += obj.get_check_dam_count();
			farm_c += obj.get_farm_pond_count();
			per_c += obj.get_perculation_tank_count();
			mi_c += obj.get_mi_tank_count();
			check_p_c += obj.get_check_dam_proposed_count();
			other_c += obj.get_others_count();

			check_ca += obj.get_check_dam_capacity();
			farm_ca += obj.get_farm_pond_capacity();
			per_ca += obj.get_perculation_tank_capacity();
			mi_ca += obj.get_mi_tank_capacity();
			check_p_ca += obj.get_check_dam_proposed_capacity();
			other_ca += obj.get_others_capacity();

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

			per_capacity += obj.get_perculation_tank_capacity();

			checkc += obj.get_check_dam_count();
			farmc += obj.get_farm_pond_count();
			percuc += obj.get_perculation_tank_count();
			mic += obj.get_mi_tank_count();
			otherc += obj.get_others_count();
			mi_capacity += obj.get_mi_tank_capacity();
			checkc_pro += obj.get_check_dam_proposed_count();

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
			fw.write("structTypeToMasterDataMap.put(CHECK_DAM_PROPOSED, new LocationStructureInfo("
					+ obj.get_check_dam_proposed_count() + ", " + obj.get_check_dam_proposed_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(OTHER_STRUCTURES, new LocationStructureInfo("
					+ obj.get_others_count() + ", " + obj.get_others_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(TOTAL_STRUCTURES, new LocationStructureInfo(" + total_count + ", "
					+ total_capacity + "));\n\n");

		}

		fw.write("\n}");
		System.out.println("check =" + checkc);
		System.out.println("farm =" + farmc);
		System.out.println("percu =" + percuc);
		System.out.println("mi_tank =" + mic);
		System.out.println("checkdam proposed =" + checkc_pro);
		System.out.println("other =" + otherc);
		System.out.println("mi_tank_capacity =" + mi_capacity);
		System.out.println("per_tank_capacity =" + per_capacity);

		System.out.println("c =" + check_c);
		System.out.println("farm =" + farm_c);
		System.out.println("per_c =" + per_c);
		System.out.println("mi_c =" + mi_c);
		System.out.println("check_p_c =" + check_p_c);
		System.out.println("other_c =" + other_c);
		System.out.println("c =" + check_ca);
		System.out.println("farm =" + farm_ca);
		System.out.println("per_c =" + per_ca);
		System.out.println("mi_c =" + mi_ca);
		System.out.println("check_p_c =" + check_p_ca);
		System.out.println("other_c =" + other_ca);

		try {
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (Exception e) {
		}
	}

	
	
	public void getData(String csvFile, Map<Integer, basin_class> total_map, int type)
			throws DSPException, AppInitializationException {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					String basin_code = row[12];
					if(basin_code.length()==0){
						break;
					}
					int basin_code1 = Integer.parseInt(row[12]);
					int basin_count1 = Integer.parseInt(row[13]);
					double basin_capacity1 = Double.parseDouble(row[14]);
					basin_class basinData = null;
					if (!total_map.containsKey(basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(basin_code1);
					}
					String name = "Andhra Pradesh" + "##" + getIWMLocationNameForBasin(basin_code1, con);

					basinData.setLocName(name);

					if (type == 1) {
						basinData.set_check_dam_count(basin_count1);
						basinData.set_check_dam_capacity(basin_capacity1);
					} else if (type == 2) {
						basinData.set_farm_pond_count(basin_count1);
						basinData.set_farm_pond_capacity(basin_capacity1);
					} else if (type == 3) {
						basinData.set_perculation_tank_count(basin_count1);
						basinData.set_perculation_tank_capacity(basin_capacity1);
					} else if (type == 4) {
						basinData.set_mi_tank_count(basin_count1);
						basinData.set_mi_tank_capacity(basin_capacity1);
					} else if (type == 5) {
						basinData.set_others_count(basin_count1);
						basinData.set_others_capacity(basin_capacity1);
					} else if (type == 6) {
						basinData.set_check_dam_proposed_count(basin_count1);
						basinData.set_check_dam_proposed_capacity(basin_capacity1);
					}

					total_map.put(basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in1 " + line);
				}
			}
			try {
				br.close();
			} catch (Exception e) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getData1(String csvFile, Map<Integer, basin_class> total_map, int type)
			throws DSPException, AppInitializationException {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					
					int basin_code1 = Integer.parseInt(row[0]);
					int basin_count1 = Integer.parseInt(row[1]);
					double basin_capacity1 = Double.parseDouble(row[2]);
					basin_class basinData = null;
					if (!total_map.containsKey(basin_code1)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(basin_code1);
					}
					String name = "Andhra Pradesh" + "##" + getIWMLocationNameForBasin(basin_code1, con);

					basinData.setLocName(name);

					if (type == 1) {
						basinData.set_check_dam_count(basin_count1);
						basinData.set_check_dam_capacity(basin_capacity1);
					} else if (type == 2) {
						basinData.set_farm_pond_count(basin_count1);
						basinData.set_farm_pond_capacity(basin_capacity1);
					} else if (type == 3) {
						basinData.set_perculation_tank_count(basin_count1);
						basinData.set_perculation_tank_capacity(basin_capacity1);
					} else if (type == 4) {
						basinData.set_mi_tank_count(basin_count1);
						basinData.set_mi_tank_capacity(basin_capacity1);
					} else if (type == 5) {
						basinData.set_others_count(basin_count1);
						basinData.set_others_capacity(basin_capacity1);
					} else if (type == 6) {
						basinData.set_check_dam_proposed_count(basin_count1);
						basinData.set_check_dam_proposed_capacity(basin_capacity1);
					}

					total_map.put(basin_code1, basinData);
				} catch (Exception e) {
					System.out.println("Error in1 " + line);
				}
			}
			try {
				br.close();
			} catch (Exception e) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getIWMLocationNameForBasin(int code, Connection con) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select display_name from location where external_id = '" + Integer.toString(code) + "-B';");
			if (rs.next()) {
				String name = rs.getString(1);
				System.out.println("Found name = " + rs.getString(1));
				// con.close();
				return name;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\t" + code + "-B");
		}
		return "";
	}

	public int getBasinCode(int subbasin_code, Map<Integer, HashSet<Integer>> basin_subbasin_map) {

		for (int code : basin_subbasin_map.keySet()) {
			HashSet<Integer> set = basin_subbasin_map.get(code);
			if (set.contains(subbasin_code)) {
				return code;
			}
		}
		return -1;
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
	public static void main(String[] args) throws IOException, DSPException, AppInitializationException {
		BasinTask1 task = AppContext.getApplicationContext().getBean(BasinTask1.class);
		System.out.println("Starting to write into file...");
		task.start();
		System.out.println("Done writing");
	}
}