package root.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.DateFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.impl.type.PrimitiveValueTypeImpl.DateTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
import root.demo.repository.RadRepository;
import root.demo.repository.UserRepository;

@Controller
@RequestMapping("/rad")
public class RadController {
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
	
	@Autowired
	private RadRepository radRepository;
	
	@GetMapping(path = "/get/{user}", produces = "application/json")
    public @ResponseBody FormFieldsDto getPocni(@PathVariable String user) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("obradaTeksta");
		
		runtimeService.setVariable(pi.getId(), "initiator", user);


		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		runtimeService.setVariable(pi.getId(), "objavljen", false);

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
	
	@GetMapping(path = "/poljaa/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDto poljaa(@PathVariable String taskId) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();	
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDto(task.getId(), task.getProcessInstanceId(), properties);
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
	
	/*@GetMapping(path = "/izborMagazina/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity potvrdi(@PathVariable String processInstanceId) {
		
		String[] parts = processInstanceId.split(",");
		processInstanceId = parts[0];
		String id = parts[1];
		
		runtimeService.setVariable(processInstanceId, "izborMagazina", id);
        return new ResponseEntity(HttpStatus.OK);
    }*/
	
	@PostMapping(path = "/izborMagazina/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity izborMagazina(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
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
		
		String id = (String) dto.get(0).getFieldValue();
		
		runtimeService.setVariable(processInstanceId, "izborMagazina", id);
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
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
			
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "rad", dto);
		

		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("naslovRada"))
			{
				runtimeService.setVariable(processInstanceId,"naslovRada",d.getFieldValue());
			}
		}
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/listaRecenzenata/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodajOblast(@RequestBody List<FormMultiple> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDtoMultiple(dto);
        System.out.println("MAP " + map);		
		
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		ArrayList<String> userList = new ArrayList<>();
		
		 for (Object value: (ArrayList) map.get("recenzenti_lista")){
		        System.out.println("VALUE " + value.toString());
		        
		        userList.add(value.toString());
	        }


			
			runtimeService.setVariable(processInstanceId, "usersList", userList);
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
	
	@PostMapping(path = "/dodajKoautora/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity dodajKoautora(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
	    String id = (String) runtimeService.getVariable(processInstanceId, "radId");
        root.demo.model.Rad korisnik = radRepository.getOne(Long.parseLong(id));
        String koautori = korisnik.getKoautori();
        
        if(koautori == null)
        	koautori = "";
				
		System.out.println("KORISNIK: " + korisnik);
					
			for(FormSubmissionDto d : dto)
			{
				if(d.getFieldId().equals("imePrezime")) {
				
					koautori += d.getFieldValue() + ','; 
				}
				
				if(d.getFieldId().equals("adresaKoautora")) {
					koautori += d.getFieldValue() + ','; 

				}
				
				if(d.getFieldId().equals("emailKoautora")) {
					koautori += d.getFieldValue() + ','; 

				}
				
				
			}		
				
			korisnik.setKoautori(koautori);
		
			radRepository.save(korisnik);

		
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
	
	@PostMapping(path = "/vremeRecenzije/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity claim(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		String novi="";
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();		

		for(FormSubmissionDto d : dto)
		{
			/*if(d.getFieldId().equals("vremeRecenzije"))
			{
				String s = d.getFieldValue();
				
				String[] parts = s.split("-");
				
				novi = parts[2] + "/" + parts[1] + "/" + parts[0];
				
			}*/
		}
		
		/*Object o = map.get("vremeRecenzije");
		
		o = novi;
		
		map.put("vremeRecenzije", o);*/
		
		
		//map.put("vremeRecenzije", dateFormType);
		formService.submitTaskForm(taskId, map);
		//runtimeService.setVariable(processInstanceId,"vremeRecenzije",novi);

        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	
	
	@PostMapping(path = "/noviRecen/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity noviRecen(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();	
		
		ArrayList<String> users = (ArrayList<String>) runtimeService.getVariable(processInstanceId, "usersList");

		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("noviRecenzent"))
			{
				root.demo.model.User user = userRepository.findByUsername(d.getFieldValue());
				if(user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				else
				{
					if(user.getRole().equals("recenzent") && user.isAktiviran())
					{
						if(users!=null)
						{
							for(String s : users)
							{
								if(s.equals(user.getUsername()))
								{
									return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
								}else
								{
									users.add(d.getFieldValue());
								}
							}
						}
						
						//korisnik.setRecenzent1(d.getFieldValue());
					}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/recenzentiOdluka/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity receOdluka(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();		
		HashMap<String, Object> mapa = new HashMap<>();
		String odluke = "";
		
		
		
		if(runtimeService.getVariable(processInstanceId, "recenzentiOdluka")==null)
		{
			for(FormSubmissionDto d : dto)
			{
				if(d.getFieldId().equals("recenzijaOdluka"))
				{
					mapa.put("odluke"+task.getAssignee(), d.getFieldValue());
				}
			}
			
			
			runtimeService.setVariable(processInstanceId,"recenzentiOdluka",mapa);
		}
		else
		{
			HashMap<String, Object> mapaa = (HashMap<String, Object>) runtimeService.getVariable(processInstanceId, "recenzentiOdluka");
			System.out.println("mapa " + mapaa);
			
		/*	String[] odlukee = (String[]) mapa.get("odluke");
			
			ArrayList<String> opet = new ArrayList<>();
			
			for(String o : odlukee)
			{
				opet.add(o);
			}
			
			System.out.println("odlukee " + odlukee);*/
			for(FormSubmissionDto d : dto)
			{
				if(d.getFieldId().equals("recenzijaOdluka"))
				{
					//int s = new Random().nextInt((99 - 15) + 1) + 15;
					
					//System.out.println("S " + s);
					
					mapaa.put("odluke"+task.getAssignee(), d.getFieldValue());
				}
			}
			
			
		
			
			runtimeService.setVariable(processInstanceId,"recenzentiOdluka",mapaa);
		}

		
		formService.submitTaskForm(taskId, map);
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
	
	@PostMapping(path = "/completeTask/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity completeTask(@PathVariable String taskId) {
		Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.complete(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/complete/{pId}", produces = "application/json")
    public @ResponseBody ResponseEntity completeTaskOfProcess(@PathVariable String pId) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(pId).list();
		Task task = tasks.get(0);
		taskService.complete(task.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	@PostMapping(path = "/completePolja/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity completeSaPoljima(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
			
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();		

		for(FormSubmissionDto d : dto)
		{
			if(d.getFieldId().equals("proveraFinalno"))
			{
				runtimeService.setVariable(processInstanceId,"proveraFinalno",d.getFieldValue());
			}
		}
		
		formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(value = "/getVar/{processInstanceId}/{varName}", produces = "application/json")
	public ResponseEntity<Object> getProcessVariable(@PathVariable String processInstanceId, @PathVariable String varName) {
		Object var = runtimeService.getVariable(processInstanceId, varName);
		return new ResponseEntity<Object>(var, HttpStatus.OK);
	}
}
