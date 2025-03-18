package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcIncomeEntity;

public interface IDcInComeRepository extends JpaRepository<DcIncomeEntity, Integer> {
        public  DcIncomeEntity    findByCaseNo(int caseNo);
}
