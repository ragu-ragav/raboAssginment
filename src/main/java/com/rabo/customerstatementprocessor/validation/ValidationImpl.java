package com.rabo.customerstatementprocessor.validation;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationImpl {
    static Map<String,String> references = new HashMap<>();
    private ValidationType validationType;

    public boolean validateReference(String[] input) {
        String reference = input[0].trim();
        if(!isUnsignedNonDecimalNumber(reference)){
            return false;
        }else {
            if(!references.containsKey(reference)){
                references.put(reference,null);
                return true;
            }else{
                return false;
            }
        }
    }
        public boolean validateStartBalance(String[] input) {
        String startBalance = input[3].trim();
        return isSignedDecimalNumber(startBalance);
    }
    public boolean validateMutation(String[] input) {
        String mutation = input[4].trim();
        return isSignedDecimalNumber(mutation);
    }
    public boolean validateEndBalance(String[] input) {
        String endBalance = input[5].trim();
        if (!isSignedDecimalNumber(endBalance)) {
            return false;
        } else {
            return (NumberUtils.createBigDecimal(input[3]).add(NumberUtils.createBigDecimal(input[4]))).equals(NumberUtils.createBigDecimal(endBalance));
        }
    }
    
    private  boolean isSignedDecimalNumber(String stringNumber) {
        stringNumber = stringNumber.trim();
        if (stringNumber.isEmpty()) {
            return false;
        } else if (stringNumber.charAt(0) == '+') {
            return NumberUtils.isParsable(stringNumber.substring(1).trim());
        } else {
            return NumberUtils.isParsable(stringNumber);
        }
    }
    private  boolean isUnsignedNonDecimalNumber(String stringNumber){
        stringNumber = stringNumber.trim();
        if (stringNumber.isEmpty()) {
            return false;
        } else if (stringNumber.charAt(0) == '-') {
            return false;
        } else {
            return NumberUtils.isParsable(stringNumber) && !stringNumber.contains(".");
        }
    }

}
