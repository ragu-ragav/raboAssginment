package com.rabo.customerstatementprocessor.services;

import com.opencsv.CSVReader;
import com.rabo.customerstatementprocessor.exception.FileManagingException;
import com.rabo.customerstatementprocessor.exception.FileValidatingException;
import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import com.rabo.customerstatementprocessor.utils.EmptyNode;
import com.rabo.customerstatementprocessor.utils.ExceptionConstants;
import com.rabo.customerstatementprocessor.validation.MT940FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class FilesProcessingService {

    @Autowired
    @Qualifier("transactionFileValidator")
    MT940FileValidator transactionFileValidator;

    @Autowired
    StatementGenerationService statementGenerationService;



    public void processFiles(String localDirectory) {
        File directory = new File(localDirectory);
        if (!directory.exists()) {
            log.error("ERROR: no files found in directory {}, something wrong in uploading file",localDirectory);
            throw new FileManagingException(ExceptionConstants.EMPTY_DIR, directory.getAbsolutePath(),
                    ExceptionConstants.NO_FILES_CAUSE);
        }
        for(File file :  directory.listFiles()){
            int extenstionStart = file.getAbsolutePath().lastIndexOf(ApplicationConstants.DOT_CHAR);
            String fileType = (extenstionStart == -1) ? "" : file.getAbsolutePath().substring(extenstionStart + ApplicationConstants.VALUE_ONE);
            if(fileType.equalsIgnoreCase(ApplicationConstants.CSV)){
                processCsvFile(file);
            }
            if(fileType.equalsIgnoreCase(ApplicationConstants.XML)){
               processXmlFile(file);
            }
        }
        cleanUpFileProcessor();
    }

    public void processCsvFile(File file){
        log.info("started processing file {}", file.getAbsolutePath());
        try(CSVReader reader =  new CSVReader(new FileReader(file))) {
            String[] record;
            // to ignore header
            reader.readNext();
            while ((record = reader.readNext()) != null) {
                transactionFileValidator.validateMt940File(record);
            }
        }catch(FileNotFoundException exception){
            log.error("TECHNICAL ERROR :  File Not Present in provided location {} ",file.getAbsolutePath());
            throw new FileManagingException(ExceptionConstants.FILE_NOT_PRESENT, file.getName(), exception.getMessage());
        }catch(IOException exception){
            log.error("unable to read file {}",file.getAbsolutePath());
             throw new FileValidatingException(ExceptionConstants.READ_ERROR, file.getName(), exception.getMessage());
        }
    }




    public void processXmlFile(File file){
        log.info("started processing file "+file.getAbsolutePath());
        NodeList listOfRecords =  readXml(file);
        for(int count=ApplicationConstants.VALUE_ZERO; count<listOfRecords.getLength() ; count++) {
            Node recordNode = listOfRecords.item(count);
            Element recordElement = (Element) recordNode;
            String[] record = new String[ApplicationConstants.VALUE_SIX];
            record[ApplicationConstants.VALUE_ZERO] = recordElement.getAttribute(ApplicationConstants.REFERENCE);
            record[ApplicationConstants.VALUE_ONE] = Optional.ofNullable(recordElement.getElementsByTagName(ApplicationConstants.ACCOUNT_NUMBER)
                    .item(ApplicationConstants.VALUE_ZERO).getFirstChild()).orElse(new EmptyNode()).getNodeValue();
            record[ApplicationConstants.VALUE_TWO] = Optional.ofNullable(recordElement.getElementsByTagName(ApplicationConstants.DESCRIPTION)
                    .item(ApplicationConstants.VALUE_ZERO).getFirstChild()).orElse(new EmptyNode()).getNodeValue();
            record[ApplicationConstants.VALUE_THREE] = Optional.ofNullable(recordElement.getElementsByTagName(ApplicationConstants.START_BALANCE)
                    .item(ApplicationConstants.VALUE_ZERO).getFirstChild()).orElse(new EmptyNode()).getNodeValue();
            record[ApplicationConstants.VALUE_FOUR] = Optional.ofNullable(recordElement.getElementsByTagName(ApplicationConstants.MUTATION)
                    .item(ApplicationConstants.VALUE_ZERO).getFirstChild()).orElse(new EmptyNode()).getNodeValue();
            record[ApplicationConstants.VALUE_FIVE] = Optional.ofNullable(recordElement.getElementsByTagName(ApplicationConstants.END_BALANCE)
                    .item(ApplicationConstants.VALUE_ZERO).getFirstChild()).orElse(new EmptyNode()).getNodeValue();

            transactionFileValidator.validateMt940File(record);
        }
    }

    public void cleanUpFileProcessor(){
      // MT940ValidationStrategy.clearReferences();
        statementGenerationService.closeWriter();
    }


    public NodeList readXml(File file){
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
        docBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        docBuilder =  docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error(e.getMessage());
            throw new FileValidatingException(ExceptionConstants.NON_PARSABLE,file.getName(), e.getMessage());
        }
        Document doc = null;
        try {
            doc = docBuilder.parse(file);
        } catch (SAXException | IOException e) {
            log.error(e.getMessage());
            throw new FileValidatingException(ExceptionConstants.NON_PARSABLE, file.getName(), e.getMessage());
        }

        // normalize text representation
        doc.getDocumentElement().normalize();
        return  doc.getElementsByTagName(ApplicationConstants.RECORD);

    }


}
