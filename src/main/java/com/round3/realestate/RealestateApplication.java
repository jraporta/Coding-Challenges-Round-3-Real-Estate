package com.round3.realestate;

import com.round3.realestate.service.ScrapeService;
import com.round3.realestate.service.ScrapeServiceJSoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RealestateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealestateApplication.class, args);
	}

	@Bean
	public ScrapeService scrapeService() {
		return new ScrapeServiceJSoup();
	}

}
