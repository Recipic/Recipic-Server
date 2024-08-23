package CaffeineCoder.recipic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class RecipicApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipicApplication.class, args);
	}

}
