package com.challenge.faire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FaireChallengeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FaireChallengeApplication.class, args[0] != null ? args[0] : "--faire_token=HQLA9307HSLQYTC24PO2G0LITTIOHS2MJC8120PVZ83HJK4KACRZJL91QB7K01NWS2TUCFXGCHQ8HVED8WNZG0KS6XRNBFRNGY71");
		context.getBean(Solution.class).execute();

	}

}
