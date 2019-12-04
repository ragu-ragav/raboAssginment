package com.rabo.customerstatementprocessor.services;


import com.rabo.customerstatementprocessor.exception.FileManagingException;
import com.rabo.customerstatementprocessor.utils.ApplicationConstants;
import com.rabo.customerstatementprocessor.utils.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class FileHandlingService {

    /**
     * @Code - clears the files if any in the local cache folder
     */
    public void cleanCacheFolder(){
        File fileCacheFolder  =  new File(ApplicationConstants.CACHE_DIR_PATH);
        if(fileCacheFolder.exists()) {
            try {
                FileUtils.cleanDirectory(fileCacheFolder);
                log.info("{} folder cleaned up", fileCacheFolder);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new FileManagingException(ExceptionConstants.CLEANUP_CACHE, fileCacheFolder.getAbsolutePath(), e.getMessage());
            }
        }
    }

    /**
     * @param upfile -  the file to be cached for post processing
     * @return String -  a file name which is been stored
     */
    public String storeFile(MultipartFile upfile) {

        File directory = new File(ApplicationConstants.CACHE_DIR_PATH);
        String fileName = Optional.ofNullable(upfile).
                orElseThrow(() -> new FileManagingException(ExceptionConstants.NULL_FILE,ExceptionConstants.NULL,ExceptionConstants.NULL_FILE)).getOriginalFilename();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File localFile = new File(directory.getAbsolutePath()
                                            + ApplicationConstants.PATH_SEPARATOR
                                            + upfile.getOriginalFilename());

        try(FileOutputStream fout = new FileOutputStream(localFile)) {
            localFile.createNewFile();
            log.info("new File created {}", localFile);
            fout.write(upfile.getBytes());
        }catch (IOException e) {
            log.error("Error in storing file {}", fileName);
            throw new FileManagingException(ExceptionConstants.ERROR_CACHING, fileName, e.getMessage());
        }
        return fileName;
    }

    public  boolean deleteFiles(MultipartFile DelFile) {
        File directory = new File(ApplicationConstants.CACHE_DIR_PATH);
        String fileName = Optional.ofNullable(DelFile).
                orElseThrow(() -> new FileManagingException(ExceptionConstants.NULL_FILE,ExceptionConstants.NULL,ExceptionConstants.NULL_FILE)).getOriginalFilename();
        File localFile = new File(directory.getAbsolutePath()
                + ApplicationConstants.PATH_SEPARATOR
                + DelFile.getOriginalFilename());
        if(localFile.delete()){
            log.info("file deleted"+localFile);
            return true;
        }
        else {
            log.info("file does't exits");
            return true;
        }
    }
}
