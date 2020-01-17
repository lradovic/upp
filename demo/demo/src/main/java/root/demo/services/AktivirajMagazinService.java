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
import root.demo.repository.CasopisRepository;
import root.demo.repository.UserRepository;

@Service
public class AktivirajMagazinService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	@Autowired
	CasopisRepository userRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Casopis casopis = new Casopis();
	      List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)execution.getVariable("magazin");
	      System.out.println(magazin);
	      
	      String ISSNbroj = "";
	      for (FormSubmissionDto formField : magazin) {
			if(formField.getFieldId().equals("ISSNbroj")) {
				ISSNbroj = formField.getFieldValue();
			}
	      }
	      
	      casopis = userRepository.findByISSNbroj(ISSNbroj);
	      
	      casopis.setAktiviran(true);
	      
	      userRepository.save(casopis);
	}
}
