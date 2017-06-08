/*package com.vassarlabs.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.iwm.commandcenter.service.GridMappingService;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vassarlabs.location.utils.LocationConstants;

@Component
public class GridServicesTest {
	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	
	@Autowired
	ILocationHierarchyService locService;
	
	@Autowired
	GridMappingService gridService;
	
	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException {

		applicationInitService.initialize();
		gridService.findNRSCGridForLatLong( 16.306928, 80.413404);
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Start of IWM TEST");
		GridServicesTest smmTest = AppContext.getApplicationContext().getBean(GridServicesTest.class);
		smmTest.testMe();
		System.out.println("End of IWM TEST");
		// iwmTest.finalize();
	}

}
*/