package root.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.FormSubmissionDto;
import root.demo.model.Rad;
import root.demo.repository.CasopisRepository;
import root.demo.repository.RadRepository;
import root.demo.repository.UserRepository;

@Service
public class PronadjiUrednikaService  implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	
	@Autowired
	CasopisRepository casopisRepository;
	
	@Autowired
	RadRepository radRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
	String radId = (String) execution.getVariable("radId");
	
	System.out.println("RAD ID " + radId);
	System.out.println("Long id " + Long.parseLong(radId));

		
		Rad rad = radRepository.getOne(Long.parseLong(radId));
		//String urednik = rad.getCasopis().getGlavniUrednik();
		List<root.demo.model.User> users = userRepository.findByRole("urednik");
		boolean nasao = false;
		
		outerloop:
		for(root.demo.model.User user : users)
		{
			
			String[] parts = user.getnOblasti().split(",");
			
			for(int i = 1;i<parts.length;i++)
			{
				if(parts[i].equals(rad.getNaucnaOblast()))
				{
					execution.setVariable("urednikDva", user.getUsername());
					execution.setVariable("urednikDvaMail", user.getEmail());
					nasao = true;
					break outerloop;
					
				}
			}	
		}
		
		if(!nasao)
		{
			String urednik = (String) execution.getVariable("urednik");
			String urednikMail = (String) execution.getVariable("urednikMail");

			
			execution.setVariable("urednikDva", urednik);
			execution.setVariable("urednikDvaMail", urednikMail);
		}
		
	      
	      
	      
	}
}
