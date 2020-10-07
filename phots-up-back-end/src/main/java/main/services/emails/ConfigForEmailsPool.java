package main.services.emails;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigForEmailsPool {

	@Bean(name = "emails") public ExecutorService emails() {
		return Executors.newSingleThreadExecutor();
	}
}
