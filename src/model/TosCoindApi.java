package model;

import java.io.*;

import org.apache.http.*;
import org.apache.http.entity.*;
import org.json.*;

public class TosCoindApi extends TosCoind {

	public void getinfo() throws Throwable {
		String entity = "{\"method\":\"getinfo\",\"id\":1,\"jsonrpc\":\"1.0\", params:[10,\"20\"]}";
		byte[] entityArray = entity.getBytes();
		HttpResponse response = this.executeHttpRequest("post", url,
				new ByteArrayEntity(entityArray, ContentType.APPLICATION_JSON));

		System.out.println(response);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));

		String inputLine;
		StringBuffer stringresponse = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			stringresponse.append(inputLine);
		}

		reader.close();

		System.out.println(stringresponse.toString());

		JSONObject obj = new JSONObject(stringresponse.toString());
		if (obj.get("error") == null)
			return;
		// parsing and print
		System.out.println(obj.getDouble("result"));
		// get the first result
		/*
		JSONObject res = obj.getJSONArray("results").getJSONObject(0);
		System.out.println(res.getString("formatted_address"));
		JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
		System.out.println("lat: " + loc.getDouble("lat") + ", lng: " + loc.getDouble("lng"));
		*/
	}
}
