package com.rabo.customerstatementprocessor.utils;

public class ApplicationConstants {

    public static final String CACHE_DIR_PATH = System.getProperty("user.dir")+"/output";
    public static final int VALUE_ZERO = 0;
    public static final int VALUE_ONE = 1;
    public static final int VALUE_TWO = 2;
    public static final int VALUE_THREE = 3;
    public static final int VALUE_FOUR = 4;
    public static final int VALUE_FIVE = 5;
    public static final int VALUE_SIX = 6;
    public static final int VALUE_SEVEN = 7;
    public static final String REFERENCE = "REFERENCE";
    public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String START_BALANCE = "START_BALANCE";
    public static final String MUTATION = "MUTATION";
    public static final String END_BALANCE = "END_BALANCE";
    public static final String CSV = "csv";
    public static final String XML = "XML";
    public static final char DOT_CHAR = '.';
    public static final String RECORD = "record";
    public static final String STATEMENT_FILE = "/statement.csv";
    public static final String PATH_SEPARATOR = "/";
    public static final String[] STATEMENT_HEADER = {REFERENCE, ACCOUNT_NUMBER, DESCRIPTION, START_BALANCE, MUTATION, END_BALANCE, "[FAILURE REASON]"};

    private ApplicationConstants(){
        //do nothing - to hide implicit constructor
    }
}
