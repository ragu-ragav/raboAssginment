package com.rabo.customerstatementprocessor.validation;

import com.rabo.customerstatementprocessor.services.StatementGenerationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MT940FileValidatorTest {

    @InjectMocks
    MT940FileValidator mt940FileValidator;

    @Mock
    StatementGenerationService statementGenerationService;


    @Test
    public void testMT940FileValidator(){
      //  Mockito.when(strategyBuilder.getMT940ValidationContext()).thenReturn(new ValidationContext(strategies));
        Mockito.doNothing().when(statementGenerationService).updateStatement(any(),any());
        String[]  record =  {"193499","NL93ABNA0585619023","Candy for Danil Dekker","88.44a","-13.28","75.16"};
        mt940FileValidator.validateMt940File(record);
        verify(statementGenerationService, times(1)).updateStatement(any(),any());

    }
}
