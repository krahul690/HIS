package com.nt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.exceptions.InvalidSSNException;
import com.nt.repository.IApplicationRegistrationRepository;

import reactor.core.publisher.Mono;


@Service
public class CitizenApplicationRegistrationServiceImpl implements ICitizenApplicationRegistrationService {
	@Autowired
	private IApplicationRegistrationRepository   citizenRepo;
	/*
	   @Autowired
	   private RestTemplate template;
	 */	
	@Autowired
	private   WebClient  client;
	@Value("${ar.ssa-web.url}")
	private    String  endpointUrl;
	@Value("${ar.state}")
	private    String  targetState;

	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs)throws InvalidSSNException {
	    
	/*	//   perform  WebService call  to check  wheather SSN is valid or not  and  to get the state name
		// ResponseEntity<String>  response=template.exchange(endpointUrl, HttpMethod.GET,null,String.class,inputs.getSsn());*/
		
	//   perform  WebService call  to check  wheather SSN is valid or not  and  to get the state name  (Using WebClient)
		 //get  state name
		Mono<String> response= client.get()
				.uri(endpointUrl,inputs.getSsn())
				.retrieve()
				              .onStatus(
				            		  HttpStatus.BAD_REQUEST::equals,res->
				            		  res.bodyToMono(String.class)
				            		  .map(ex-> new InvalidSSNException("invalid ssn"))).bodyToMono(String.class);
		   String stateName=response.block();
		  //register  citizen   if   he belongs to   California state (CA)
		     if(stateName.equalsIgnoreCase(targetState)) {
			  //prepare the Entity object
			  CitizenAppRegistrationEntity  entity=new CitizenAppRegistrationEntity();
			   BeanUtils.copyProperties(inputs, entity);
			   entity.setStateName(stateName);
			   //save  the object
			   int  appId=citizenRepo.save(entity).getAppId();
			   return  appId;
		 		 }//if
		  throw  new  InvalidSSNException("invalid  SSN");
	}

}
