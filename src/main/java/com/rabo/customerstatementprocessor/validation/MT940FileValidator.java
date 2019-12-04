package com.rabo.customerstatementprocessor.validation;


import com.rabo.customerstatementprocessor.services.StatementGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component("transactionFileValidator")
public class MT940FileValidator {

    @Autowired
    StatementGenerationService statementGenerationService;
    @Autowired
    ValidationContext validationContext;

    public void validateMt940File(String[] dataRecord){

        Set<ValidationType> validFields = validationContext.validateData(dataRecord);
        if(!validFields.isEmpty()) {
            statementGenerationService.updateStatement(dataRecord,validFields);
        }
    }
}
