package com.nt.bindings;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ChildInputs {
	  private  Integer  childId;
		private  Integer   caseNo;
		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate childDOB;
		private  Long  childSSN;

}
