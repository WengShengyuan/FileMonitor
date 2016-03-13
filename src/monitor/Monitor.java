package monitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.Params;
import upload.Uploader;

public class Monitor {
	Persister per = new Persister();
	Uploader upload = new Uploader();
	
	List<FileWrapper> scanFile = new ArrayList<FileWrapper>();
	List<FileWrapper> readFile = new ArrayList<FileWrapper>();
	
	List<FileWrapper> deletedFile = new ArrayList<FileWrapper>();
	List<FileWrapper> modifiedFile = new ArrayList<FileWrapper>();
	List<FileWrapper> addedFile = new ArrayList<FileWrapper>();
	
	
	public void scan() throws IOException{
		System.out.println("scanning...");
		File folder = new File(Params.scanFolderPath);
		File[] fs = folder.listFiles();
		for(File f : fs){
			scanFile.add(new FileWrapper(f));
		}
	}
	
	public void compare() throws FileNotFoundException, IOException{
		try {
			System.out.println("reading...");
			readFile = per.read();
			System.out.println("writing...");
			per.write();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		getModified();
		getAdded();
		
		upload.upload(modifiedFile, addedFile);
	}
	
	private void getModified(){
		for(FileWrapper s : scanFile){
			for(FileWrapper r : readFile){
				if(s.getPath().equals(r.getPath()) && !s.getMD5().equals(r.getMD5())){
					modifiedFile.add(s);
				}
			}
		}
	}
	
	private void getAdded(){
		for(FileWrapper s : scanFile){
			
			boolean founded = false;
			for(FileWrapper r : readFile){
				
				if(s.getPath().equals(r.getPath())){
					founded = true;
					break;
				}
			}
			if(!founded){
				addedFile.add(s);
			}
		}
	}
	
}
