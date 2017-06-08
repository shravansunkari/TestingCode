/*package com.vassarlabs.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.common.utils.err.VLException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.iwm.commandcenter.dsp.impl.WeatherForecastDSPImpl;
import com.vassarlabs.iwm.commandcenter.service.api.IISRODataService;
import com.vassarlabs.iwm.commandcenter.service.api.IWeatherForecastService;
import com.vassarlabs.iwm.dss.waterbalance.service.api.IWaterBalanceService;
import com.vassarlabs.iwm.soilmoisture.stress.service.api.ISoilMoistureStressService;
import com.vassarlabs.location.pojo.api.IMandalGeoData;
import com.vassarlabs.location.service.api.IMandalGeoDataService;
import com.vividsolutions.jts.io.ParseException;

@Component
public class WeatherForecastTest {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	@Autowired
	private IWaterBalanceService waterBalanceService;

	@Autowired
	IWeatherForecastService wfService;

	@Autowired
	protected IMandalGeoDataService mandalGeoDataService;
	
	@Autowired
	protected ISoilMoistureStressService soilMoistureStressService;

	@Autowired
	WeatherForecastDSPImpl wfDsp;
	
	@Autowired
	IISRODataService isroService;
	
	public void testMe() throws IOException, ParseException, VLException {

		applicationInitService.initialize();
		IRFWBData data = waterBalanceService.getRFData(1481252400000l, 1481513600000l);

		Map<String, Map<String, IWaterBalance>> mandalData;

		mandalData = data.getMandalLevelData();
		Map<String, Map<String, IWaterBalance>> sensorData = data.getSensorLevelData();

		for (String district : mandalData.keySet()) {
			System.out.println("District " + district);
			for (String mandalName : mandalData.get(district).keySet()) {
				System.out.println("Mandal name " + mandalName + " value "
						+ mandalData.get(district).get(mandalName).getTotalLevelValue1());
			}
		}
		
		
		LinkedHashMap<ICCForecastBlock, ICCWeatherForecast> fData = wfService.get24HourForecast(System.currentTimeMillis());
		
		for(ICCForecastBlock block : fData.keySet()){
			
			System.out.println("BLOCK           "+block.getBlockName());
			for(String district : fData.get(block).getMandalForecastData().keySet()){
				System.out.println("District           "+district);
				
				for(String mandal: fData.get(block).getMandalForecastData().get(district).keySet()){
					System.out.println(mandal+" "+fData.get(block).getMandalForecastData().get(district).get(mandal).getRf());
				}
				
			}
		}
		
		List<Integer> mandalLocList = new ArrayList<Integer>();
		mandalLocList.add(683);
		mandalLocList.add(682);
		mandalLocList.add(177);
		mandalLocList.add(156);
		mandalLocList.add(151);
		mandalLocList.add(148);
		
			Map<Integer, Double> mandalAreaMap = new HashMap<Integer, Double>();
			for(Integer mandalId : mandalLocList) {
				IMandalGeoData geoData = null;
				try {
					geoData = mandalGeoDataService.getMandalGeoDataByMandalId(mandalId);
				}
				catch (ObjectNotFoundException e) {
					System.out.println("RFWBSService:getRFWBData throws exception for " + mandalId);
					continue;
				}
				mandalAreaMap.put(mandalId, geoData.getArea());
				System.out.println(" mandal Id "+mandalId+" Area "+geoData.getArea());
			}
		
		
		//
		//wfDsp.copyWeatherForecastDataToHistoryData(1481371200000l + 2000);
		//wfDsp.deleteWeatherForecastRecords(1481371200000l + 2000);
		
		//wfService.deleteOldWeatherForecastRecords();
		isroService.deleteOldWeatherForecastRecords();
		Map<String, IWaterBalance> districtData = data.getDistrictLevelData();
		for (String district : districtData.keySet()) {
			System.out.println("District " + district + " Data " + districtData.get(district).getTotalLevelValue1());
		}

	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Start of IWM TEST");
		WeatherForecastTest smmTest = AppContext.getApplicationContext().getBean(WeatherForecastTest.class);
		smmTest.testMe();
		System.out.println("End of IWM TEST");
		// iwmTest.finalize();
	}
}
*/