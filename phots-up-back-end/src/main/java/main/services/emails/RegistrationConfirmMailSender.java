package main.services.emails;

import java.util.concurrent.ExecutorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegistrationConfirmMailSender implements EmailsSender {
	@Value("${spring.mail.username}")
	private String from;
	@Value("${confirm.reg.message}")
	private String message;
	
	private final ExecutorService emailsPool;
	private final JavaMailSender sender;
	
	public RegistrationConfirmMailSender(
			@Qualifier("emails") ExecutorService emailsPool, JavaMailSender sender) {
		this.emailsPool = emailsPool;
		this.sender = sender;
	}

	@Override
	public void sendEmail(String to, String confirmCode) {
		var task = SendConfirmMailTask.builder()
				.from(from)
				.message(message + confirmCode)
				.sender(sender)
				.to(to).build();
		
		this.emailsPool.submit(task);
	}
}