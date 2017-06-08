/*package com.vassarlabs.bhuvan.scraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vassarlabs.scrapers.utils.ScraperConstants;


public class BhuvanLocationValues {

	
	 * Example Final URL must be
	 * 
	 * http://bhuvan-noeda.nrsc.gov.in/cgi-bin/postgis.exe?
	 * LAYERS=RO,grid,state&QUERY_LAYERS=RO,grid,state&STYLES=,,
	 * &SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo
	 * FEATURE_COUNT=10&HEIGHT=500&WIDTH=440&FORMAT=image/png&INFO_FORMAT=text/
	 * html&SRS=EPSG:4326
	 * &X=220&Y=290&date=2016-07-03&BBOX=61.328125,0.527344,100,44.472656
	 * 
	 * Append the values in last Line 1. date <--format(YYYY-mm-dd) 2. BBOX
	 * <--format(A,B,C,D) 3. X <--between the Limits Set Above 4. Y
	 * 
	 

	
	public static void main(String args[]) throws IOException {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("date", new SimpleDateFormat("YYYY-MM-dd").format(new Date(1467503045000l)));
		paramsMap.put("BBOX", BhuvanScraperConstants.BBOX_STRING);

		String fileName = System.getProperty("user.home") + "/CodeBaseGit/files/bhuvan/bhuvanScraped-9-11.csv";
		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));

			// Write the CSV file header
			writer.append(BhuvanScraperConstants.FILE_HEADER.toString());

			// Add a new line separator after the header
			writer.append(BhuvanScraperConstants.NEW_LINE_SEPARATOR);
			long startTime = System.currentTimeMillis();
			// Grid Size is 9x9 testing for values using 8x8, so if duplicates
			// occur
			for (int x = BhuvanScraperConstants.LEFT_X; x <= BhuvanScraperConstants.RIGHT_X; x = x + 8) {
				for (int y = BhuvanScraperConstants.TOP_Y; y <= BhuvanScraperConstants.BOTTOM_Y; y = y + 8) {

					paramsMap.put("X", "" + x);
					paramsMap.put("Y", "" + y);
					String tempURL = generateURL(paramsMap);
					System.out.println(BhuvanScraperConstants.URL_PREFIX + URLEncoder.encode(tempURL, "CP1252"));
					try {
						Document doc2 = Jsoup
								.connect(BhuvanScraperConstants.URL_PREFIX + URLEncoder.encode(tempURL, "CP1252"))
								.timeout((int) ScraperConstants.APSDPS_CONNECT_TIMEOUT).maxBodySize(0).get();
						Element body2 = doc2.body();
						String ar2[] = body2.text().split(" |,");
						if (ar2 != null && ar2.length >= 5) {
							BhuvanData bData = convertETOParsedData(ar2);
							bData.setX(x);
							bData.setY(y);
							// Write a new student object list to the CSV file
							writer.append(String.valueOf(bData.getX()));
							writer.append(BhuvanScraperConstants.COMMA_DELIMITER);
							writer.append(String.valueOf(bData.getY()));
							writer.append(BhuvanScraperConstants.COMMA_DELIMITER);
							writer.append(String.valueOf(bData.getGridId()));
							writer.append(BhuvanScraperConstants.COMMA_DELIMITER);
							writer.append(String.valueOf(bData.getLongitude()));
							writer.append(BhuvanScraperConstants.COMMA_DELIMITER);
							writer.append(String.valueOf(bData.getLatitude()));
							writer.append(BhuvanScraperConstants.COMMA_DELIMITER);
							writer.append(String.valueOf(bData.getRunOff()));
							writer.append(BhuvanScraperConstants.NEW_LINE_SEPARATOR);
							writer.flush();
							System.out.println("Inserted values for X,Y " + x + " " + y);
						}
					}

					catch (SocketTimeoutException e) {
						System.out.println("Exception occured for X,Y values " + x + " " + y);
						// e.printStackTrace();
					} catch (UnknownHostException e) {
						System.out.println("Host not defined");
					}
				}
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time Taken " + (endTime - startTime));
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}
}*/