package com.mvc.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpTest {

	@Test
	public void test1() throws ClientProtocolException, IOException {
		String str = "{\"post_id\":\"237a24f0-ec77-4267-be73-7e061cb20658\"}";
		String url = "http://localhost:8080/revolution-web/json/api/";
		HttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		StringEntity s = new StringEntity(str, "utf-8");
		s.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		System.out.println(s.getContentLength());

		httpost.setEntity(s);
		HttpResponse httpResponse = client.execute(httpost);
		System.out.println(httpResponse.getStatusLine().getStatusCode());
		System.out.println("=====" + EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
	}

}
