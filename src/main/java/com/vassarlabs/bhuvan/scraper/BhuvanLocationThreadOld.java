/*package com.vassarlabs.bhuvan.scraper;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BhuvanLocationThreadOld implements Runnable {
	private Queue<String> sharedQueue;
	private long referenceTs;
	private String fileName;

	public Queue<String> getSharedQueue() {
		return sharedQueue;
	}

	public void setSharedQueue(Queue<String> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	public long getReferenceTs() {
		return referenceTs;
	}

	public void setReferenceTs(long referenceTs) {
		this.referenceTs = referenceTs;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private synchronized String getTopXY() {
		return this.sharedQueue.poll();
	}

	public BhuvanLocationThreadOld(Queue<String> sharedQueue, long referenceTs, String fileName) {
		super();
		this.sharedQueue = sharedQueue;
		this.referenceTs = referenceTs;
		this.fileName = fileName;
	}

	public static String generateURL(Map<String, String> params) {
		StringBuilder builder = new StringBuilder(BhuvanScraperConstants.DEF_URL);
		for (Entry<String, String> param : params.entrySet()) {
			System.out.println("param Name " + param.getKey() + "  value " + param.getValue());
			builder.append("&" + param.getKey() + "=" + param.getValue());
		}
		return builder.toString();
	}

	public static BhuvanData convertParsedData(String data[]) {
		BhuvanData bData = new BhuvanData();

		System.out.println("data[2]" + data[2]);
		System.out.println("data[5]" + data[5]);
		System.out.println("data[6]" + data[6]);
		System.out.println("data[9]" + data[8]);

		bData.setGridId(Integer.parseInt(data[2]));
		bData.setLongitude(Double.parseDouble(data[5]));
		bData.setLatitude(Double.parseDouble(data[6]));
		bData.setEvapotranspiration(Double.parseDouble(data[8]));
		return bData;
	}

	@Override
	public void run() {
		while (!this.sharedQueue.isEmpty()) {
			System.out.println("IsEmpty "+this.sharedQueue.isEmpty());
			String topXY = getTopXY();
			if (topXY != null) {
				String temp[] = topXY.split("#");
				int x = Integer.parseInt(temp[0]);
				int y = Integer.parseInt(temp[1]);
				Map<String, String> paramsMap = new HashMap<>();
				paramsMap.put("date", new SimpleDateFormat("YYYY-MM-dd").format(new Date(referenceTs)));
				paramsMap.put("X", String.valueOf(x));
				paramsMap.put("Y", String.valueOf(y));
				paramsMap.put("BBOX", BhuvanScraperConstants.BBOX_STRING);
				String tempURL = generateURL(paramsMap);

				try {
					System.out.println(BhuvanScraperConstants.URL_PREFIX + URLEncoder.encode(tempURL, "CP1252"));
				} catch (UnsupportedEncodingException e1) {
					// TODO
					// Auto-generated
					// catch block
					e1.printStackTrace();
				}

				try {
					Document doc2 = Jsoup
							.connect(BhuvanScraperConstants.URL_PREFIX + URLEncoder.encode(tempURL, "CP1252"))
							.timeout(BhuvanScraperConstants.CONNECT_TIMEOUT).maxBodySize(0).get();
					Element body2 = doc2.body();
					System.out.println(body2.text());
					String ar2[] = body2.text().split(" |,");
					System.out.println("ar2.length " + ar2.length);
					if (ar2 != null && ar2.length >= 5) {
						System.out.println("Convo before");
						BhuvanData bData = convertParsedData(ar2);
						System.out.println("Convo after");
						System.out.println(bData.toString());
						bData.setX(x);
						bData.setY(y);
						// Write a new student object list to the CSV file
						StringBuilder builder = new StringBuilder();
						builder.append(String.valueOf(bData.getX()));
						builder.append(BhuvanScraperConstants.COMMA_DELIMITER);
						builder.append(String.valueOf(bData.getY()));
						builder.append(BhuvanScraperConstants.COMMA_DELIMITER);
						builder.append(String.valueOf(bData.getGridId()));
						builder.append(BhuvanScraperConstants.COMMA_DELIMITER);
						builder.append(String.valueOf(bData.getLongitude()));
						builder.append(BhuvanScraperConstants.COMMA_DELIMITER);
						builder.append(String.valueOf(bData.getLatitude()));
						builder.append(BhuvanScraperConstants.COMMA_DELIMITER);
						builder.append(String.valueOf(bData.getEvapotranspiration()));
						builder.append(BhuvanScraperConstants.NEW_LINE_SEPARATOR);
						System.out.println("Appended String" + builder);
						writeToFile(builder);
					}

				} catch (SocketTimeoutException e) {
					System.out.println(" SocketTimeoutException Exception occured for X,Y values " + x + " " + y);
					e.printStackTrace();
					this.sharedQueue.add(topXY);
				} catch (UnknownHostException e) {
					System.out.println("Host not defined");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void writeToFile(StringBuilder builder) {
		FileWriter fileWriter = null;
		try {
			System.out.println("Write To file start");
			BhuvanScraperConstants.MUTEX.acquire();
			fileWriter = new FileWriter(fileName, true);
			fileWriter.append(builder.toString());
			fileWriter.flush();
			fileWriter.close();
			System.out.println("Write To file End");
			System.out.println(Thread.currentThread().getName() + "Append To CSV");
			BhuvanScraperConstants.MUTEX.release();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
*/