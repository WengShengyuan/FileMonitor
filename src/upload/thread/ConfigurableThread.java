package upload.thread;

import java.util.Date;

public class ConfigurableThread extends Thread{
	
	private boolean firstExe = false;
	private boolean enableTimeLimit = false;
	private boolean enableExeLimit = false;
	private Date firstExeTime;
	private Long timeInterval=1000L;
	private Integer exeLimit = 0;
	private Long timeLimit = 0L;
	
	protected Integer tryCount = 0;
	protected boolean successFlag = false;
 	
	@Override
	public void run() {
		if(!firstExe){
			this.firstExe = true;
			this.firstExeTime = new Date();
		}
		
		try{
			while(execuable()){
				doRun();
				tryCount ++;
				Thread.sleep(this.timeInterval);
			}
			return ;
		} catch(Exception e){
			onError(e);
		}
		
	}
	
	protected void doRun() throws Exception {
		throw new Exception("not override yet");
	}
	
	private boolean execuable(){
		if(enableTimeLimit && (new Date().getTime() - this.firstExeTime.getTime()) > this.timeLimit){
			onTimeout();
			return false;
		}
		if(enableExeLimit && this.tryCount > this.exeLimit){
			onTryout();
			return false;
		}
		if(successFlag){
			onSuccess();
			return false;
		}
		return true;
	}
	
	protected void onTimeout(){
		System.out.println("time out");
	}
	
	protected void onTryout(){
		System.out.println("try out");
	}
	
	protected void onSuccess(){
		System.out.println("success");
	}
	
	protected void onError(Exception e){
		System.out.println("Error:"+e);
	}
	
	public final ConfigurableThread enableExeLimit(){
		this.enableExeLimit = true;
		return this;
	}
	
	public final ConfigurableThread disableExeLimit(){
		this.enableExeLimit = false;
		return this;
	}
	
	public final ConfigurableThread enableTimeLimit(){
		this.enableTimeLimit = true;
		return this;
	}

	public final ConfigurableThread disableTimeLimit(){
		this.enableTimeLimit = false;
		return this;
	}
	
	public final ConfigurableThread setExeLimit(int limit){
		this.exeLimit = limit;
		return this;
	}
	
	public final ConfigurableThread setTimeLimit(Long timeLimit){
		this.timeLimit = timeLimit;
		return this;
	}
	
	public final ConfigurableThread setInterval(Long interval){
		this.timeInterval = interval;
		return this;
	}
	
}
