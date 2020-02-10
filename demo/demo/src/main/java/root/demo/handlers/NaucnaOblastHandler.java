package root.demo.handlers;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormType;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.MultipleEnumFormType;
import root.demo.model.NaucnaOblast;
import root.demo.repository.NaucnaOblastRepository;

import java.util.List;

@Service
public class NaucnaOblastHandler implements TaskListener {

    @Autowired
    private FormService formService;

    @Autowired
    private NaucnaOblastRepository naucneOblasti;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Dodavanje vrednosti enuma");
        List<NaucnaOblast> scientificAreas = naucneOblasti.findAll();

        TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
        List<FormField> formFields = taskFormData.getFormFields();
        for (FormField formField : formFields){
            if (formField.getId().equals("n_oblasti")){
               MultipleEnumFormType multipleEnumFormType = (MultipleEnumFormType) formField.getType();

               for(NaucnaOblast scientificArea: scientificAreas){
                   multipleEnumFormType.getValues().put(scientificArea.getId().toString(), scientificArea.getNaucnaOblast());
               }
            }
        }
    }
}