package upload.thread;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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

public class UploadThread extends ConfigurableThread {

	private FileWrapper wrapper;
	private String url;
	private String flag = "";

	public UploadThread(FileWrapper wrapper, String url, String flag) {
		this.wrapper = wrapper;
		this.flag = flag;
		this.url = url;
	}

	@Override
	protected void doRun() throws Exception{
			
			System.out.println("start uploading "+this.wrapper);
			CloseableHttpClient client = HttpClients.createDefault();
			File file = new File(this.wrapper.getPath());
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

			HttpEntity re = response.getEntity();
			int code = response.getStatusLine().getStatusCode();
			String r = "";
			if (re != null) {
				r = EntityUtils.toString(re);
				EntityUtils.consume(re);
			}

			if (code == HttpStatus.SC_OK && "success".equals(r)) {
				System.out.println(file.getName() + " 上传成功");
				response.close();
				if(this.flag.equals(Params.FLAG_ADD)){
					SingletonLists.getInstance().unregisterAdd(this.wrapper);
				} else {
					SingletonLists.getInstance().unregisterModify(this.wrapper);
				}
				successFlag = true;
			} else {
				System.out.println("上传失败 ：状态码[" + code + "], 回复:" + r);
			}
	}

}
