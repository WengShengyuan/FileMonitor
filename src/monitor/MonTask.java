package monitor;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import app.Params;

public class MonTask {
	Timer timer ;
	
	public void start() {
		timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// schedule task
				try {
					Monitor monitor = new Monitor();
					monitor.scan();
					monitor.compare();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}, 0, Params.interval);
	}

	public void stop() {
		
		timer.cancel();
	}
}
