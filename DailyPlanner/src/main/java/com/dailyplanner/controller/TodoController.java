package com.dailyplanner.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dailyplanner.controller.TodoController;
import com.dailyplanner.dto.TodoDto;
import com.dailyplanner.entity.User;
import com.dailyplanner.repository.UserRepository;
import com.dailyplanner.service.TodoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/todos")
@CrossOrigin("*")
public class TodoController {

	Logger log = LoggerFactory.getLogger(TodoController.class);

	private final TodoService todoService;
	
	private final UserRepository userRepository;

	public TodoController(TodoService todoService,UserRepository userRepository) {
		this.todoService = todoService;
		this.userRepository=userRepository;
	}
	
	   // http://localhost:8080/view/todos
		@RequestMapping("/todos")
		public String todos(Model model) {

			List<TodoDto> todos = todoService.getAllTodos();

			model.addAttribute("todos", todos);
			return "todos.html";
		}
	
	// handler method to handle user registration form request
	@PreAuthorize("hasRole('USER')")
    @GetMapping("/createTodo")
    public String createTodo(Model model){
    	log.info("Entering into AuthController :: createTodo");
        // create model object to store form data
        TodoDto user = new TodoDto();
        model.addAttribute("user", user);
        log.info("Exiting into AuthController :: createTodo");
        return "createTodo";
    }
	
	   // handler method to handle user registration form submit request
	//@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PreAuthorize("hasRole('USER')")
    @PostMapping("/save")
    public String todo(@Valid @ModelAttribute("user") TodoDto todoDto,
                               BindingResult result,
                               Model model){
        try {
        	log.info("Entering into AuthController :: registration");
			
			log.info("Entering into AuthController :: hasErrors");
			if(result.hasErrors()){
			    model.addAttribute("user", todoDto);
			    return "/createTodo";
			}
			
			User user=userRepository.findByEmail(todoDto.getEmail());
			
			if(user!=null) {
				
				todoDto.setUserId(user.getId());
				
			}else {
				return "redirect:/todos/createTodo?existingNoEmail";
			}
			
			log.info("Entering into AuthController :: saveUser");
			todoService.addTodo(todoDto);
			log.info("Exiting into AuthController :: saveUser");
			  log.info("Exiting into AuthController :: registration");
			return "redirect:/todos/createTodo?success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/getTodoById/{id}")
	public ResponseEntity<TodoDto> getTodoById(@Valid @PathVariable("id") Long todoId) {
		log.info("Entering into TodoController :: getTodoById");
		TodoDto todoDto = todoService.getTodo(todoId);
		log.info("Exiting into TodoController :: getTodoById");
		return new ResponseEntity<>(todoDto, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/getAllTodos")
	public ResponseEntity<List<TodoDto>> getAllTodos() {
		log.info("Entering into TodoController :: getAllTodos");
		List<TodoDto> todos = todoService.getAllTodos();
		log.info("Exiting into TodoController :: getAllTodos");
		return ResponseEntity.ok(todos);
	}

	// Build Update Todo REST API
	
	@GetMapping("/updateTodoById/{id}")
	public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable("id") Long todoId) {
		log.info("Entering into TodoController :: updateTodo");
		TodoDto updatedTodo = todoService.updateTodo(todoDto, todoId);
		log.info("Exiting into TodoController :: updateTodo");
		return ResponseEntity.ok(updatedTodo);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/deleteTodoById/{id}")
	public ResponseEntity<String> deleteTodoById(@PathVariable("id") Long todoId) {
		log.info("Entering into TodoController :: deleteTodoById");
		
		
		todoService.deleteTodo(todoId);
		log.info("Exiting into TodoController :: deleteTodoById");
		return ResponseEntity.ok("Todo deleted successfully!!");
	}

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PatchMapping("{id}/complete")
	public ResponseEntity<TodoDto> completeTodo(@PathVariable("id") Long todoId) {
		TodoDto updatedTodo = todoService.completeTodo(todoId);
		return ResponseEntity.ok(updatedTodo);
	}

	   @PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PatchMapping("{id}/in-complete")
	public ResponseEntity<TodoDto> inCompleteTodo(@PathVariable("id") Long todoId) {
		TodoDto updatedTodo = todoService.inCompleteTodo(todoId);
		return ResponseEntity.ok(updatedTodo);
	}

}
