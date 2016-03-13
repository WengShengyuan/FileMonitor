package monitor;

import java.io.File;
import java.io.FileNotFoundException;

import util.MD5Util;

public class FileWrapper {
	
	String path;
	String MD5;
	
	public FileWrapper(String str){
		String[] strs = str.split("#####");
		this.path = strs[0];
		this.MD5 = strs[1];
	}
	
	@Override
	public String toString(){
		return this.path+"#####"+this.MD5;
	}
	
	public FileWrapper(File file) {
		this.path = file.getPath();
		try {
			this.MD5 = MD5Util.getMd5ByFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public FileWrapper(){}
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMD5() {
		return MD5;
	}
	public void setMD5(String mD5) {
		MD5 = mD5;
	}
	
	

}
