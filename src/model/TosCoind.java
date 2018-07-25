package model;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TosCoind {
	protected final static String username = "TosCoinrpc";
	protected final static String pass = "2DRZQYRkDwNYtePLpj9uNfdpQLsTrGZWb8vS6du8TKsm";
	protected final static String url = "http://52.79.202.124:24887/";
	protected final static String author = username + ":" + pass;
	
	protected HttpResponse executeHttpRequest(String requestType, String url, ByteArrayEntity entity) {
		requestType = requestType.toUpperCase();
		HttpResponse httpResponse = null;
		CloseableHttpClient port = HttpClients.createDefault();

		if (entity != null) {
			entity.setContentType("application/json");
		}
		try {
			switch (requestType) {
			case "POST":
				HttpPost post = new HttpPost(url);
				String encoding = Base64.encodeBase64String(author.getBytes());
				post.setHeader("Authorization", "Basic " + encoding);
				post.addHeader("Content-Type", "application/json");
				post.setEntity(entity);
				httpResponse = port.execute(post);
				break;
			default:
				throw new MethodNotSupportedException(requestType.toString());
			}
		} catch (IOException | MethodNotSupportedException ex) {
			ex.printStackTrace();
		}
		return httpResponse;
	}
}
