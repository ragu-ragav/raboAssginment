package com.rabo.customerstatementprocessor.services;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.rabo.customerstatementprocessor.exception.FileManagingException;
import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import com.rabo.customerstatementprocessor.utils.ExceptionConstants;
import com.rabo.customerstatementprocessor.validation.ValidationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@Component
@Slf4j
public class StatementGenerationService {

    private CSVWriter writer = null;
    private Writer bufferedWriter = null;

    public CSVWriter createStatement(){
        try {
            //we are closing the OutPutStream at the end of the process by calling closeWriter
            bufferedWriter = Files.newBufferedWriter(Paths.get(ApplicationConstants.CACHE_DIR_PATH+ApplicationConstants.STATEMENT_FILE));
            CSVWriter csvWriter = new CSVWriter(bufferedWriter,
                    ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER,
                    ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    ICSVWriter.DEFAULT_LINE_END);

            csvWriter.writeNext(ApplicationConstants.STATEMENT_HEADER);
            return csvWriter;
        }catch (IOException e){
            log.error(e.getMessage());
            throw new FileManagingException(ExceptionConstants.STATEMENT_CREATE_ERR,ExceptionConstants.STATEMENT_FILE, e.getMessage());
        }
    }

    public void updateStatement( String[] record, Set<ValidationType> invalids ){
        if(null == writer ){
            writer = createStatement();
        }
        String[] statementRecord =  new String[ApplicationConstants.VALUE_SEVEN];
        System.arraycopy( record,ApplicationConstants.VALUE_ZERO,statementRecord,ApplicationConstants.VALUE_ZERO,record.length );
        statementRecord[ApplicationConstants.VALUE_SIX] = invalids.toString();
        writer.writeNext( statementRecord );
    }

    public void closeWriter(){
        try{
            this.writer.close();
            this.writer = null;
            this.bufferedWriter.close();
            this.bufferedWriter =  null;
        }catch(IOException e){
            log.error("unable to close statement writer because of {}",e.getMessage());
            throw new FileManagingException(ExceptionConstants.NON_CLOSABLE_WRITER,
                    ExceptionConstants.NULL, e.getMessage());
        }

    }
}
