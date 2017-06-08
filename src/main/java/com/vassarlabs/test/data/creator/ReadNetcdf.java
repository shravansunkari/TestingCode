/*package com.vassarlabs.test.data.creator;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.vassarlabs.common.utils.DateUtils;

import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

public class ReadNetcdf {

	public static void main(String args[]) {

		try {
			NetcdfDataset nc = NetcdfDataset
					.openDataset("/home/srikanth/Downloads/DET_10N20N_76E85E_2016121012.nc");
			dumpFile(nc,"DET_10N20N_76E85E_2016111712.nc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void dumpFile(NetcdfDataset nc, String fileName) throws IOException {

		PrintWriter writer = new PrintWriter("/home/srikanth/Downloads/negativeDiff-2016121012.csv",
				"UTF-8");

		writer.println("Latitude, Longitude, toTime, toRainfall, fromTime, fromRainfall, rainfallDifference");
		String words[] = fileName.split("_");
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
		String modelDate = words[words.length -1].split("\\.")[0].substring(0, 8);
		System.out.println(modelDate);
		System.out.println(nc);
		String timeUnits[] = nc.findVariable("time").getUnitsString().split(" ");
		String date = timeUnits[2];
		String timeHours = timeUnits[3];
		System.out.println("Time "+nc.findVariable("time").getUnitsString());
		System.out.println("Date Given "+date);
		
		System.out.println("Date Given "+timeHours);
		timeHours = timeHours.substring(0, timeHours.length() -1);
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-mm-dd");
		String formatedDate = DateUtils.changeDateFormat("yyyy-mm-dd", "yyyymmdd", date);
		
		System.out.println("formatedDate "+formatedDate);
		long millis = DateUtils.getModelDateInMillis(Integer.parseInt(formatedDate));
		//Add Hours
		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(millis);
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		System.out.println(dateFormat.format(cal.getTime()));
		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timeHours));
		cal.add(Calendar.HOUR_OF_DAY, 5);
		cal.add(Calendar.MINUTE, 30);
		
		System.out.println(cal.getTimeInMillis());
		
		Variable lat = nc.findVariable("latitude");
		Array latArr = lat.read();

		Variable lon = nc.findVariable("longitude");
		Array lonArr = lon.read();

		Variable time = nc.findVariable("time");
		Array timeArr = time.read();

		Variable temperature_2m = nc.findVariable("2m_temperature");
		Array tempArray = temperature_2m.read();

		Variable relative_humidity_2m = nc.findVariable("2m_relative_humidity");
		Array humArray = relative_humidity_2m.read();

		Variable accumulated_precip_6hr = nc.findVariable("6hr_accumulated_precip");
		Array accPrecArray = accumulated_precip_6hr.read();

		Variable U_wind = nc.findVariable("10m_U_wind");
		Array U_WindArray = U_wind.read();

		Variable V_Wind = nc.findVariable("10m_V_wind");
		Array V_WindArray = V_Wind.read();

		Variable windMag = nc.findVariable("wind_magnitude");
		Array windMagArray = windMag.read();

		Variable windDir = nc.findVariable("wind_direction");
		Array windDirArray = windDir.read();

		Variable mean_sea_level_pressure = nc.findVariable("mean_sea_level_pressure");
		Array meanSeaLevelPressureArray = mean_sea_level_pressure.read();
		int v = 0;

		Map<String, Map<Integer, Double>> corrdTimeValueMap = new HashMap<String, Map<Integer, Double>>();
		for (int i = 0; i < timeArr.getSize(); i++) {
			System.out.println("Time Aarr "+timeArr.getDouble(i));
			for (int j = 0; j < latArr.getSize(); j++) {
				for (int k = 0; k < lonArr.getSize(); k++) {
					
					String latLngKey = latArr.getDouble(j)+"##"+ lonArr.getDouble(k);
					
					writer.println(timeArr.getDouble(i) + " , " + latArr.getDouble(j) + " , " + lonArr.getDouble(k)
							+ " , " + tempArray.getDouble(v) + " , " + humArray.getDouble(v) + " , "
							+ accPrecArray.getDouble(v) + " , " + U_WindArray.getDouble(v) + " , "
							+ V_WindArray.getDouble(v) + " , " + windMagArray.getDouble(v) + " , "
							+ windDirArray.getDouble(v) + " , " + meanSeaLevelPressureArray.getDouble(v));
					
					
					if(!corrdTimeValueMap.containsKey(latLngKey)){
						Map<Integer, Double> timeToRainMap = new TreeMap<Integer, Double>();
						corrdTimeValueMap.put(latLngKey, timeToRainMap);
					}
					
					Map<Integer, Double> timeToRainMap = corrdTimeValueMap.get(latLngKey);
					timeToRainMap.put((int)timeArr.getDouble(i), accPrecArray.getDouble(v));
					
					v++;
				}
			}
		}
		
		for(String latLngKey : corrdTimeValueMap.keySet()){
			String latitude = latLngKey.split("##")[0];
			String longitude = latLngKey.split("##")[1];
			
			Map<Integer, Double> timeToRainMap = corrdTimeValueMap.get(latLngKey);
			double prevRainfall = timeToRainMap.get(0);
			int prevTime  = 0;
			timeToRainMap.keySet().remove(0);
			for(int currTime : timeToRainMap.keySet()){
				double currRainfall = timeToRainMap.get(currTime);
				double rainfallToConsider = currRainfall - prevRainfall;
				if(currRainfall < prevRainfall){
					writer.println(latitude + " , " + longitude + " , " + currTime + " , "  + currRainfall + " , " + prevTime
					+ " , " + prevRainfall+ " , "+ rainfallToConsider);
				}
				prevTime = currTime;
				prevRainfall = currRainfall;
				
			}
		}
		
		
		
		writer.close();
	}
}*/