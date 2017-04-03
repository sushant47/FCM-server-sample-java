package com.sushant.android.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sushant.android.dto.Contract;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8080")
@RequestMapping(ContractController.CONTRACT_BASE_URI)
public class ContractController {

	public static final String CONTRACT_BASE_URI = "svc/v1/contracts";
	public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	public static final String FCM_SERVER_API_KEY = "Enter your server api key here";
	private static String deviceRegistrationId = "Enter device registeration ID here";

	@RequestMapping(value = "{token}") //here token is device registeration ID (deviceRegistrationId). It can be called from client code.
	public Contract getContract(@PathVariable final String token) {
		 Contract contract = new Contract();
		 contract.setName("philip");
		 contract.setId(token);
		 deviceRegistrationId = token;

		 
			    String result = "";
			    URL url;
				try {
					url = new URL(FCM_URL);
				
			    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			    conn.setUseCaches(false);
			    conn.setDoInput(true);
			    conn.setDoOutput(true);

			    conn.setRequestMethod("POST");
			    conn.setRequestProperty("Authorization", "key=" + FCM_SERVER_API_KEY);
			    conn.setRequestProperty("Content-Type", "application/json");

			    JSONObject json = new JSONObject();

			    json.put("to", deviceRegistrationId.trim());
			    JSONObject info = new JSONObject();
			    info.put("title", "notification title"); // Notification title
			    info.put("body", "message body"); // Notification
			                                                            // body
			    json.put("notification", info);
			    
			    
			        OutputStreamWriter wr = new OutputStreamWriter(
			                conn.getOutputStream());
			        wr.write(json.toString());
			        wr.flush();

			        BufferedReader br = new BufferedReader(new InputStreamReader(
			                (conn.getInputStream())));

			        String output;
			        System.out.println("Output from Server .... \n");
			        while ((output = br.readLine()) != null) {
			            System.out.println(output);
			        }
			        result = "SUCCESS";
			    } catch (Exception e) {
			        e.printStackTrace();
			        result = "FAILURE";
			    }
			    System.out.println("GCM Notification is sent successfully");

			    

		return contract;
	}

	public static byte[] getPostData(String registrationId) throws JSONException {
		HashMap<String, String> dataMap = new HashMap<>();
		JSONObject payloadObject = new JSONObject();

		dataMap.put("name", "Aniket!");
		dataMap.put("country", "India");

		JSONObject data = new JSONObject(dataMap);
		;
		payloadObject.put("data", data);
		payloadObject.put("to", registrationId);

		return payloadObject.toString().getBytes();
	}

	public static String convertStreamToString(InputStream inStream) throws Exception {
		InputStreamReader inputStream = new InputStreamReader(inStream);
		BufferedReader bReader = new BufferedReader(inputStream);

		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = bReader.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}
}
