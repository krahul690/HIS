package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {
	 public  DcEducationEntity    findByCaseNo(int caseNo);
}
