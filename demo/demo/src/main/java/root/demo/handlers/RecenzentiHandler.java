package root.demo.handlers;

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
import root.demo.model.NaucnaOblast;
import root.demo.repository.NaucnaOblastRepository;

@Service
public class RecenzentiHandler implements TaskListener{
/*taskFormData = task.getExecution().getProcessEngineServices().getFormService().getTaskFormData(task.getId());
accountants = task.getExecution().getProcessEngineServices().getIdentityService().createUserQuery().memberOfGroup("recenzenti").list();

formFields = taskFormData. getFormFields();
if(formFields!=null){
   for(field in formFields){
       if( field.getId() == "recenzenti_lista"){
           //ovo je nase select polje
           items = field.getType().getValues();
           for(user in accountants){
               items.put(user.getId(),user.getId());
           }
       }
   }
}*/
	
	@Autowired
    private FormService formService;

    @Autowired
    private NaucnaOblastRepository naucneOblasti;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Dodavanje vrednosti enuma");
      //  List<NaucnaOblast> scientificAreas = naucneOblasti.findAll();
        List<User> accountants = delegateTask.getExecution().getProcessEngineServices().getIdentityService().createUserQuery().memberOfGroup("recenzenti").list();

        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> formFields = taskFormData.getFormFields();
        for (FormField formField : formFields){
            if (formField.getId().equals("recenzenti_lista")){
               MultipleEnumFormType multipleEnumFormType = (MultipleEnumFormType) formField.getType();

               for(User user: accountants){
                   multipleEnumFormType.getValues().put(user.getId().toString(), user.getId().toString());
               }
            }
        }
    }
}
