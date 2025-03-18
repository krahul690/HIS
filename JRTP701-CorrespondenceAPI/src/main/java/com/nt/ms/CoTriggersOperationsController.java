package com.nt.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.binding.COSummary;
import com.nt.service.ICorrespondenceMgmtService;

import jakarta.mail.Service;

@RestController
@RequestMapping("/co-triggers-api")
public class CoTriggersOperationsController {
	@Autowired
	private  ICorrespondenceMgmtService coService;

	   @GetMapping("/process")
	  public  ResponseEntity<COSummary>   processAndUpdateTriggers()throws Exception{
		  COSummary  summary= coService.processPendingTriggers();
		  return  new ResponseEntity<COSummary>(summary,HttpStatus.OK);
	  }
}
