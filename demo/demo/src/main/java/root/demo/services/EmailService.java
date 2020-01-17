package root.demo.services;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.FormSubmissionDto;
import root.demo.repository.UserRepository;

@Service
public class EmailService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String email = (String) execution.getVariable("emailKorisnika");
		
		root.demo.model.User korisnik = userRepository.findByEmail(email);	
		       // smtpMailSender.send(userDTO.getEmail(), "Test mail from Spring", "http://localhost:4200/auth/confirmation"+userDTO.getId());

		        final String username = "isaprojekat.ftn@gmail.com";
		        final String password = "ISAOdbrana";

		        Properties props = new Properties();
		        props.put("mail.smtp.debug", "true");
		        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		        props.put("mail.smtp.auth", true);
		        props.put("mail.smtp.starttls.enable", true);
		        props.put("mail.smtp.host", "smtp.gmail.com");
		        props.put("mail.smtp.port", "587");
		        
		        /*mail.smtp.host=smtp.gmail.com, mail.smtp.port=25, mail.smtp.auth=true mail.smtp.starttls.enable=true*/


		        Session session = Session.getInstance(props,
		                new javax.mail.Authenticator() {
		                    protected PasswordAuthentication getPasswordAuthentication() {
		                        return new PasswordAuthentication(username, password);
		                    }
		                });
		        session.setDebug(true);

		        try {

		            Message message = new MimeMessage(session);
		            message.setFrom(new
		                    InternetAddress("isaprojekat.ftn@gmail.com"));
		            message.setRecipients(Message.RecipientType.TO,
		                    InternetAddress.parse(korisnik.getEmail()));
		            message.setSubject("Confirm password");
		            message.setText("Postovani,"
		                    + "\n\n Kliknite na link ispod za verifikaciju naloga!"+"\n \n http://localhost:4200/verification/"+korisnik.getId());

		            Transport.send(message);

		            System.out.println("Done");

		        } catch (MessagingException e) {
		            throw new RuntimeException(e);
		        }
		    }
	
}
