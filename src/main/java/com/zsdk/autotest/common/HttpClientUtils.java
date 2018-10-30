package com.zsdk.autotest.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class HttpClientUtils {

	public String doGetRequest() throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//创建一个uri对象
		URIBuilder uriBuilder = new URIBuilder("http://www.sogou.com/web");
		uriBuilder.addParameter("query","花千骨");
		HttpGet get = new HttpGet(uriBuilder.build());

		//执行请求
		CloseableHttpResponse response =httpClient.execute(get);
		//取响应的结果
		int statusCode =response.getStatusLine().getStatusCode();
		System.out.println(statusCode);

		HttpEntity entity =response.getEntity();
		String respStr = EntityUtils.toString(entity,"utf-8");

//		System.out.println(respStr);
		//关闭httpclient
		response.close();
		httpClient.close();
		
		return respStr;
	}
	
	
	public String doPostRequst(String url, JSONObject requestData) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient= HttpClients.createDefault();
		HttpPost post = new HttpPost(url);

		//包装成一个Entity对象
		StringEntity entity = new StringEntity(requestData.toString(),"utf-8");

		//设置请求的内容
		post.setEntity(entity);

		//执行post请求
		CloseableHttpResponse response = httpClient.execute(post);
		String respStr = EntityUtils.toString(response.getEntity(),"utf-8");

		System.out.println(respStr);
		response.close();
		httpClient.close();
		
		return respStr;
	}
	
	public String doPostRequst(String url, JSONObject requestData, HashMap<String, String> header) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient= HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		//Userid: 7f0pRahZRLc 添加消息头
		for (Map.Entry<String, String> entry : header.entrySet()) {
			post.addHeader(entry.getKey(),entry.getValue());
		}
		
		//包装成一个Entity对象
		StringEntity entity = new StringEntity(requestData.toString(),"utf-8");
		post.setEntity(entity);

		//执行post请求
		CloseableHttpResponse response = httpClient.execute(post);
		String respStr = EntityUtils.toString(response.getEntity(),"utf-8");

		response.close();
		httpClient.close();
		
		return respStr;
	}
	
}