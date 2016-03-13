package upload;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import app.Params;
import app.SingletonLists;
import monitor.FileWrapper;
import upload.thread.UploadThread;

public class Uploader {

	public void upload(List<FileWrapper> modified, List<FileWrapper> added) {

//		System.out.println("------modified--------");
		for (FileWrapper m : modified) {
			try {
//				SingletonLists.getInstance().registerModify(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println(m);
		}

		for (FileWrapper a : added) {
			try {
				SingletonLists.getInstance().registerAdd(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println(a);
		}
	}

	private void doUpload(String filePath) throws Exception {
		CloseableHttpClient client =HttpClients.createDefault();
		File file = new File(filePath);
		HttpPost post = new HttpPost(Params.uploadUrl);
		FileBody fileBody = new FileBody(file);
		StringBody stringBody1 = new StringBody(file.getName(), ContentType.MULTIPART_FORM_DATA);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(Charset.forName(HTTP.UTF_8));
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", fileBody);
		builder.addPart("name", stringBody1);
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		CloseableHttpResponse response = client.execute(post);
		
		try {  
            HttpEntity re = response.getEntity();  
            int code = response.getStatusLine().getStatusCode();  
            String r = "";
            if (re != null) {  
                r = EntityUtils.toString(re);  
                EntityUtils.consume(re);  
            }
            
            if(code == HttpStatus.SC_OK && "success".equals(r)){
            	System.out.println("上传成功");
            } else {
            	System.out.println("上传失败 ：状态码["+ code+"], 回复:"+r);
            }
            
        } catch(Exception e){
        	System.out.println("err:"+e);
        } finally {  
            response.close();  
        }  
		
		
		
	}
	
	/* public static void main(String[] args) {
		Uploader o = new Uploader();
		try {
			Params.uploadUrl="http://localhost:8080/WebPage/upload";
			o.doUpload("C://test//a.docx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

}
