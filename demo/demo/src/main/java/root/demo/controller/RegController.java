package root.demo.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cmd.GetDeploymentResourceNamesCmd;
import org.camunda.bpm.engine.rest.dto.identity.UserCredentialsDto;
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
import root.demo.model.UserDto;
import root.demo.repository.UserRepository;

@Controller
@RequestMapping("/welcome")
public class RegController {
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
	
	
	@PostMapping(value = "/login", consumes = "application/json")
	public ResponseEntity<root.demo.model.User> login(@RequestBody UserDto user) {
		root.demo.model.User customUser = findUser(user.getUsername(),
				user.getPassword());
		User camundaUser = null;
		if (customUser != null && customUser.isAktiviran()!=false) {
			identityService.setAuthenticatedUserId(customUser.getUsername());
			camundaUser = identityService.createUserQuery().userId(customUser.getUsername()).singleResult();
			// runtimeService.setVariable(processInstanceId, "camundaUser", camundaUser);
			return new ResponseEntity<root.demo.model.User>(customUser, HttpStatus.OK);

		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);

	}
	
	@GetMapping(value = "/loggedUser", produces = "application/json")
	public ResponseEntity<User> getLoggedUser() {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		Authentication auth = engine.getIdentityService().getCurrentAuthentication();
		String id = identityService.getCurrentAuthentication().getUserId();
		
		System.out.println("PRVI " + id);
		
		User user = engine.getIdentityService().createUserQuery().userId(auth.getUserId()).singleResult();
		
		System.out.println("DRUGI " + user.getId());
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping(value = "/logout")
	public ResponseEntity logout() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService idenServ = processEngine.getIdentityService();
		idenServ.clearAuthentication();
		
		return new ResponseEntity(HttpStatus.OK);
	}

	
	@GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDto get() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("Registracija");
		
	//	runtimeService.setVariable(pi.getId(), "initiator", getLoggedUser().getBody().getId());		

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		runtimeService.setVariable(pi.getId(), "emailAktiviran", false);

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	@GetMapping(path = "/polja/{processInstanceId}", produces = "application/json")
    public @ResponseBody FormFieldsDto polja(@PathVariable String processInstanceId) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		
/*		if(task.getAssignee()!=null)
		{
			if(!task.getAssignee().equals(getLoggedUser().getBody().getId()))
			{
				return null;
			}
			
		}*/
		
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
		runtimeService.setVariable(processInstanceId, "registration", dto);
		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("email"))
			{
				runtimeService.setVariable(processInstanceId,"emailKorisnika",d.getFieldValue());
			}
		}
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/dodajOblast/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodajOblast(@RequestBody List<FormMultiple> dto, @PathVariable String taskId) {
        System.out.println("USAO ");

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
		String email = (String) runtimeService.getVariable(processInstanceId, "emailKorisnika");
				
				root.demo.model.User korisnik = userRepository.findByEmail(email);
				
		System.out.println("KORISNIK: " + korisnik);
		String nOblasti = "";

		
		runtimeService.setVariable(processInstanceId, "oblast", dto);
			
			//for(FormSubmissionDto d : dto)
			//{
		
		 for (Object value: (ArrayList) map.get("n_oblasti")){
		        System.out.println("VALUE " + value.toString());

			 	nOblasti +=',' + value.toString();
	        }

			//}
			
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
	String email = (String) runtimeService.getVariable(processInstanceId, "emailKorisnika");
		
		root.demo.model.User korisnik = userRepository.findByEmail(email);
		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("proveraRecenzenta"))
			{
				promenljiva = Boolean.parseBoolean(d.getFieldValue());
			}
		}
		
		if(promenljiva)
		{
			korisnik.setRecenzent(true);
			userRepository.save(korisnik);
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
	
	public root.demo.model.User findUser(String username, String password) {
		root.demo.model.User user = userRepository.findByUsername(username);
		if(user != null) {
			if(identityService.checkPassword(user.getUsername(), password)) {
				return user;
			}
		}
		return null;
	}
		
	@GetMapping(value = "/getProcessId/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getProcessbyTask(@PathVariable String taskId) {
	
		Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
		String process = taskTemp.getProcessInstanceId();
		System.out.println("PROCES " + process);
		
		TaskFormData tfd = formService.getTaskFormData(taskTemp.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}

        return new FormFieldsDto(taskTemp.getId(), process, properties);
    }
	
/*	@GetMapping(value = "/getTasks/{userId}", produces = "application/json")
	public ResponseEntity<List<TaskDto>> getUserTasks(@PathVariable String userId) {
		List<TaskDto> taskDtoList = new ArrayList<>();
		for (Task task : taskService.createTaskQuery().taskAssignee(userId).list()) {
			User camUser = (User) runtimeService.getVariable(task.getExecutionId(), "camundaUser");
			CustomUser customUser = null;
			if(camUser != null) {
				customUser = userService.findByUsername(camUser.getId());
			}
			
			FormDataDto formDataDto = new FormDataDto(task.getId(),
					formService.getTaskFormData(task.getId()).getFormFields(), task.getExecutionId());

			TaskDto dto = new TaskDto(formDataDto, task.getName(), customUser);

			taskDtoList.add(dto);
		}
		return new ResponseEntity<List<TaskDto>>(taskDtoList, HttpStatus.OK);
	}*/
	
	@GetMapping(value = "/getTasks/{userId}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDto>> getUserTasks(@PathVariable String userId) {
		
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}
		
        return new ResponseEntity(dtos,  HttpStatus.OK);
    }
	
}
