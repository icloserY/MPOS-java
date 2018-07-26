package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

public class TosCoindApi extends TosCoind {
	private static TosCoindApi instance = new TosCoindApi();
	public static TosCoindApi getInstance() {
		return instance;
	}
	public String getaccount(String params){
		String method = "getaccount";
		String id = "1";
		String jsonrpc = "1.0";
		String result = null;
		//params = "[\"TigPuBqMiAY5MVrY3nswQzfKcNs8NYZEhb\"]";
		String entitys = "{ \"jsonrpc\" : " + "\"" + jsonrpc + "\"," +
							" \"id\" : " + id + "," +
							" \"method\" : " + "\"" + method + "\" ";
		if(params == null) {
			entitys += "}";
		}else {
			entitys += ", \"params\" : " + params + "}";
		}
		byte[] entityArray = entitys.getBytes();
		HttpResponse response = this.executeHttpRequest("post", url,
				new ByteArrayEntity(entityArray, ContentType.APPLICATION_JSON));

		System.out.println(response);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedOperationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String inputLine;
		StringBuffer stringresponse = new StringBuffer();
		if(reader == null) {
			return null;
		}
		try {
			while ((inputLine = reader.readLine()) != null) {
				stringresponse.append(inputLine);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(stringresponse.toString());

		JSONObject obj = new JSONObject(stringresponse.toString());
		try {
			if (obj.get("error").toString().equals("null"))
				result = obj.getString("result");
		} catch(NullPointerException | NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}
}
