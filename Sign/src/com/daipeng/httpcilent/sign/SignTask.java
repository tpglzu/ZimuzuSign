package com.daipeng.httpcilent.sign;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.daipeng.httpcilent.Exception.SignerException;
import com.daipeng.httpcilent.common.Application;
import com.daipeng.httpcilent.common.ImmutableValues;

public class SignTask {
	
	private String userName = "";
	
	private String userPassword = "";
	
	private static Logger logger = Logger.getLogger(SignTask.class);
	
	
	public void startSign(){
		
		logger.info("****** sign start ******");
		
		CloseableHttpClient client = null;
		try {
			//Do Login 
			client = startLogin();

			//Open Sign Page
			openSignPage(client);
			
			//Do Sign
			doSign(client);
			
		} catch (Exception e) {
			logger.error("****** sign failure ******",e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private CloseableHttpClient startLogin() throws SignerException{
		
		logger.info("login start ...");
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(Application.URL_DOLOGIN);
		List<NameValuePair> parameterMap = new ArrayList<NameValuePair>();
		parameterMap.add(new BasicNameValuePair(Application.LOGIN_USER_NAME_KEY,userName));
		parameterMap.add(new BasicNameValuePair(Application.LOGIN_USER_PASSWORD_KEY,userPassword));

		HttpResponse httpResponse = null;
		try {
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(parameterMap, "UTF-8");
			httpPost.setEntity(postEntity);
			httpResponse = client.execute(httpPost);
			Map<String,Object> responseData = getResponseJSON(httpResponse);
			if(checkStatusWtihJson(responseData)){
				logger.info("login success ...");
			}else{
				throw new SignerException("login failure ...msg : " + responseData.get("info"));
			}
		} catch (Exception e) {
			throw new SignerException("login exception ", e);
		}
		
		logger.info("login end ...");
		
		return client;
		
	}
	
	private void openSignPage(CloseableHttpClient client) throws SignerException {
		
		logger.info("open sign page start ...");
		
		HttpGet httpGet = new HttpGet(Application.URL_SIGNPAGE);
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			String responseData = getResponseString(httpResponse);
			if(responseData.contains("<title>签到</title>")){
				logger.info("open sign page success ...");
			}else{
				throw new SignerException("open sign page failure ...");
			}
			
			Thread.sleep(ImmutableValues.WAIT_TIME);
			
		} catch (Exception e) {
			throw new SignerException("open sign page failure", e);
		}
		
		logger.info("open sign page end ...");
	}
	
	private void doSign(CloseableHttpClient client) throws SignerException {
		
		logger.info("doSign start ...");
		
		HttpGet httpGet = new HttpGet(Application.URL_DOSIGN);
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			Map<String,Object> responseData = getResponseJSON(httpResponse);
			if(checkStatusWtihJson(responseData)){
				logger.info("doSign success ...");
			}else{
				throw new SignerException("doSign failure ...error code : "
						+ responseData.get(ImmutableValues.AJAX_STATUS) + ",error msg : "
						+ dispUnicodeStr((String) responseData.get("info")));
			}
		}catch (Exception e) {
			throw new SignerException("doSign failure ...",e);
		}
		
		logger.info("doSign end ...");
	}
	
	private static String getResponseString(HttpResponse httpResponse) throws SignerException{
		
		String responseString = null; 
		
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		int status = httpResponse.getStatusLine().getStatusCode();
		if(ImmutableValues.HTTP_STATUS_OK != status){
			throw new SignerException("Http Response Error ... ");
		}
		
		try {
			// 判断响应实体是否为空
			if (entity != null) {
				responseString = EntityUtils.toString(entity);
			}
		} catch (ParseException | IOException e) {
			throw new SignerException("Response Message Reader Error ... ");
		}
		
		return responseString;
	}
	
	private static Map<String,Object> getResponseJSON(HttpResponse httpResponse) throws SignerException{
		
		String responseString = getResponseString(httpResponse); 
		
		Map<String,Object> responseJSON = JSON.decode(responseString);
		
		return responseJSON;
	}
	
	private static boolean checkStatusWtihJson(Map<String,Object> responseJSON){
		BigDecimal responseStatus = (BigDecimal) responseJSON.get(ImmutableValues.AJAX_STATUS);
		return responseStatus.longValue() == ImmutableValues.AJAX_STATUS_OK;
	}
	
	public String dispUnicodeStr(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		
		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}
		
		return sb.toString();
	}

	/**
	 * @param userName セットする userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param userPassword セットする userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public static void main(String[] args) {
		
		String userNameConfig = Application.LOGIN_USER_NAME_VALUE;
		String userPassworConfig = Application.LOGIN_USER_PASSWORD_VALUE;

		if(userNameConfig == null || userNameConfig.length() == 0 
				|| userPassworConfig == null || userPassworConfig.length() == 0){
			logger.error("user name and password not config ...");
		}
		
		String userNames[] = userNameConfig.split(",");
		String userPwds[] =  userPassworConfig.split(",");
		if(userNames.length != userPwds.length){
			logger.error("user name and password's config is wrong...username : " + userNameConfig + ", password : " + userPassworConfig);
		}
		
		
		for (int i = 0; i < userPwds.length; i++) {
			logger.info("do sign for user : " + userNames[i]);
			System.out.println("do sign for user : " + userNames[i]);
			SignTask signTask = new SignTask();
			signTask.setUserName(userNames[i]);
			signTask.setUserPassword(userPwds[i]);
			signTask.startSign();
			
		}
		
	}
}
