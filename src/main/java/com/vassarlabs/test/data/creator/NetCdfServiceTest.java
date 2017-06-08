/*package com.vassarlabs.test.data.creator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.iwm.commandcenter.service.api.IWeatherForecastService;
import com.vassarlabs.iwm.commandcenter.service.impl.ISRONetCDFFileParserService;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vividsolutions.jts.io.ParseException;
@Component
public class NetCdfServiceTest {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;


	@Autowired
	protected ILocationHierarchyService locService;
	
	@Autowired
	ISRONetCDFFileParserService netcdfService;
	@Autowired	
	IWeatherForecastService wfService;
	
	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException, IOException, ParseException {

		applicationInitService.initialize();
		locService.getAllUUIDForChildTypeParentType("DISTRICT", "MANDAL");
		
		
		List<String> filePathList = new ArrayList<String>();
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016120812.nc");
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016121000.nc");
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016121012.nc");
		
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016121100.nc");
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016121112.nc");
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016121200.nc");
		filePathList.add("/home/srikanth/Downloads/netcdfFiles/DET_10N20N_76E85E_2016121212.nc");
		
		
		for(String filePath : filePathList){
			long start = System.currentTimeMillis();
			List<String> strings = netcdfService.parseFile(filePath);
			for(String s : strings){
				System.out.println(s);
			}
			System.out.println("netcdfService out of populateISro  ");
			System.out.println(" Time Taken "+(System.currentTimeMillis() - start)/1000);
				
		}
		
		NetcdfDataset nc = NetcdfDataset.openDataset(filePathList.get(0));
		
		netcdfService.populateISROGridData(nc);
		
		
		
		
		
		
		
		
		
		
		
		LinkedHashMap<ICCForecastBlock, ICCWeatherForecast> data = wfService.get24HourForecast(1479513600000l);
		System.out.println("DATA KEY SET "+data.keySet());
		for(ICCForecastBlock block : data.keySet()){
			
			System.out.println(data.get(block).getMandalForecastData());
		}
		//System.out.println(data);
		
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Start of IWM TEST");
		NetCdfServiceTest smmTest = AppContext.getApplicationContext().getBean(NetCdfServiceTest.class);
		smmTest.testMe();
		System.out.println("End of IWM TEST");
		// iwmTest.finalize();
	}
}
*/