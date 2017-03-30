package com.nearnia.encouragement.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {
	static String response = null;
	public static int GET = 1;
	public static int POST = 2;

	public ServiceHandler() {

	}

	public String toUploadImage(String url, String Path) {
		return this.toUploadImage1(url, Path);
	}

	private String toUploadImage1(String url, String path) {

		String response1 = null;
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(url);
		File file = new File(path);
		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "image/jpeg");
		mpEntity.addPart("userfile", cbFile);
		httppost.setEntity(mpEntity);
		
		System.out.println("executing request " + httppost.getRequestLine());
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			HttpEntity resEntity = response.getEntity();
			response1 = EntityUtils.toString(resEntity);
		} catch (org.apache.http.ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response1;

	}
	
	public String toUploadCoverImage(String url, String Path,String userid) {
		return this.toUploadCoverImage1(url, Path, userid);
	}
	
	private String toUploadCoverImage1(String url, String path,String userid) {

		String response1 = null;
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(url);
		File file = new File(path);
		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "image/jpeg");
		mpEntity.addPart("userfile", cbFile);
		try {
			mpEntity.addPart("userid", new StringBody(userid));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httppost.setEntity(mpEntity);
		
		System.out.println("executing request " + httppost.getRequestLine());
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			HttpEntity resEntity = response.getEntity();
			response1 = EntityUtils.toString(resEntity);
		} catch (org.apache.http.ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response1;

	}

	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	public String makeServiceCall(String url, int method, List<NameValuePair> params) {
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				// adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}

				httpResponse = httpClient.execute(httpPost);
			} else if (method == GET) {
				if (params != null) {
					String paramString = URLEncodedUtils.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				httpResponse = httpClient.execute(httpGet);
			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
}
