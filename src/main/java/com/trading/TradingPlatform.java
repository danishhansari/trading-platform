package com.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

@EnableKafka
@EnableRetry
@SpringBootApplication
public class TradingPlatform {

	public static void main(String[] args) {
		SpringApplication.run(TradingPlatform.class, args);
	}

}
