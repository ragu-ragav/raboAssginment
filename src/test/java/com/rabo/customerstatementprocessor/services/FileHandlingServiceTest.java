package com.rabo.customerstatementprocessor.services;

import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class FileHandlingServiceTest {

    @InjectMocks
    FileHandlingService fileHandlingService;

    MultipartFile multipartFile;

    @Test
    public void storeFileAndClearCacheTest(){
        fileHandlingService.storeFile(multipartFile);
        File fileCacheFolder  =  new File(ApplicationConstants.CACHE_DIR_PATH);
        Assert.assertEquals(1, fileCacheFolder.listFiles().length);
        fileHandlingService.cleanCacheFolder();
        Assert.assertEquals(0, fileCacheFolder.listFiles().length);
    }

    @Before
    public void before(){
        Path path = Paths.get("/src/test/resources/records.csv");
        String name = "records.csv";
        String originalFileName = "records.csv";
        String contentType = "text/csv";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
           log.error(e.getMessage());
        }
        multipartFile = new MockMultipartFile(name,
                originalFileName, contentType, content);
    }

}
