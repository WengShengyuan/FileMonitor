package monitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import app.Params;
import app.SingletonLists;

public class Persister {
	List<String> strings = new ArrayList<String>();
	List<FileWrapper> wrappers = new ArrayList<FileWrapper>();
	
	public List<FileWrapper> read() throws FileNotFoundException, IOException{
		File f = new File(Params.tempFile);
		if(!f.exists())
			return wrappers;
		FileReader reader = new FileReader(Params.tempFile);
		strings  = IOUtils.readLines(reader);
		for(String s : strings){
			FileWrapper wrapper = new FileWrapper(s);
			wrappers.add(wrapper);
		}
		reader.close();
		return wrappers;
	}
	
	
	public void write() throws IOException{
		
		FileWriter writer = new FileWriter(Params.tempFile, false);
		while(!SingletonLists.getInstance().getAddUploaded().empty()){
			FileWrapper w = SingletonLists.getInstance().getAddUploaded().pop();
			System.out.println("add "+w +" to persist");
			wrappers.add(w);
		}
		
		while(!SingletonLists.getInstance().getModifyUploaded().empty()){
			FileWrapper w = SingletonLists.getInstance().getAddUploaded().pop();
			for(FileWrapper item : wrappers){
				if(item.getPath().equals(w.getPath())){
					item = w;
					System.out.println("mod "+w +" to persist");
					break;
				}
			}
		}
		
		for(FileWrapper f : wrappers){
			String str = f.toString();
			IOUtils.write(str+"\n", writer);
		}
		writer.close();
	}
	
}
