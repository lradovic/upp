package root.demo.handlers;

import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Casopis;
import root.demo.model.Rad;
import root.demo.repository.CasopisRepository;
import root.demo.repository.RadRepository;
import root.demo.repository.UserRepository;

@Service
public class ZapamtiEmailHandler implements TaskListener {

	    @Autowired
	    private FormService formService;

		@Autowired
		RadRepository radRepository;
		
		@Autowired
		UserRepository userRepository;

	    @Override
	    public void notify(DelegateTask delegateTask) {
	    	String as = delegateTask.getAssignee();
			root.demo.model.User user = userRepository.findByUsername(as);
	    	
	    	
	    	delegateTask.getExecution().setVariable("istekloVremeMail", user.getEmail());
	     
	    }
}

