package com.rabo.customerstatementprocessor.utils;

public class ExceptionConstants {

    public static final String NO_FILES_CAUSE = " Technical Error: Files should be there to process ";
    public static final String NON_PARSABLE = "unable to parse file";
    public static final String FILE_NOT_PRESENT = " file should be present to process";
    public static final String READ_ERROR = "error in reading file ";
    public static final String EMPTY_DIR= "Error: No files in the directory ";
    public static final String NON_CLOSABLE_WRITER= "TECHNICAL ERROR : unable to close statement writer";
    public static final String STATEMENT_CREATE_ERR= " error in creating statement ";
    public static final String STATEMENT_FILE= " statement.csv ";
    public static final String CLEANUP_CACHE = "unable to clean up folder";
    public static final String ERROR_CACHING = " Error in file caching ";
    public static final String NULL = null;
    public static final String NULL_FILE = "file to be uploaded has null reference";

    private ExceptionConstants(){
        // to override implicit public constructor
    }
}
