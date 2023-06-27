package com.azevedoedison.userapi.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

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

import com.azevedoedison.userapi.model.User;
import com.azevedoedison.userapi.repository.UserRepository;
import com.azevedoedison.userapi.service.exception.DataIntegrityViolationException;
import com.azevedoedison.userapi.service.exception.ObjectNotFoundException;

@SpringBootTest
public class UserServiceJunitWithMockitoTesting {

	private static final Integer ID = 1;
	private static final String NAME = "Hemanth";
	private static final String EMAIL = "hemanth@gmail.com";
	private static final String PASSWORD = "Saif@123";

	@InjectMocks
	private UserServiceImpl service;

	@Mock
	private UserRepository repository;

	@Mock
	private ModelMapper mapper;

	private User user;
	private Optional<User> optionalUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		startUser();
	}

	private void startUser() {
		user = new User(ID, NAME, EMAIL, PASSWORD);
		optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
	}
	
	@Test 
	@DisplayName("User data not found by find By Methods")
	void whenFindByIdThenReturnAnObjectNotFoundException() {
		Mockito.when(repository.findById(Mockito.anyInt()))
			.thenThrow(new ObjectNotFoundException("User data not found"));		
		try {
			service.findById(ID);
		}catch (Exception e) {
			assertEquals(ObjectNotFoundException.class, e.getClass());
			assertEquals("User data not found",e.getMessage());
		}	
	}
	
	@Test 
	@DisplayName("Find all User info")
	void whenFindAllThenReturnAnListOfUserInstance() {
		Mockito.when(repository.findAll()).thenReturn(List.of(user));
		
		List<User> response = service.findAll();   
		Assertions.assertNotNull(response);
		Assertions.assertEquals(1, response.size());
		Assertions.assertEquals(User.class, response.get(0).getClass());
		
		Assertions.assertEquals(ID, response.get(0).getId());
		Assertions.assertEquals(EMAIL, response.get(0).getEmail());
		Assertions.assertEquals(NAME, response.get(0).getName());
		Assertions.assertEquals(PASSWORD, response.get(0).getPassword());			
	}
	
	@Test 
	@DisplayName("when FindById Then Return An UserInstance")
	void whenFindByIdThenReturnAnUserInstance() {				
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(optionalUser);		
		User response = service.findById(ID);
		
		Assertions.assertNotNull(response);		
		Assertions.assertEquals(User.class, response.getClass());			
		Assertions.assertEquals(ID, response.getId());		
		Assertions.assertEquals(NAME, response.getName());		
		Assertions.assertEquals(EMAIL, response.getEmail());							
	}
	
	@Test 
	@DisplayName("Create user information Sucess message")
	void whenCreateThenReturnSuccess() {
		Mockito.when(repository.save(Mockito.any())).thenReturn(user);
		User response = service.create(user);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(User.class, response.getClass());						
		Assertions.assertEquals(NAME, response.getName());		
		Assertions.assertEquals(EMAIL, response.getEmail());		
		Assertions.assertEquals(PASSWORD, response.getPassword());
	}
	
	@Test 
	@DisplayName("Data Integrity Violet Exception")
	void whenCreateThenReturnAnDataIntegrityViolationException() {
		Mockito.when(repository.findByEmail(Mockito.anyString()))
			.thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2); 
			service.create(user);
		}catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}
	
	@Test 
	void whenUpdateThenReturnSuccess() {
		Mockito.when(repository.save(Mockito.any())).thenReturn(user);
		
		User response = service.update(user);
		
		Assertions.assertNotNull(response);	
		Assertions.assertEquals(User.class, response.getClass());						
		Assertions.assertEquals(NAME, response.getName());		
		Assertions.assertEquals(EMAIL, response.getEmail());		
		Assertions.assertEquals(PASSWORD, response.getPassword());
	}
	
	@Test 
	@DisplayName("whenUpdateThenReturnAnDataIntegrityViolationException")
	void whenUpdateThenReturnAnDataIntegrityViolationException() {
		Mockito.when(repository.findByEmail(Mockito.anyString()))
			.thenReturn(optionalUser);		
		try {
			optionalUser.get().setId(2); 
			service.update(user);
		}catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}
	
	@Test 
	@DisplayName("whenFindByEmailThenReturnDataIntegrityViolationException")
	void whenFindByEmailThenReturnDataIntegrityViolationException() {	
		Mockito.when(repository.findByEmail(Mockito.any()))
		.thenThrow(new DataIntegrityViolationException("E-mail already exist"));
							
		try {
			service.findByEmail(user);
		}catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());			
			assertEquals("E-mail already exist",e.getMessage());
		}		
	}
	
	@Test
	@DisplayName("deleteWithSuccess")
	void deleteWithSuccess() {
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(optionalUser);
		Mockito.doNothing().when(repository).deleteById(Mockito.anyInt());
		service.delete(ID);		
		Mockito.verify(repository,Mockito.times(1)).deleteById(Mockito.anyInt());
	}
	

	@Test
	@DisplayName("deleteWithoutSuccess")
	void deleteWithoutSuccess() {
		Mockito.when(repository.findById(Mockito.anyInt()))
		.thenThrow(new ObjectNotFoundException("User data not found"));
		try {
			service.delete(ID);
		}catch (Exception e) {
			assertEquals(ObjectNotFoundException.class, e.getClass());			
			assertEquals("User data not found",e.getMessage());
		}
	}
}
