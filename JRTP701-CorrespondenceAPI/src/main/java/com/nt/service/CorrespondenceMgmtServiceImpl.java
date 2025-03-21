package com.nt.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nt.binding.COSummary;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.ElgibilityDetailsEntity;
import com.nt.repository.IApplicationRegistrationRepository;
import com.nt.repository.ICOTriggerRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IElgibiltyDeterminationRepository;
import com.nt.utils.EmailUtils;

@Service
public class CorrespondenceMgmtServiceImpl implements ICorrespondenceMgmtService {
	@Autowired
	private  ICOTriggerRepository    triggerRepo;
	@Autowired
	private   IElgibiltyDeterminationRepository   elgiRepo;
	@Autowired
	private   IDcCaseRepository    caseRepo;
	
	@Autowired
	private    IApplicationRegistrationRepository   citizenRepo;
	@Autowired
	private   EmailUtils  mailUtil;
	
	int  pendingTriggers=0;
    int successTrigger=0;
	

	@Override
	public COSummary processPendingTriggers() {
		CitizenAppRegistrationEntity   citizenEntity=null;
		ElgibilityDetailsEntity  eligiEntity=null;
		   
		// get all  pending  triggers  
		List<CoTriggersEntity>   triggersList=triggerRepo.findByTriggerStatus("pending");
		//prepare  COSummary Report
		   COSummary summary=new COSummary();
		   summary.setTotalTriggers(triggersList.size());
		   
		   //  Process the triggers in multithreaded env... using Executor Framework
		   ExecutorService  executorService=Executors.newFixedThreadPool(10);
		   ExecutorCompletionService<Object> pool=new ExecutorCompletionService<Object>(executorService);
		   
		  
		//  process  each  pending trigger
		   for(CoTriggersEntity  triggerEntity:triggersList) {
			   pool.submit(()-> {
					  try {
						  processTrigger(summary,triggerEntity);
						  successTrigger++;
					  }
					  catch(Exception  e) {
						  e.printStackTrace();
						  pendingTriggers++;
					  }
					return null;
			   });
				       
		   }//for
		      
		    summary.setPendingTriggers(pendingTriggers);
		   summary.setSuccessTriggers(successTrigger);
		 return summary;
	}
	
	   //helper method
	   private     CitizenAppRegistrationEntity  processTrigger(COSummary summary ,CoTriggersEntity  triggerEntity)throws Exception {
		     CitizenAppRegistrationEntity   citizenEntity=null;
		 //  get  Elgibility details  based  on caseno
		       ElgibilityDetailsEntity  eligiEntity=elgiRepo.findByCaseNo(triggerEntity.getCaseNo());
		       //get   appId based  on CaseNo
		       Optional<DcCaseEntity>    optCaseEntity=caseRepo.findById(triggerEntity.getCaseNo());
		       if(optCaseEntity.isPresent()) {
		    	   DcCaseEntity  caseEntity=optCaseEntity.get();
		    	   Integer  appId=caseEntity.getAppId();
		    	      // get the Citizen details  based on  the appId
		    	   Optional<CitizenAppRegistrationEntity>   optCitizenEntity=citizenRepo.findById(appId);
		    	   if(optCitizenEntity.isPresent()) {
		    		     citizenEntity=optCitizenEntity.get();
		    	   }
		       }
		   //  generate pdf doc  having   eligibility details  and  send that pdf doc  as email  
		    
		       generatePdfAndSendMail(eligiEntity, citizenEntity);
		       
		       return  citizenEntity;
	   }
	
	    //helper  method to generate the pdf doc
	   private  void  generatePdfAndSendMail(ElgibilityDetailsEntity  eligiEntity ,  CitizenAppRegistrationEntity  citizenEntity)throws Exception {
		
			// create Document  obj (openPdf)
			Document  document=new Document(PageSize.A4);
			//  create  pdf file  to write the content  to it
			File  file=new File(eligiEntity.getCaseNo()+".pdf");
			FileOutputStream  fos=new FileOutputStream(file);
			//get  PdfWriter  to  write  to the document and response obj
			PdfWriter.getInstance(document,fos);
			//open the document
			document.open();
			//Define  Font  for the Paragraph
			Font font=FontFactory.getFont(FontFactory.TIMES_BOLD);
			font.setSize(30);
			font.setColor(Color.CYAN);
			
			//create the paragraph having  content  and above font  style
			Paragraph para=new Paragraph("Plan Approval/ Denial Communication", font);
			para.setAlignment(Paragraph.ALIGN_CENTER);
			//add paragraph  to  document
			document.add(para);
			
			
			//  Display search results as the pdf table
			PdfPTable table=new PdfPTable(10);
			table.setWidthPercentage(70);
			table.setWidths(new  float[] {3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f});
			table.setSpacingBefore(2.0f);
			
			//prepare  heading  row cells  in the pdf table
			PdfPCell  cell=new  PdfPCell();
			cell.setBackgroundColor(Color.gray);
			cell.setPadding(5);
			Font  cellFont=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			cellFont.setColor(Color.BLACK);
			
			cell.setPhrase(new Phrase("TraceID",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("CaseNo",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("HolderName",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("HolderSSN",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("PlanName",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("PlanStatus",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("PlanStartDate",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("PlanEndDate",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("BenifitAmt",cellFont));
			table.addCell(cell);
			cell.setPhrase(new Phrase("DenialReason",cellFont));
			table.addCell(cell);
			
			// add   data cells  to   pdftable
				table.addCell(String.valueOf(eligiEntity.getEdTraceId()));
				table.addCell(String.valueOf(eligiEntity.getCaseNo()));
				table.addCell(eligiEntity.getHolderName());
				table.addCell(String.valueOf(eligiEntity.getHolderSSN()));
				table.addCell(eligiEntity.getPlanName());
				table.addCell(eligiEntity.getPlanStatus());
				table.addCell(String.valueOf(eligiEntity.getPlanStartDate()));
				table.addCell(String.valueOf(eligiEntity.getPlanEndDate()));
				table.addCell(String.valueOf(eligiEntity.getBenifitAmt()));
				table.addCell(String.valueOf(eligiEntity.getDenialReason()));
				
			// add table  to  document
			document.add(table);
			//close the document
			document.close();
           // send the generated pdf doc as the email  message
			String  subject=" Plan approval/deniel  mail ";
			String  body="hello Mr/Miss/Mrs."+citizenEntity.getFullName()+",  This  mail  contains  complete deatails  plan approval or deniel  ";
			mailUtil.sendMail(citizenEntity.getEmail(),subject, body, file);
			//  update   Co_Trigger  table
		   updateCoTrigger(eligiEntity.getCaseNo(),file);
	   }
	   
	   private  void  updateCoTrigger(Integer caseNo, File file) throws Exception{
		  // check Trigger avaaiblity  based on the   caseNo
		   CoTriggersEntity  triggerEntity=triggerRepo.findByCaseNo(caseNo);
		   // get  byte[]  representing  pdf doc  contentn
		   byte[]  pdfContent=new  byte[(int) file.length()];
		   FileInputStream  fis=new FileInputStream(file);
		   fis.read(pdfContent);
		   if(triggerEntity!=null) {
			   triggerEntity.setCoNoticePdf(pdfContent);
			   triggerEntity.setTriggerStatus("completed");
			   triggerRepo.save(triggerEntity);
		   }
		   fis.close();
	   }

}
