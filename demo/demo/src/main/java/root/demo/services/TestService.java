package root.demo.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.FormSubmissionDto;
import root.demo.repository.UserRepository;

@Service
public class TestService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		 String var = "Pera";
	      var = var.toUpperCase();
	      execution.setVariable("input", var);
	      List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
	      System.out.println(registration);
	      User user = identityService.newUser("");
	      root.demo.model.User korisnik = new  root.demo.model.User();

	      for (FormSubmissionDto formField : registration) {
			if(formField.getFieldId().equals("username")) {
				user.setId(formField.getFieldValue());
				korisnik.setUsername(formField.getFieldValue());

			}
			if(formField.getFieldId().equals("password")) {
				user.setPassword(formField.getFieldValue());
				korisnik.setPassword(formField.getFieldValue());

			}
			if(formField.getFieldId().equals("ime")) {
				korisnik.setIme(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("prezime")) {
				korisnik.setPrezime(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("email")) {
				korisnik.setEmail(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("drzava")) {
				korisnik.setDrzava(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("grad")) {
				korisnik.setGrad(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("titula")) {
				korisnik.setTitula(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("nOblasti")) {
				korisnik.setnOblasti(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("recenzent")) {
				korisnik.setRecenzent(Boolean.getBoolean(formField.getFieldValue()));
			}
	      }
	      identityService.saveUser(user);
	      userRepository.save(korisnik);
	      
	      
	      
	}
}
