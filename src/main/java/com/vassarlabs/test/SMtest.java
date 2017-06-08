/*package com.vassarlabs.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassaralabs.iwm.dss.wbs.sm.service.api.ISoilMoistureWaterBalanceService;
import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.DateUtils;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.iwm.pojo.api.IAWSEventData;
import com.vassarlabs.iwm.service.api.IAWSEventDataService;
import com.vassarlabs.iwm.soilmoisture.stress.service.api.ISoilMoistureStressService;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vassarlabs.location.utils.LocationConstants;

@Component
public class SMtest {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	@Autowired
	private ISoilMoistureWaterBalanceService soilMoistureWaterBalanceService;

	@Autowired
	private ISoilMoistureStressService soilMoistureStressService;
	
	@Autowired
	private IAWSEventDataService awsService;
	
	@Autowired
	private ILocationHierarchyService locationHierarchyService;
	
	@Autowired
	private ISoilMoistureStressService smStressService;
	
	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException {

		System.out.println("Im in testMe()");
		applicationInitService.initialize();

		
		 * List<Integer> a= new ArrayList<Integer>(); a.add(1);
		 * 
		 * a.add(5); a.add(6); a.add(7);
		 * 
		 * Map<String, IWaterBalance> data =
		 * soilMoistureWaterBalanceService.getSoilMoistureStorageData(a,
		 * System.currentTimeMillis());
		 

		// System.out.println(data.toString());
		long mandalStart = System.currentTimeMillis();
		List<String> mandalFullnameList = new ArrayList<String>();
		mandalFullnameList.add("anantapur##anantapur".toUpperCase());
		mandalFullnameList.add("visakhapatnam##pedabayulu".toUpperCase());
		mandalFullnameList.add("prakasam##podili".toUpperCase());
		soilMoistureStressService.getSMStressDataForRange(20160601, 20161010, mandalFullnameList, IWMConstants.SM_DEFAULT_ROOTZONE_DEPTH);
		System.out.println("MANDAL END " + (System.currentTimeMillis() - mandalStart));

		long villStart = System.currentTimeMillis();

		List<String> villFullNameList = new ArrayList<String>();
		villFullNameList.add("anantapur##anantapur##a.narayanapuram".toUpperCase());
		villFullNameList.add("visakhapatnam##pedabayulu##laxmipuram".toUpperCase());
		villFullNameList.add("prakasam##podili##kesavabhotla palem".toUpperCase());

		soilMoistureStressService.getSMStressDataForRange(20160601, 20161010, villFullNameList, IWMConstants.SM_DEFAULT_ROOTZONE_DEPTH);
		System.out.println("Village END " + (System.currentTimeMillis() - villStart));
		
		
		
		List<String> awsLocationUUIDs = locationHierarchyService.getListOfLocationUUIDSForALocationType(LocationConstants.AWS);
		
		Map<String, ArrayList<IAWSEventData>> data = awsService.getAllAWSData(awsLocationUUIDs, System.currentTimeMillis() - 10 * DateUtils.TWENTY_FOUR_HOURS_IN_SECONDS, 3);
		System.out.println("Map Size "+data.size());
		for(String locUUID:data.keySet()){
			//System.out.println("Location UUID : "+locUUID);
			System.out.println(" max wind Value "+data.get(locUUID).size());
		}
		
	}

	public static void main(String[] args) throws Throwable {

		try{
		System.out.println("Start of IWM TEST");
		//SMtest smmTest = AppContext.getApplicationContext().getBean(SMtest.class);
		//smmTest.testMe();
		//System.out.println("End of IWM TEST");
		SMtest obj=new SMtest();
		obj.smStressService.getAllRFStressData(20170309);
		// iwmTest.finalize();
		}
		catch(Exception e){
			System.out.println("erro: "+e.getMessage());
		}
	}

}
*/