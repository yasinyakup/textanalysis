package com.kalyonpv.textanalysis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class TextanalysisApplicationTests {

	private static final String path = "http://10.11.11.30:50000/XMII/Runner?Transaction=CETC_MES/SAP/KEPCLNT100/MES_GET_ORDER&IllumLoginName=MESUSER&IllumLoginPassword=Kalyon20!&I_First_Date=2022-02-01&I_Second_Date=2022-02-10&OutputParameter=OutputXml";


	@Test
	void contextLoads() {
	}

	@Test
	public void test1() {
		URL url = null;
		BufferedReader br;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = httpURLConnection.getInputStream();
			br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			System.out.println(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test2() {
		XmlMapper xmlMapper = new XmlMapper();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest;
		HttpResponse<String> httpResponse;
		URI uri = URI.create(path);

		httpRequest = HttpRequest.newBuilder()
				.uri(uri)
				.version(HttpClient.Version.HTTP_1_1)
				.setHeader("IllumLoginName", "MESUSER")
				.setHeader("IllumLoginPassword", "Kalyon20!")
//				.header("Accept", "application/xml")
				.build();
		try {
			httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			System.out.println("Response status code: " + httpResponse.statusCode());
			JsonNode node = xmlMapper.readTree(httpResponse.body());
//			Object object = xmlMapper.readValue(httpResponse.body(), Object.class);
			System.out.println(node.get("ET_COMPONENT").get("item").get(0));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

	@Test
	public void test2Async() {
		XmlMapper xmlMapper = new XmlMapper();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest;
		HttpResponse<String> httpResponse;
		URI uri = URI.create(path);

		httpRequest = HttpRequest.newBuilder()
				.uri(uri)
				.version(HttpClient.Version.HTTP_1_1)
				.build();

		var cf = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
				.thenApply(response -> response.body())
				.thenAccept(body -> {
					System.out.println("test");
					try {
						TimeUnit.SECONDS.sleep(3);
						JsonNode node = xmlMapper.readTree(body);
						System.out.println(node.get("ET_COMPONENT").get("item").get(0));
					} catch (JsonProcessingException | InterruptedException e) {
						e.printStackTrace();
					}

				});

		System.out.println("this is first printed, because of async request");
		cf.join();

	}
}
