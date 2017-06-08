/*package com.vassarlabs.bhuvan.scraper;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BhuvanRunnerClass {
	public static void main(String args[]) {
		Queue<String> queue = new LinkedList<String>();
		// 14/11/2016, 00:00:00
		int t=0;
		long referenceTs = 1479673922000l;
		for (int x = BhuvanScraperConstants.LEFT_X; x <= BhuvanScraperConstants.RIGHT_X; x += BhuvanScraperConstants.X_DIFF) {
			for (int y = BhuvanScraperConstants.TOP_Y; y <= BhuvanScraperConstants.BOTTOM_Y; y += BhuvanScraperConstants.Y_DIFF) {
				queue.add(String.valueOf(x) + "#" + String.valueOf(y));
				t++;
			}
		}
		System.out.println("Total Count "+t);
		String fileName = System.getProperty("user.home") + "/CodeBaseGit/files/bhuvan/bhuvan-all-Service.csv";
		ExecutorService executors = Executors.newFixedThreadPool(BhuvanScraperConstants.NUM_OF_THREADS);
		for (int i = 0; i < BhuvanScraperConstants.NUM_OF_THREADS; i++) {
			BhuvanLocationThread thread = new BhuvanLocationThread(queue, referenceTs, fileName);
			executors.submit(thread);
		}
		executors.shutdown();
	}
}
*/