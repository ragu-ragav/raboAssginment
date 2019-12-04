package com.rabo.customerstatementprocessor.controllers;

import com.rabo.customerstatementprocessor.beans.FilesProcessed;
import com.rabo.customerstatementprocessor.services.FileHandlingService;
import com.rabo.customerstatementprocessor.services.FilesProcessingService;
import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CustomerStatementController {

    @Autowired
    FileHandlingService fileHandlingService;

    @Autowired
    FilesProcessingService filesProcessingService;

    @PostMapping("/authorizeTransactions")
    public List<String> validateTransactions(@RequestParam("files") MultipartFile[] files) {
        log.info("started validating MT940 files");
        fileHandlingService.cleanCacheFolder();
        List<String> filesProcessed = Arrays.asList(files)
                .stream()
                .map(file -> storeFile(file))
                .collect(Collectors.toList());
        filesProcessingService.processFiles(ApplicationConstants.CACHE_DIR_PATH);
        List<Boolean> cleared= Arrays.asList(files)
                .stream()
                .map(file -> deleteFiles(file))
                .collect(Collectors.toList());
        return filesProcessed;
    }


    private String storeFile(MultipartFile file){
        String fileName = fileHandlingService.storeFile(file);
        log.info(fileName+" file cached");
        return "successfully processed";

    }
    private boolean deleteFiles(MultipartFile file){
       return fileHandlingService.deleteFiles(file);
    }
}
