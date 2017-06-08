/*package com.vassarlabs.test.data.creator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.iwm.soilmoisture.stress.service.api.ISMLocMapDataService;
import com.vassarlabs.location.service.api.ILocationHierarchyService;

//@Component
public class ISROSMMandalToIWMManadalMapping {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	@Autowired
	protected ISMLocMapDataService smNrscLocMap;

	@Autowired
	protected ILocationHierarchyService locService;

	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException {

		applicationInitService.initialize();
		locService.getAllUUIDForChildTypeParentType("DISTRICT", "MANDAL");
		Map<String, String> smTOIwmMap = smNrscLocMap.getSMStressToIWMLocMap();

		String csvFile = "/home/srikanth/CodeBaseGit/files/bhuvan/otherProject/Corrected_Grid_mandal_intersection.csv";
		String outPutFile = "/home/srikanth/CodeBaseGit/files/bhuvan/otherProject/corrected-iwm-mandal-gridId.csv";
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			StringBuffer buffer = new StringBuffer();
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] splitLine = line.split(cvsSplitBy);
				String gridId = splitLine[1];
				String area = splitLine[2];
				String smMandalFullName = splitLine[3];
				if (smTOIwmMap.containsKey(smMandalFullName)) {
					String iwmMandalFullName = smTOIwmMap.get(smMandalFullName);
					String iwmMandalName = iwmMandalFullName.split("##")[1];
					List<String> locNames = new ArrayList<String>();
					locNames.add(iwmMandalName);
					int mandalId = locService.getlocationIdFromNames(locNames).get(0);
					buffer = new StringBuffer();
					buffer.append(gridId);
					buffer.append(",");
					
					buffer.append(mandalId);
					buffer.append(",");
					buffer.append(iwmMandalName);
					buffer.append(",");
					buffer.append(area);
					buffer.append(",");
					
					buffer.append(smMandalFullName);
					buffer.append("\n");
					writeToFile(buffer.toString(), outPutFile);
				} else {
					System.out.println("No IWM Mandal Found For " + smMandalFullName);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void writeToFile(String builder,String fileName) {
		FileWriter fileWriter = null;
		try {
			//System.out.println("Write To file start");
			fileWriter = new FileWriter(fileName, true);
			fileWriter.append(builder.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Start of IWM TEST");
		ISROSMMandalToIWMManadalMapping smmTest = AppContext.getApplicationContext()
				.getBean(ISROSMMandalToIWMManadalMapping.class);
		smmTest.testMe();
		System.out.println("End of IWM TEST");
		// iwmTest.finalize();
	}
}
*/