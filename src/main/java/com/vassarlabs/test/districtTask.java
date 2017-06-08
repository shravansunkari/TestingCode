/*package com.vassarlabs.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.config.spring.AppContext;

@Component
public class districtTask {
	public static BufferedWriter bw = null;
	public static FileWriter fw = null;
	public Connection con = null;
	private double Perculation_Tanks = 0.000353;
	private double CheckDams = 0.000353;
	private double Farm_Ponds = 0.0000071;
	private double others = 0.000353;
	private double check_dam_proposed = 0.000353;


	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	public void start() throws IOException, AppInitializationException{

		applicationInitService.initialize();
		
		try {
			fw = new FileWriter("/home/srikanth/Desktop/DistrictLevelkkkk.java");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}

		fw.write("static {\n\n");
		fw.write("Map<String, ILocationStructureInfo> locToMasterDataMap = null;\n\n");
		
		Map<String, HashSet<String>> district_mandal = new HashMap<>();
		
		File folder = new File("/home/srikanth/Desktop/district_mandal_map");
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {

				String fileName = listOfFiles[i].getName();
				String csvFile = "/home/srikanth/Desktop/district_mandal_map/" + fileName;
				BufferedReader br = null;
				String line = "";
				String cvsSplitBy = ",";

				br = new BufferedReader(new FileReader(csvFile));

				while ((line = br.readLine()) != null) {

					String[] row = line.split(cvsSplitBy);
					
					try {
						String dist = row[0];
						String man = row[1];

						if (!district_mandal.containsKey(dist)) {
							HashSet<String> mandals = new HashSet<>();
							mandals.add(man);
							district_mandal.put(dist, mandals);
						} else {
							district_mandal.get(dist).add(man);
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

		Map<String, basin_class> total_map = new HashMap<>();

		getData("/home/srikanth/Desktop/district/check_dam.csv", total_map, 1);
		getData("/home/srikanth/Desktop/district/farm_pond.csv", total_map, 2);
		getData("/home/srikanth/Desktop/district/perculation_tank.csv", total_map, 3);
		//getData("/home/srikanth/Desktop/district/mi_tank.csv", total_map, 4);
		getData("/home/srikanth/Desktop/district/others.csv", total_map, 5);
		getData("/home/srikanth/Desktop/district/check_dam_proposed.csv", total_map, 6);

		// Data will be stored in total_map.
		for (String dist : total_map.keySet()) {

			int total_count = 0;
			double total_capacity = 0.0;

			basin_class obj = total_map.get(dist);

			total_count += obj.get_check_dam_count();
			total_count += obj.get_farm_pond_count();
			//total_count += obj.get_mi_tank_count();
			total_count += obj.get_perculation_tank_count();
			total_count += obj.get_others_count();

			total_capacity += obj.get_check_dam_capacity();
			total_capacity += obj.get_farm_pond_capacity();
			//total_capacity += obj.get_mi_tank_capacity();
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
			fw.write("structTypeToMasterDataMap.put(CHECK_DAM_PROPOSED, new LocationStructureInfo("
					+ obj.get_check_dam_proposed_count() + ", " + obj.get_check_dam_proposed_capacity() + "));\n");
			fw.write("structTypeToMasterDataMap.put(OTHER_STRUCTURES, new LocationStructureInfo("
					+ obj.get_others_count() + ", " + obj.get_others_capacity() + "));\n");
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
		try {
			con.close();
		} catch (Exception e) {
		}
	}

	private class basin_class {
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

	public void getData(String csvFile, Map<String, basin_class> total_map, int type)
			 {
		// String csvFile = "/home/srikanth/Desktop/counts1/CHECKDAM.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);
				try {
					String dist = row[0];
					int count = Integer.parseInt(row[1]);
					basin_class basinData = null;
					
					if (!total_map.containsKey(dist)) {
						basinData = new basin_class();
					} else {
						basinData = total_map.get(dist);
					}
					
					String name = "Andhra Pradesh" + "##" + dist;

					basinData.setLocName(name);

					if (type == 1) {
						basinData.set_check_dam_count(count);
						basinData.set_check_dam_capacity(count * CheckDams);
					} else if (type == 2) {
						basinData.set_farm_pond_count(count);
						basinData.set_farm_pond_capacity(count * Farm_Ponds);
					} else if (type == 3) {
						basinData.set_perculation_tank_count(count);
						basinData.set_perculation_tank_capacity(count * Perculation_Tanks);
					}
					else if (type == 4) {
						basinData.set_mi_tank_count(count);
						basinData.set_mi_tank_capacity(count * mi_tank);
					} 
					else if (type == 5) {
						basinData.set_others_count(count);
						basinData.set_others_capacity(count * others);
					} else if (type == 6) {
						basinData.set_check_dam_proposed_count(count);
						basinData.set_check_dam_proposed_capacity(count * check_dam_proposed);
					}

					total_map.put(dist, basinData);
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
	public static void main(String[] args) throws IOException, AppInitializationException {
		// BasinTask task = new BasinTask();
		districtTask task = AppContext.getApplicationContext().getBean(districtTask.class);
		System.out.println("Starting to write into file...");
		task.start();
		System.out.println("Done writing");
	}
}
*/