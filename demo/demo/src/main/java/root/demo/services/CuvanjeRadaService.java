package root.demo.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Casopis;
import root.demo.model.FormSubmissionDto;
import root.demo.model.Rad;
import root.demo.repository.CasopisRepository;
import root.demo.repository.RadRepository;
import root.demo.repository.UserRepository;

@Service
public class CuvanjeRadaService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RadRepository radRepository;
	
	@Autowired
	CasopisRepository casopisRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String c = (String) execution.getVariable("magazinId");
		Casopis cas = casopisRepository.getOne(Long.parseLong(c));
		List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)execution.getVariable("rad");
		String username = (String) execution.getVariable("initiator");
		root.demo.model.User user = userRepository.findByUsername(username);
		
		execution.setVariable("autorMail", user.getEmail());

	      System.out.println(magazin);
	      Rad casopis = new Rad();
	      
	      for (FormSubmissionDto formField : magazin) {
			if(formField.getFieldId().equals("naslovRada")) {
				System.out.println("ISSN BROJ " + formField.getFieldValue());
				casopis.setNaslovRada(formField.getFieldValue());

			}
			if(formField.getFieldId().equals("kljucniPojmovi")) {
				casopis.setKljucniPojmovi(formField.getFieldValue());

			}
			if(formField.getFieldId().equals("apstrakt")) {
				casopis.setApstrakt(formField.getFieldValue());
			}

			if(formField.getFieldId().equals("naucnaOblast")) {
				casopis.setNaucnaOblast(formField.getFieldValue());
			}
	      }
	      
	      casopis.setKoautori("");
	      casopis.setObjavljen(false);
	      casopis.setCasopis(cas);
	      casopis.setPdf("");
	      casopis.setAutor(username);
	      radRepository.save(casopis);
	      
	      execution.setVariable("radId", casopis.getId().toString());
	      
	      
	      
	      
	}
}
