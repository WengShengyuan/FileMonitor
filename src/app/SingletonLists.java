package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import monitor.FileWrapper;
import upload.thread.UploadThread;

public class SingletonLists {
	
	private volatile static SingletonLists instance;
	
	private SingletonLists(){
		this.addToUpload = new ArrayList<FileWrapper>();
		this.modifyToUpload = new ArrayList<FileWrapper>();
		this.addUploaded = new Stack<FileWrapper>();
		this.modifyUploaded = new Stack<FileWrapper>();
	}
	
	public static SingletonLists getInstance(){
		if(instance == null){
			synchronized(SingletonLists.class){
				if(instance == null){
					instance = new SingletonLists();
				}
			}
		}
		return instance;
	}
	
	private List<FileWrapper> addToUpload;
	private List<FileWrapper> modifyToUpload;
	
	private Stack<FileWrapper> addUploaded;
	private Stack<FileWrapper> modifyUploaded;
	
	public void registerAdd(FileWrapper f){
		synchronized (this) {
			if(!this.containAdd(f)){
				System.out.println("registering add :"+f);
				this.addToUpload.add(f);
				new UploadThread(f, Params.uploadUrl, Params.FLAG_ADD).enableExeLimit().setExeLimit(3).setInterval(1000L).start();
			}
		}
	}
	
	public void registerModify(FileWrapper f){
		synchronized (this) {
			if(!this.containModify(f)){
				System.out.println("registering mod :"+f);
				this.modifyToUpload.add(f);
				new UploadThread(f, Params.uploadUrl, Params.FLAG_MOD).enableExeLimit().setExeLimit(3).setInterval(1000L).start();
			}
				
		}
	}
	
	
	
	public void unregisterAdd(FileWrapper f) throws Exception{
		synchronized (this) {
			int index = -1;
			for (int i = 0; i < this.addToUpload.size(); i++) {
				FileWrapper item = this.addToUpload.get(i);
				if (item.getPath().equals(f.getPath()) && item.getMD5().equals(f.getMD5())) {
					index = i;
					break;
				}
			}
			if (index < 0)
				throw new Exception("item not found");
			
			System.out.println("unregistering add "+f);
			this.addToUpload.remove(index);
			this.addUploaded.push(f);
		}
	}
	
	public void unregisterModify(FileWrapper f) throws Exception{
		synchronized (this) {
			int index = -1;
			for (int i = 0; i < this.modifyToUpload.size(); i++) {
				FileWrapper item = this.modifyToUpload.get(i);
				if (item.getPath().equals(f.getPath()) && item.getMD5().equals(f.getMD5())) {
					index = i;
					break;
				}
			}
			if (index < 0)
				throw new Exception("item not found");
			
			System.out.println("unregistering mod "+f);
			this.modifyToUpload.remove(index);
			this.modifyUploaded.push(f);
		}
	}
	
	
	
	
	private boolean containAdd(FileWrapper f){
		synchronized(this){
			for(FileWrapper item : this.addToUpload){
				if(item.getPath().equals(f.getPath()))
					return true;
			}
			return false;
		}
	}
	
	private boolean containModify(FileWrapper f){
		synchronized(this){
			for(FileWrapper item : this.modifyToUpload){
				if(item.getPath().equals(f.getPath()))
					return true;
			}
			
			return false;
		}
	}

	public List<FileWrapper> getAddToUpload() {
		return addToUpload;
	}
	public List<FileWrapper> getModifyToUpload() {
		return modifyToUpload;
	}
	public Stack<FileWrapper> getAddUploaded() {
		return addUploaded;
	}
	public Stack<FileWrapper> getModifyUploaded() {
		return modifyUploaded;
	}

}
