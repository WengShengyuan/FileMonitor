package app;

import monitor.MonTask;

public class App {

	public static void main(String[] args) {
		
		Params.interval = 5*100L;
		Params.scanFolderPath="C:\\test";
		Params.uploadUrl="http://192.168.1.110:8080/WebPage/upload";
		Params.tempFile = "temp.txt";
		
		MonTask task = new MonTask();
		task.start();
	}

}
