/*package com.vassarlabs.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;

@Component
public class ECropScraperTest {

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException {

		applicationInitService.initialize();
		System.out.println("ECropScraperTest TEST Start");
		//ECropDataWebServiceParser scraper = new ECropDataWebServiceParser();
		//scraper.method();
		
		 * ExecutorService service = Executors.newFixedThreadPool(2);
		 * List<Integer> villageList = new ArrayList<Integer>();
		 * villageList.add(1753); villageList.add(1742); villageList.add(1743);
		 * villageList.add(1744); villageList.add(1745); villageList.add(1746);
		 * villageList.add(1747); villageList.add(1748); villageList.add(1749);
		 * villageList.add(1750); villageList.add(1751); villageList.add(1752);
		 * 
		 * villageList.add(1754); villageList.add(1755); villageList.add(1756);
		 * villageList.add(1757); villageList.add(1758); villageList.add(1759);
		 * villageList.add(1760); villageList.add(1761); villageList.add(1762);
		 * villageList.add(1763); villageList.add(1764); villageList.add(1765);
		 * ECropSoapServiceThread thread = new
		 * ECropSoapServiceThread(villageList); service.submit(thread);
		 * service.shutdown();
		  long startTs = System.currentTimeMillis();

		System.out.println("ECropScraperTest TEST END " + ((System.currentTimeMillis() - startTs) / 1000) + " secs");
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("ECropScraperTest of DC TEST");
		ECropScraperTest dcTest = AppContext.getApplicationContext().getBean(ECropScraperTest.class);
		dcTest.testMe();
		System.out.println("ECropScraperTest of DC TEST");
		// dcTest.finalize();
	}

}
*/