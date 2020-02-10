package root.demo.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Rad;
import root.demo.repository.CasopisRepository;
import root.demo.repository.RadRepository;
import root.demo.repository.UserRepository;

@Service
public class PronadjiRecenzenteService implements JavaDelegate{

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
		
	String radId = (String)execution.getVariable("radId");
		
		Rad rad = radRepository.getOne(Long.parseLong(radId));
		//String urednik = rad.getCasopis().getGlavniUrednik();
		List<root.demo.model.User> users = userRepository.findByRole("recenzent");
		

				if(users.size()>=2)
				{
					execution.setVariable("brRecenzenata", true);					
				}else
					execution.setVariable("brRecenzenata", false);					
	      
	      
	      
	}
}
