package com.nt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.PlanEntity;
import com.nt.repository.IApplicationRegistrationRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IDcChidrenRepository;
import com.nt.repository.IDcEducationRepository;
import com.nt.repository.IDcInComeRepository;
import com.nt.repository.IPlanRepository;
import com.nt.service.DcMgmtServiceImpl;
import com.nt.service.IDcMgmtService;

//@WebMvcTest(value=DcMgmtServiceImpl.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DcMgmtServiceImplTest {
	@MockBean
	private   IDcCaseRepository   caseRepo;
	@MockBean
	private   IApplicationRegistrationRepository  citizenAppRepo;
	@MockBean
	private   IPlanRepository  planRepo;
	@MockBean
	private   IDcInComeRepository  incomeRepo;
	@MockBean
	private   IDcEducationRepository  educationRepo;
	@MockBean
	private   IDcChidrenRepository     childrenRepo;
	@InjectMocks
	private     DcMgmtServiceImpl   dcService;
	
	
	@Test
	public   void    generateCaseNoTest1() {
	//  provide  behaviour for    repository methods (findById())
		CitizenAppRegistrationEntity  citizenEntity=new CitizenAppRegistrationEntity();
		citizenEntity.setAppId(1);
		Optional<CitizenAppRegistrationEntity>  optCitizenEntity=Optional.of(citizenEntity);
        Mockito.when(citizenAppRepo.findById(1)).thenReturn(optCitizenEntity);
    //  provide  behaviour for    repository methods (save())
        DcCaseEntity   caseEntity=new DcCaseEntity();
        caseEntity.setAppId(1);
        
        DcCaseEntity   caseEntity1=new DcCaseEntity();
        caseEntity1.setAppId(1);
        caseEntity1.setCaseNo(1001);
        
        
          Mockito.when(caseRepo.save(caseEntity)).thenReturn(caseEntity1);
        
        //get  actual  result
        int  actual=dcService.generateCaseNo(1);
        	    
           assertEquals(1001, actual);
	}
	
	/*@Test
	public   void    generateCaseNoTest2() {
	//  provide  behaviour for    repository methods (findById())
		CitizenAppRegistrationEntity  citizenEntity=new CitizenAppRegistrationEntity();
		citizenEntity.setAppId(10);
		Optional<CitizenAppRegistrationEntity>  optCitizenEntity=Optional.empty();
        Mockito.when(citizenAppRepo.findById(1)).thenReturn(optCitizenEntity);
    //  provide  behaviour for    repository methods (save())
        DcCaseEntity   caseEntity=new DcCaseEntity();
        caseEntity.setAppId(10);
        
        DcCaseEntity   caseEntity1=new DcCaseEntity();
        caseEntity1.setAppId(10);
        caseEntity1.setCaseNo(0000);
          Mockito.when(caseRepo.save(caseEntity)).thenReturn(caseEntity1);
        
        //get  actual  result
        int  actual=dcService.generateCaseNo(10);
        	    
           assertEquals(0, actual);
	}*/
	
	
	@Test
	public void showAllPlanNamesTest() {
		PlanEntity  entity1=new PlanEntity();
		entity1.setPlanName("SNAP");
		PlanEntity  entity2=new PlanEntity();
		entity2.setPlanName("MEDAID");
		PlanEntity  entity3=new PlanEntity();
		entity3.setPlanName("MEDCARE");
		PlanEntity  entity4=new PlanEntity();
		entity4.setPlanName("QHP");
		PlanEntity  entity5=new PlanEntity();
		entity5.setPlanName("CCAP");
		PlanEntity  entity6=new PlanEntity();
		entity6.setPlanName("CAJW");
		List<PlanEntity>  list=List.of(entity1,entity2,entity3,entity4,entity5,entity6);
		
	//  provide  behaviour for    repository methods (findAll())
		   Mockito.when(planRepo.findAll()).thenReturn(list);
		    	//  invoke  actual  method to actual result
		   List<String> plansList=dcService.showAllPlanNames();
		   
	    	assertEquals(6,plansList.size());
	}
	
	


}
