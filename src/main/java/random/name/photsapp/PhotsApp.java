package random.name.photsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class PhotsApp {

	public static void main(String[] args) {
		SpringApplication.run(PhotsApp.class, args);
	}

}