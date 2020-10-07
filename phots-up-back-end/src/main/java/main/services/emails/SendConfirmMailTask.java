package main.services.emails;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.Builder;

@Builder
public class SendConfirmMailTask implements Runnable{
	private final JavaMailSender sender;
	private final String message;
	private final String from;
	private final String to;
	
	@Override
	public void run() {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(from);
		smm.setTo(to);
		smm.setSubject("Registration confirm");
		smm.setText(message);
		this.sender.send(smm);
	}	
}
