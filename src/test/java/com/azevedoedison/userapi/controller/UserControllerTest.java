package com.azevedoedison.userapi.controller;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.azevedoedison.userapi.dto.UserDTO;
import com.azevedoedison.userapi.model.User;
import com.azevedoedison.userapi.service.impl.UserServiceImpl;

@SpringBootTest
public class UserControllerTest {
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserServiceImpl service;
	
	@Mock
	private ModelMapper mapper;
	
	private User user;
	private UserDTO userDTO;
	
	private static final Integer ID = 1;
	private static final String NAME = "Saifuddin";
	private static final String EMAIL = "saif@gmail.com";
	private static final String PASSWORD = "Saif@123";
	
	@BeforeEach 
	void setUp() {
		MockitoAnnotations.openMocks(this);
		startUser();
	}
	
	private void startUser() {
		user =    new User(ID,NAME,EMAIL, PASSWORD);
		userDTO = new UserDTO(ID,NAME,EMAIL, PASSWORD);
	}
	
	@Test
	@DisplayName("whenFindByIdThenReturnSuccess")
	void whenFindByIdThenReturnSuccess(){	
		Mockito.when(service.findById(Mockito.anyInt())).thenReturn(user);
		Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = userController.findById(ID);
		
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(ResponseEntity.class, response.getClass());
		Assertions.assertEquals(UserDTO.class, response.getBody().getClass());		
		Assertions.assertEquals(ID, response.getBody().getId());		
		
	}
	
	@Test  
	@DisplayName("whenFindAllThenReturnAListOfUserDTO")
	void whenFindAllThenReturnAListOfUserDTO(){	
		
		Mockito.when(service.findAll()).thenReturn(List.of(user));
		Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDTO);
		
		ResponseEntity<List<UserDTO>> response = userController.findAll();
		
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(ResponseEntity.class, response.getClass());
		Assertions.assertEquals(ArrayList.class, response.getBody().getClass());
		Assertions.assertEquals(UserDTO.class, response.getBody().get(0).getClass());
		
		Assertions.assertEquals(ID, response.getBody().get(0).getId());
		Assertions.assertEquals(EMAIL, response.getBody().get(0).getEmail());
		Assertions.assertEquals(NAME, response.getBody().get(0).getName());
		Assertions.assertEquals(PASSWORD, response.getBody().get(0).getPassword());			
	}
	
	@Test  
	@DisplayName("whenCreateThenReturnCreated")
	void whenCreateThenReturnCreated(){		
		Mockito.when(service.create(Mockito.any())).thenReturn(user);						
		ResponseEntity<UserDTO> response =  userController.create(userDTO);
	
		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assertions.assertNotNull(response.getHeaders().get("Location"));
		Assertions.assertEquals(ResponseEntity.class, response.getClass());
		
	}
	
	@Test  
	@DisplayName("whenUpdateThenReturnSuccess")
	void whenUpdateThenReturnSuccess(){
		Mockito.when(service.update(user)).thenReturn(user);
		//Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = userController.update(ID, userDTO);
		
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(UserDTO.class, response.getBody().getClass());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(ID, response.getBody().getId());			
	}
			
	@Test
	@DisplayName("whenDeleteReturnSuccess")
	void whenDeleteReturnSuccess(){
		Mockito.doNothing().when(service).delete(Mockito.anyInt());		
		ResponseEntity<UserDTO> response = userController.delete(ID);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(ResponseEntity.class, response.getClass());
		Mockito.verify(service, times(1)).delete(Mockito.anyInt());
		Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		
		
	}

}
