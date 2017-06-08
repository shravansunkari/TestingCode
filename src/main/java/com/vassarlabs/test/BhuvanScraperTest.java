/*package com.vassarlabs.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.eventmapper.err.EventHandlingException;
import com.vassarlabs.scrapers.processor.handler.impl.BhuvanScraperEventHandler;

@Component
public class BhuvanScraperTest {
	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;

	public void testMe()
			throws DSPException, AppInitializationException, ObjectNotFoundException, EventHandlingException {

		applicationInitService.initialize();
		BhuvanScraperEventHandler bhu = new BhuvanScraperEventHandler();
		bhu.handleEvent(null);
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("BhuvanScraperTest of DC TEST");
		BhuvanScraperTest dcTest = AppContext.getApplicationContext().getBean(BhuvanScraperTest.class);
		dcTest.testMe();
		System.out.println("BhuvanScraperTest of DC TEST");
		// dcTest.finalize();
	}
}*/