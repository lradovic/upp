package root.demo.handlers;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.MultipleEnumFormType;
import root.demo.repository.NaucnaOblastRepository;

@Service
public class CompleteHandler implements TaskListener{
/*taskFormData = task.getExecution().getProcessEngineServices().getFormService().getTaskFormData(task.getId());
userIds = task.getExecution().getVariable("usersList");

korisnici = new java.util.ArrayList();

if(userIds == null){userIds = new java.util.ArrayList();}

formFields = taskFormData. getFormFields();
if(formFields!=null){
   for(field in formFields){
       if(field.getId() == "recenzenti_lista"){
           if(field.getValue()!="none") korisnici = field.getValue();
       }

for(korisnik in korisnici){                userIds.add(korisnik);
}
}

task.getExecution().setVariable("usersList", userIds);*/
	

	@Autowired
    private FormService formService;

    @Autowired
    private NaucnaOblastRepository naucneOblasti;

    @Override
    public void notify(DelegateTask delegateTask) {

        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<String> userIds = (List<String>) delegateTask.getExecution().getVariable("usersList");
        
        System.out.println("USer ids " + userIds);

        List<String> korisnici = new java.util.ArrayList();

        if(userIds == null){userIds = new java.util.ArrayList();}

        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields!=null){
           for(FormField field : formFields){
               if(field.getId() == "recenzenti_lista"){
            	   ArrayList<String> a = (ArrayList<String>) field.getValue();
                   System.out.println("aaaa ids " + a);

            	   for(String s : a)
            	   {
            		   korisnici.add(s);
            	   }
               	}

		        for(String korisnik : korisnici){                
		        	userIds.add(korisnik);
		        }
        }
       }

        delegateTask.getExecution().setVariable("usersList", userIds);
        
        System.out.println("final" + userIds);

    }
}
