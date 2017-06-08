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
import com.vassarlabs.iwm.dss.waterbalance.pojo.api.IWaterBalance;
import com.vassarlabs.iwm.soilmoisture.pojo.api.ILocationBhuvanData;
import com.vassarlabs.iwm.soilmoisture.service.BhuvanDataService;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
//@Component
public class EToTest {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	
	@Autowired	
	ILocationHierarchyService locService;
	
	@Autowired
	protected BhuvanDataService bhuvanService;
	
	@Autowired
	private ISoilMoistureWaterBalanceService smService;
	
	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException {
		applicationInitService.initialize();
		locService.getAllUUIDForChildTypeParentType("DISTRICT", "MANDAL");
		Map<Long, IBhuvanGridData> map = bhuvanService.calculateBhuvanDataOverGrids(20161121, 0.15, 0.15);
		double totalETO = 0.0;
		for(long id:map.keySet()){
			IBhuvanGridData grid = map.get(id);
			totalETO+= grid.getEvapotranspirationInTMC();
		}
		System.out.println("Total ETo "+totalETO+" map Size "+map.size());
		
		
		Map<Long, IBhuvanGridData> map = bhuvanService.calculateBhuvanDataOverGrids(20160601, 20161121, 0.15, 0.15);
		double totalETO = 0.0;
		for(long id:map.keySet()){
			IBhuvanGridData grid = map.get(id);
			totalETO+= grid.getEvapotranspirationInTMC();
		}
		Map<String, ILocationBhuvanData> map = bhuvanService.getBhuvanDataOverMandals(20160601, 20160602);
		System.out.println("Result Map.size "+map.size());
		double totalETO = 0.0;
		for(String id:map.keySet()){
			ILocationBhuvanData grid = map.get(id);
			totalETO+= grid.getEvapotranspirationInTMC();
			System.out.println(" ILocationBhuvanData "+grid.toString() );
		}
		System.out.println("Total ETo "+totalETO+" map Size "+map.size());
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(15);
		//list.add(8);
		//list.add(9);
		
		Map<String, IWaterBalance> SMmap = smService.getEvapotranspirationData(list, DateUtils.getModelDateInMillis(20120601), DateUtils.getModelDateInMillis(20160602));
		double totalETO1 = 0.0;
		for(String id:SMmap.keySet()){
			IWaterBalance grid = SMmap.get(id);
			totalETO1+= grid.getTotalOutFlow1();
			System.out.println("District Name "+id+"  "+grid.getTotalOutFlow1());
		}
		
		
		System.out.println("Total ETo "+totalETO1+" map Size ");
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Start of IWM TEST");
		EToTest smmTest = AppContext.getApplicationContext().getBean(EToTest.class);
		smmTest.testMe();
		System.out.println("End of IWM TEST");
		// iwmTest.finalize();
	}
}
*/