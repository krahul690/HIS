package com.nt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nt.bindings.ChildInputs;
import com.nt.bindings.DcSummaryReport;
import com.nt.bindings.EducationInputs;
import com.nt.bindings.IncomeInputs;
import com.nt.bindings.PlanSelectionInputs;
import com.nt.ms.DataCollectionOperationsController;
import com.nt.service.IDcMgmtService;

//@SpringBootTest
@WebMvcTest(value=DataCollectionOperationsController.class)
public class DataCollectionOperationsControllerTest {
	@MockBean
	private IDcMgmtService  dcService;
	@Autowired
	private  MockMvc   mockMvc;
	
	@Test
	public  void    displayPlanNamesTest() throws Exception {
		  //prepare  List of Plans
		 List<String> plansList=new ArrayList();
		 plansList.add("SNAP"); plansList.add("QHP");
		 plansList.add("MEDAID"); plansList.add("CCAP");
         plansList.add("MEDCARE");
         //  provide  behaviour for    service class method
         Mockito.when(dcService.showAllPlanNames()).thenReturn(plansList);
         //   prepare   requestBuilder
         MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.get("/dc-api/planNames");
         //generate the request by submitting the request
         MvcResult  result=mockMvc.perform(builder).andReturn();
         //get the status  code
         int  status=result.getResponse().getStatus();
         assertEquals(200, status);
    }
	
	@Test
	public     void   generateCaseNoTest1()throws Exception {
	//  provide  behaviour for    service class method
        Mockito.when(dcService.generateCaseNo(1)).thenReturn(1001);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.post("/dc-api/generateCaseNo/1");
        		                                                                                                                             
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        int  status=result.getResponse().getStatus();
        assertEquals(201, status);
     	}
	
	@Test
	public     void   generateCaseNoTest2()throws Exception {
	//  provide  behaviour for    service class method
        Mockito.when(dcService.generateCaseNo(121)).thenReturn(0);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.post("/dc-api/generateCaseNo/121");
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        int  status=result.getResponse().getStatus();
        assertEquals(201, status);
     	}

	@Test
	public     void   updatePlanSelectionTest1()throws Exception {
		 //create  Binding class obj  with data
		  PlanSelectionInputs  inputs=new PlanSelectionInputs();
		  inputs.setCaseNo(1001); inputs.setAppId(1);  inputs.setPlanId(1);
	//  provide  behaviour for    service class method
        Mockito.when(dcService.savePlanSelection(inputs)).thenReturn(1001);
        // convert Binding object  data  to  json  content
        ObjectMapper  mapper=new ObjectMapper();
        String jsonContent=mapper.writeValueAsString(inputs);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.put("/dc-api/updatePlanSelection")
        		                                                                           .contentType("application/json")
        		                                                                           .content(jsonContent);
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        String  respContent=result.getResponse().getContentAsString();
        assertEquals(1001, Integer.parseInt(respContent));
	}
	
	@Test
	public     void   updatePlanSelectionTest2()throws Exception {
		 //create  Binding class obj  with data
		  PlanSelectionInputs  inputs=new PlanSelectionInputs();
		  inputs.setCaseNo(2001); inputs.setAppId(123);  inputs.setPlanId(1);
	//  provide  behaviour for    service class method
        Mockito.when(dcService.savePlanSelection(inputs)).thenReturn(0);
        // convert Binding object  data  to  json  content
        ObjectMapper  mapper=new ObjectMapper();
        String jsonContent=mapper.writeValueAsString(inputs);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.put("/dc-api/updatePlanSelection")
        		                                                                           .contentType("application/json")
        		                                                                           .content(jsonContent);
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        String respContent=result.getResponse().getContentAsString();
        assertEquals(0, Integer.parseInt(respContent));
	}
	
	@Test
	public     void   saveIncomeTest()throws Exception {
		 //create  Binding class obj  with data
		IncomeInputs  inputs=new IncomeInputs();
		inputs.setCaseNo(1001);  inputs.setPropertyIncome(300000.0);  inputs.setEmpIncome(2000.0);
	//  provide  behaviour for    service class method
        Mockito.when(dcService.saveIncomeDetails(inputs)).thenReturn(1001);
        // convert Binding object  data  to  json  content
        ObjectMapper  mapper=new ObjectMapper();
        String jsonContent=mapper.writeValueAsString(inputs);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.post("/dc-api/saveIncome")
        		                                                                           .contentType("application/json")
        		                                                                           .content(jsonContent);
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        String  respContent=result.getResponse().getContentAsString();
        assertEquals(1001, Integer.parseInt(respContent));
	}
	
	@Test
	public     void   saveEducationDetailsTest()throws Exception {
		 //create  Binding class obj  with data
		EducationInputs  inputs=new EducationInputs();
		inputs.setCaseNo(1001);  inputs.setHighestQlfy("B.Tech");  inputs.setPassOutYear(2020);
	//  provide  behaviour for    service class method
        Mockito.when(dcService.saveEducationDetails(inputs)).thenReturn(1001);
        // convert Binding object  data  to  json  content
        ObjectMapper  mapper=new ObjectMapper();
        String jsonContent=mapper.writeValueAsString(inputs);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.post("/dc-api/saveEducation")
        		                                                                           .contentType("application/json")
        		                                                                           .content(jsonContent);
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        String  respContent=result.getResponse().getContentAsString();
        assertEquals(1001, Integer.parseInt(respContent));
	}

	@Test
	public     void   saveChildrenDetailsTest()throws Exception {
		 //create  Binding class obj  with data
		ChildInputs  inputs1=new ChildInputs();
		inputs1.setCaseNo(1001);  inputs1.setChildSSN(9994555L);  inputs1.setChildDOB(LocalDate.of(2000, 10, 21));
		
		ChildInputs  inputs2=new ChildInputs();
		inputs2.setCaseNo(1001);  inputs2.setChildSSN(98914555L);  inputs2.setChildDOB(LocalDate.of(2001, 11, 11));
		
		List<ChildInputs> list=new ArrayList();
		list.add(inputs1); list.add(inputs2);
		
	//  provide  behaviour for    service class method
        Mockito.when(dcService.saveChidrenDetails(list)).thenReturn(1001);
        // convert Binding object  data  to  json  content
        ObjectMapper  mapper=new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        String jsonContent=mapper.writeValueAsString(list);
        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.post("/dc-api/saveChilds")
        		                                                                           .contentType("application/json")
        		                                                                           .content(jsonContent);
        //generate the request by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
        //get the status  code
        String  respContent=result.getResponse().getContentAsString();
        assertEquals(1001, Integer.parseInt(respContent));
	}
	
	@Test
	public     void   showSummaryReportTest()throws Exception {
		//  prepare Biding obj with  data
		IncomeInputs  inputs1=new IncomeInputs();
		inputs1.setCaseNo(1001);  inputs1.setPropertyIncome(300000.0);  inputs1.setEmpIncome(2000.0);
		EducationInputs  inputs2=new EducationInputs();
		inputs2.setCaseNo(1001);  inputs2.setHighestQlfy("B.Tech");  inputs2.setPassOutYear(2020);
		
		ChildInputs  inputs3=new ChildInputs();
		inputs3.setCaseNo(1001);  inputs3.setChildSSN(9994555L);  inputs3.setChildDOB(LocalDate.of(2000, 10, 21));
		
		ChildInputs  inputs4=new ChildInputs();
		inputs4.setCaseNo(1001);  inputs4.setChildSSN(98914555L);  inputs4.setChildDOB(LocalDate.of(2001, 11, 11));
		
		List<ChildInputs> list=List.of(inputs3, inputs4);
		DcSummaryReport  report=new DcSummaryReport();
		report.setPlanName("SNAP"); report.setIncomeDetails(inputs1); report.setEducationDetails(inputs2);
		report.setChildrenDetails(list);
		
	//  provide  behaviour for    service class method
        Mockito.when(dcService.showDCSummary(1001)).thenReturn(report);

        //   prepare   requestBuilder
        MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.get("/dc-api/summary/1001");
        		           //generate the response by submitting the request
        MvcResult  result=mockMvc.perform(builder).andReturn();
          //get response status code
        int status=result.getResponse().getStatus();
       assertEquals(200, status);
	}

}
