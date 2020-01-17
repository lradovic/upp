package root.demo.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Casopis;
import root.demo.model.FormSubmissionDto;
import root.demo.repository.CasopisRepository;
import root.demo.repository.UserRepository;

@Service
public class CuvajMagazinService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	
	@Autowired
	CasopisRepository casopisRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
	      List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)execution.getVariable("magazin");
	      System.out.println(magazin);
	      Casopis casopis = new Casopis();
	      
	      for (FormSubmissionDto formField : magazin) {
			if(formField.getFieldId().equals("ISSNbroj")) {
				System.out.println("ISSN BROJ " + formField.getFieldValue());
				casopis.setiSSNbroj(formField.getFieldValue());

			}
			if(formField.getFieldId().equals("nazivMagazina")) {
				casopis.setNazivMagazina(formField.getFieldValue());

			}
			if(formField.getFieldId().equals("placanje")) {
				casopis.setPlacanje(Boolean.parseBoolean(formField.getFieldValue()));
			}

	      }
	      casopisRepository.save(casopis);
	      
	      
	      
	}
}