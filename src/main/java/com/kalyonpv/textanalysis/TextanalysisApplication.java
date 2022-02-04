package com.kalyonpv.textanalysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class TextanalysisApplication implements CommandLineRunner {

	@Value("${path: No environment parameter set  }")
	private String path;

	public static void main(String[] args) {
		SpringApplication.run(TextanalysisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(path);
	}
}
