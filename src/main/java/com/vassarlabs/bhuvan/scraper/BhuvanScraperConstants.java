/*package com.vassarlabs.bhuvan.scraper;

import java.util.concurrent.Semaphore;

public interface BhuvanScraperConstants {
	public final static int LEFT_X = 0;
	public final static int TOP_Y = 0;
	public final int RIGHT_X = 440;
	public final int BOTTOM_Y = 500;
	
	public final static int LEFT_X = 17;
	public final static int TOP_Y = 81;
	public final int RIGHT_X = 401;
	public final int BOTTOM_Y = 393;
	
	
	public final int Y_DIFF = 5;
	public final int X_DIFF = 5;
	// private final static int NORTH_WEST_X=416;
	// private final static int NORTH_WEST_Y=96;
	// private final static int SOUTH_WEST_X=416;
	// private final static int SOUTH_WEST_Y=435;
	public final String COMMA_DELIMITER = ",";
	public final String NEW_LINE_SEPARATOR = "\n";
	public final String FILE_HEADER = "X,Y,GRID_ID,LONGITUDE,LATITUDE,RUN_OFF";
	//Test BBOX
	public final String BBOX_STRING = "75.842284,10.775146,85.510253,21.761475";
	//First Known BBOX
	//public final String BBOX_STRING = "75.79834,10.291748,85.466309,21.278076";
	// Ap Shape File
	//public final String BBOX_STRING = "76.76060,12.62373,84.76465,19.16566";
	
	public final String URL_PREFIX = "http://bhuvan.nrsc.gov.in/cgi-bin/proxy.cgi?url=";
	public final String DEF_URL = "http://bhuvan-noeda.nrsc.gov.in/cgi-bin/postgis.exe?"
			+ "LAYERS=ET,grid,state&QUERY_LAYERS=ET,grid,state&STYLES=,,"
			+ "&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo" + "&FEATURE_COUNT=10&"
			+ "HEIGHT=500&WIDTH=440&FORMAT=image/png&INFO_FORMAT=text/html&SRS=" + "EPSG:4326";

	public final String DEF_URL = "http://bhuvan-noeda.nrsc.gov.in/cgi-bin/postgis.exe?"
			+ "STYLES=,,"
			+ "&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo" + "&FEATURE_COUNT=10&"
			+ "HEIGHT=500&WIDTH=440&FORMAT=image/png&INFO_FORMAT=text/html&SRS=" + "EPSG:4326";

	
	
	
	public final String ETO_LAYER = "ET,grid,state";
	public final String SOIL_MOISTURE_LAYER = "SM,grid,state";
	public final String RUN_OFF_LAYER = "RO,grid,state";
	
	public Semaphore MUTEX = new Semaphore(1);
	public final int NUM_OF_THREADS = 20;
	public static final int CONNECT_TIMEOUT = 60 * 1000;
}
*/