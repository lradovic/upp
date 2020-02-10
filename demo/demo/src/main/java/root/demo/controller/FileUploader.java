package root.demo.controller;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import root.demo.repository.RadRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileUploader {

	@Autowired
	RadRepository radRepository;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	  //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\Users\\Luka\\Downloads\\primer\\demo\\demo\\src\\main\\java\\root\\demo\\files\\";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/saveFile/{processId}") 
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String processId){

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
        	String fileNewname = FilenameUtils.getBaseName(file.getOriginalFilename()) + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileNewname);
            Files.write(path, bytes);
            
            
           
    		//String naslovRada = (String) runtimeService.getVariable(processId, "naslovRada");
    		
    		//root.demo.model.Rad korisnik = radRepository.findByNaslovRada(naslovRada);
            
            String id = (String) runtimeService.getVariable(processId, "radId");
            root.demo.model.Rad korisnik = radRepository.getOne(Long.parseLong(id));
    		korisnik.setPdf(fileNewname);
    		
    		radRepository.save(korisnik);
    		
    		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();

    		Task task = tasks.get(0);
    		taskService.complete(task.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/updateFile/{processId}") 
    public ResponseEntity update(@RequestParam("file") MultipartFile file, @PathVariable String processId){

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
        	String fileNewname = FilenameUtils.getBaseName(file.getOriginalFilename()) + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileNewname);
            String id = (String) runtimeService.getVariable(processId, "radId");
            root.demo.model.Rad korisnik = radRepository.getOne(Long.parseLong(id));
            Path pathBrisi = Paths.get(UPLOADED_FOLDER + korisnik.getPdf());
            		
            Files.write(path, bytes);
            Files.deleteIfExists(pathBrisi);
            
           
    		//String naslovRada = (String) runtimeService.getVariable(processId, "naslovRada");
    		
    		//root.demo.model.Rad korisnik = radRepository.findByNaslovRada(naslovRada);
            

    		korisnik.setPdf(fileNewname);
    		
    		radRepository.save(korisnik);
    		
    		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();

    		Task task = tasks.get(0);
    		taskService.complete(task.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
