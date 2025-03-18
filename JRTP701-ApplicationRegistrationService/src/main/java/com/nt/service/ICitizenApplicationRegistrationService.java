package com.nt.service;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.exceptions.InvalidSSNException;

public interface ICitizenApplicationRegistrationService {
    public   Integer  registerCitizenApplication(CitizenAppRegistrationInputs inputs)throws InvalidSSNException;
}
