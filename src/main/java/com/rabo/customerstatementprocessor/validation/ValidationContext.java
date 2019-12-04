package com.rabo.customerstatementprocessor.validation;



import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@Component
public class ValidationContext {
    @Autowired
    ValidationImpl validation;

    public ValidationContext() {
    }

    public Set<ValidationType> validateData(String[] record) {
        Validation validationStrategy ;
        Set<ValidationType> validatedTypes = new HashSet<>();
            if(validatedTypes.size()> ApplicationConstants.VALUE_ZERO && (!validatedTypes.contains(ValidationType.REFERENCE_ERROR))){
                return validatedTypes;
            }
            if (!validation.validateReference(record)) {
                validatedTypes.add(ValidationType.REFERENCE_ERROR);
            }
            if (!validation.validateMutation(record)) {
                validatedTypes.add(ValidationType.MUTATION_ERROR);
            }
            if (!validation.validateStartBalance(record)) {
                validatedTypes.add(ValidationType.START_BALANCE_ERROR);
            }
            if (!validation.validateEndBalance(record)) {
                validatedTypes.add(ValidationType.END_BALANCE_ERROR);
            }
        return validatedTypes;
    }
}
