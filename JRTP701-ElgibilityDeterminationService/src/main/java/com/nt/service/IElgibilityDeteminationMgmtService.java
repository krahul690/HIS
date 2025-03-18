package com.nt.service;

import com.nt.bindings.ElgibilityDetailsOutput;

public interface IElgibilityDeteminationMgmtService {
    public  ElgibilityDetailsOutput   determineElgibility(int caseNo);
}
