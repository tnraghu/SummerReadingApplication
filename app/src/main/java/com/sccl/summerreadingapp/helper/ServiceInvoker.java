package com.sccl.summerreadingapp.helper;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;
 
public class ServiceInvoker {

	public final static int GET = 1;
	public final static int POST = 2;
	public final static int PUT = 3;

	public ServiceInvoker() {
	}

	public String invoke(String url, int method) {
		return this.invoke(url, method, null);
	}

	public String invoke(String url, int method, List<NameValuePair> params) {
		String response = null;
		try {
			HttpResponse httpResponse = null;

			if (method == POST) {
				httpResponse = handlePost(url, params);
			} else if (method == PUT) {
				httpResponse = handlePut(url, params);
			} else if (method == GET) {
				httpResponse = handleGet(url, params);
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			Log.e("error", e.getMessage());
		}

		return response;

	}

	public HttpResponse handleGet(String url, List<NameValuePair> params) 
			throws IOException, ClientProtocolException {
		if (params != null) {
			String paramString = URLEncodedUtils.format(params, "utf-8");
			url += "?" + paramString;
		}
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Content-Type", "application/json"); 
		httpGet.setHeader("Accept", "application/json");

		return new DefaultHttpClient().execute(httpGet);
	}

	public HttpResponse handlePut(String url, List<NameValuePair> params) 
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		HttpPut HttpPut = new HttpPut(url);
		// adding post params
		if (params != null) {
			HttpPut.setEntity(new UrlEncodedFormEntity(params));
		}

		return new DefaultHttpClient().execute(HttpPut);
	}

	public HttpResponse handlePost(String url, List<NameValuePair> params)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		HttpPost httpPost = new HttpPost(url);
		if (params != null) {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		}

		return new DefaultHttpClient().execute(httpPost);
	}

	public String invokeJson(String url, int method, JSONObject jsonObject) {
		String response = null;
		try {
			HttpResponse httpResponse = null;

			if (method == POST) {
				httpResponse = handleJsonPost(url, jsonObject);
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
		}

		return response;

	}

	public HttpResponse handleJsonPost(String url, JSONObject jsonObject)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json"); 
		httpPost.setHeader("Accept", "application/json");
		
		if (jsonObject != null) {
			StringEntity se = new StringEntity(jsonObject.toString());
			// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(se);
		}

		return new DefaultHttpClient().execute(httpPost);
	}
}