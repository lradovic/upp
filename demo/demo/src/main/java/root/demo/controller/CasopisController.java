package root.demo.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.cmd.GetDeploymentResourceNamesCmd;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import root.demo.model.FormFieldsDto;
import root.demo.model.FormMultiple;
import root.demo.model.FormSubmissionDto;
import root.demo.model.TaskDto;
import root.demo.repository.CasopisRepository;
import root.demo.repository.UserRepository;

@Controller
@RequestMapping("/casopis")
public class CasopisController {
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CasopisRepository casopisRepository;
	
	@GetMapping(path = "/get/{user}", produces = "application/json")
    public @ResponseBody FormFieldsDto getPocni(@PathVariable String user) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("KreiranjeMagazina");
		
		runtimeService.setVariable(pi.getId(), "initiator", user);


		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		runtimeService.setVariable(pi.getId(), "casopisAktiviran", false);

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	@GetMapping(path = "/polja/{processInstanceId}", produces = "application/json")
    public @ResponseBody FormFieldsDto polja(@PathVariable String processInstanceId) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDto(task.getId(), processInstanceId, properties);
    }
	
	@GetMapping(path = "/get/tasks/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> get(@PathVariable String processInstanceId) {
		
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}
		
        return new ResponseEntity(dtos,  HttpStatus.OK);
    }
	
	@GetMapping(path = "/potvrdi/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity potvrdi(@PathVariable String processInstanceId) {
		
		runtimeService.setVariable(processInstanceId, "emailAktiviran", true);
        return new ResponseEntity(HttpStatus.OK);
    }
	

	@PostMapping(path = "/dodatiUrednika/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodatiUrednika(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "dodatiUrednika", dto);
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "magazin", dto);
		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("ISSNbroj"))
			{
				runtimeService.setVariable(processInstanceId,"ISSNbroj",d.getFieldValue());
			}
		}
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/dodajOblast/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodajOblast(@RequestBody List<FormMultiple> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDtoMultiple(dto);
        System.out.println("MAP " + map);

		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		

		
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String ISSNbroj = (String) runtimeService.getVariable(processInstanceId, "ISSNbroj");
				
				root.demo.model.Casopis korisnik = casopisRepository.findByISSNbroj(ISSNbroj);
				
		System.out.println("KORISNIK: " + korisnik);
		String nOblasti = "";

		String urednik = (String) runtimeService.getVariable(processInstanceId, "initiator");
		

		runtimeService.setVariable(processInstanceId, "oblast", dto);
			
			//for(FormSubmissionDto d : dto)
			//{
		
		 for (Object value: (ArrayList) map.get("n_oblasti")){
		        System.out.println("VALUE " + value.toString());

			 	nOblasti +=',' + value.toString();
	        }

			//}
			korisnik.setGlavniUrednik(urednik);
			korisnik.setnOblasti(nOblasti);		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	private HashMap<String, Object> mapListToDtoMultiple(List<FormMultiple> dto) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormMultiple temp : dto){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	@PostMapping(path = "/dodajUrednika/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodajUrednika(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String ISSNbroj = (String) runtimeService.getVariable(processInstanceId, "ISSNbroj");
				
				root.demo.model.Casopis korisnik = casopisRepository.findByISSNbroj(ISSNbroj);
				
		System.out.println("KORISNIK: " + korisnik);
		
		runtimeService.setVariable(processInstanceId, "urednici", dto);
			
			for(FormSubmissionDto d : dto)
			{
				if(d.getFieldId().equals("prviUrednik")) {
					
					root.demo.model.User user = userRepository.findByUsername(d.getFieldValue());
					if(user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					else
					{
						if(user.getRole().equals("urednik") && user.isAktiviran())
						{
							korisnik.setUrednik1(d.getFieldValue());
						}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
					
				}
				
				if(d.getFieldId().equals("drugiUrednik")) {
					root.demo.model.User userr = userRepository.findByUsername(d.getFieldValue());
					if(userr == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					else
					{
						if(userr.getRole().equals("urednik") && userr.isAktiviran())
						{
							korisnik.setUrednik2(d.getFieldValue());
						}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

					}

				}
				
				
			}		

		
			casopisRepository.save(korisnik);

		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/dodajRecenzenta/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodajRecenzenta(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String ISSNbroj = (String) runtimeService.getVariable(processInstanceId, "ISSNbroj");
				
				root.demo.model.Casopis korisnik = casopisRepository.findByISSNbroj(ISSNbroj);
				
		System.out.println("KORISNIK: " + korisnik);
		
		runtimeService.setVariable(processInstanceId, "recenzenti", dto);
			
			for(FormSubmissionDto d : dto)
			{
				if(d.getFieldId().equals("prviRecenzent"))
				{
					root.demo.model.User user = userRepository.findByUsername(d.getFieldValue());
					if(user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					else
					{
						if(user.getRole().equals("recenzent") && user.isAktiviran())
						{
							korisnik.setRecenzent1(d.getFieldValue());
						}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
				}
				
				if(d.getFieldId().equals("drugiRecenzent"))
				{
					root.demo.model.User userr = userRepository.findByUsername(d.getFieldValue());
					if(userr == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					else
					{
						if(userr.getRole().equals("recenzent") && userr.isAktiviran())
						{
							korisnik.setRecenzent2(d.getFieldValue());
						}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
				}
				
				
			}		

		
			casopisRepository.save(korisnik);
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/adminPotvrda/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity adminPotvrda(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
	
		boolean promenljiva = false;
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String ISSNbroj = (String) runtimeService.getVariable(processInstanceId, "ISSNbroj");
		
		root.demo.model.Casopis korisnik = casopisRepository.findByISSNbroj(ISSNbroj);
		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("adminMagazin"))
			{
				promenljiva = Boolean.parseBoolean(d.getFieldValue());
			}
		}
		
		if(!promenljiva)
		{
			casopisRepository.delete(korisnik);
		}
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/tasks/claim/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity claim(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String user = (String) runtimeService.getVariable(processInstanceId, "username");
		taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/tasks/complete/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> complete(@PathVariable String taskId) {
		Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.complete(taskId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(taskTemp.getProcessInstanceId()).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}
        return new ResponseEntity<List<TaskDto>>(dtos, HttpStatus.OK);
    }
	
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	/* public void sendMail(User userDTO) throws MessagingException {

       // smtpMailSender.send(userDTO.getEmail(), "Test mail from Spring", "http://localhost:4200/auth/confirmation"+userDTO.getId());

        final String username = "isaprojekat.ftn@gmail.com";
        final String password = "ISAOdbrana";

        Properties props = new Properties();
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new
                    InternetAddress("isaprojekat.ftn@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(userDTO.getEmail()));
            message.setSubject("Confirm password");
            message.setText("Postovani,"
                    + "\n\n Kliknite na link ispod za verifikaciju naloga!"+"\n \n http://localhost:4200/verification/"+userDTO.getId());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }*/
}
