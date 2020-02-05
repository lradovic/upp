package root.demo.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.FormSubmissionDto;
import root.demo.repository.UserRepository;

@Service
public class CuvanjeService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		  root.demo.model.User user = new root.demo.model.User();
		  String username = "";
	      List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
	      System.out.println(registration);
	      for (FormSubmissionDto formField : registration) {
			if(formField.getFieldId().equals("username")) {
				username = formField.getFieldValue();
			}
	      }
	      
	      user = userRepository.findByUsername(username);
	      
	      user.setAktiviran(true);
	      user.setRole("recenzent");
	      
	      
	      userRepository.save(user);
	}
}