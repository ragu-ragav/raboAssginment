package com.rabo.customerstatementprocessor.services;

import com.rabo.customerstatementprocessor.exception.FileManagingException;
import com.rabo.customerstatementprocessor.exception.FileValidatingException;
import com.rabo.customerstatementprocessor.validation.MT940FileValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileProcessigServiceTest {

    @InjectMocks
    @Spy
    FilesProcessingService fileProcessigService;

    @Mock
    MT940FileValidator transactionFileValidator;

    @Mock
    StatementGenerationService statementGenerationService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testProcessFiles(){
        doNothing().when(fileProcessigService).processCsvFile(any());
        doNothing().when(fileProcessigService).processXmlFile(any());
        doNothing().when(fileProcessigService).cleanUpFileProcessor();
        Path path = Paths.get("src/test/resources/process_files");
        fileProcessigService.processFiles(path.toFile().getAbsolutePath());
        verify(fileProcessigService, times(2)).processCsvFile(any());
        verify(fileProcessigService, times(1)).processXmlFile(any());
        verify(fileProcessigService, times(1)).cleanUpFileProcessor();
    }

    @Test
    public void testProcessCSvFile(){
        Mockito.doNothing().when(transactionFileValidator).validateMt940File(any());
        Path path = Paths.get("src/test/resources/process_files/records.csv");
        fileProcessigService.processCsvFile(path.toFile());
        verify(transactionFileValidator, times(10)).validateMt940File(any());
    }

    @Test
    public void testProcessWrongCSvFile(){
        expectedEx.expect(FileValidatingException.class);
        Path path = Paths.get("src/test/resources/process_files/wrongrecords.csv");
        fileProcessigService.processCsvFile(path.toFile());

    }

    @Test
    public void testProcessCSvFileNegative(){
        expectedEx.expect(FileManagingException.class);
        Path path = Paths.get("src/test/resources/process_files/noFile.csv");
        fileProcessigService.processCsvFile(path.toFile());
    }

    @Test
    public void testProcessXmlFile(){
        Mockito.doNothing().when(transactionFileValidator).validateMt940File(any());
        Path path = Paths.get("src/test/resources/process_files/records.xml");
        fileProcessigService.processXmlFile(path.toFile());
        verify(transactionFileValidator, times(10)).validateMt940File(any());
    }

    @Test
    public void testCleanUpFileProcessor(){
        Mockito.doNothing().when(statementGenerationService).closeWriter();
        fileProcessigService.cleanUpFileProcessor();
        verify(statementGenerationService, times(1)).closeWriter();
    }
}
