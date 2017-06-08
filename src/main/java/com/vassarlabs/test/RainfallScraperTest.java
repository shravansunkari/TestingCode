/*package com.vassarlabs.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.scrapers.parsers.impl.RFForcastDataScraper;
import com.vassarlabs.scrapers.parsers.impl.RainfallScraperParser;

@Component
public class RainfallScraperTest {
	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	public void testMe() throws DSPException, AppInitializationException, ObjectNotFoundException {

		applicationInitService.initialize();
		RainfallScraperParser rf = new RainfallScraperParser();
		RFForcastDataScraper rrf = new RFForcastDataScraper();
		rf.execute();
		rrf.execute();
		
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Start of RF SCRAPER TEST");
		RainfallScraperTest RfTest = AppContext.getApplicationContext().getBean(RainfallScraperTest.class);
		RfTest.testMe();
		System.out.println("End of RF SCRAPER TEST");

	}
}
*/