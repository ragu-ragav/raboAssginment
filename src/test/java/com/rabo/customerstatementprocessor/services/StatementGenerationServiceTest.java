package com.rabo.customerstatementprocessor.services;

import com.rabo.customerstatementprocessor.exception.FileManagingException;
import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import com.rabo.customerstatementprocessor.validation.ValidationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class StatementGenerationServiceTest {

    @InjectMocks
    StatementGenerationService statementGenerationService;

    @Test
    public void updateAndCreateStatementTest() {
        String[] record = {"112806","NL69ABNA0433647324","Subscription for Jan Theu","a45.59","+48.18a","93.77"};
        Set<ValidationType> invalids = new HashSet<>();
        invalids.add(ValidationType.START_BALANCE_ERROR);
        invalids.add(ValidationType.MUTATION_ERROR);
        statementGenerationService.updateStatement(record,invalids);
        statementGenerationService.closeWriter();
        File fileCacheFolder  =  new File(ApplicationConstants.CACHE_DIR_PATH);
        Assert.assertEquals(1, fileCacheFolder.listFiles().length);
        try(BufferedReader reader  =  new BufferedReader(new FileReader(
                Paths.get(ApplicationConstants.CACHE_DIR_PATH+"/statement.csv").toAbsolutePath().toFile()))){
            Assert.assertEquals("reference,accountNumber,description,startBalance,mutation,endBalance,[validation failures]"
                    ,reader.readLine());
            Assert.assertTrue(reader.readLine().contains("112806,NL69ABNA0433647324"));
        }catch(IOException e){
            log.error(e.getMessage());
        }

    }

    @After
    public void after(){
        File fileCacheFolder  =  new File(ApplicationConstants.CACHE_DIR_PATH);
        if(fileCacheFolder.exists()) {
            try {
                FileUtils.cleanDirectory(fileCacheFolder);
            } catch (IOException e) {
                throw new FileManagingException(" unable to delete folder ", fileCacheFolder.getAbsolutePath(),
                        e.getMessage());
            }
        }
    }
}
