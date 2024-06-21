package com.avrist.urlshortener.global.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class FileUtil {

    public static byte[] readFile(String pathToFile) throws IOException {
        return Files.readAllBytes(Paths.get(pathToFile));
    }

    public static String getFileMimeType(String pathToFile) throws IOException {
        return Files.probeContentType(Paths.get(pathToFile));
    }

    public static String getFileName(String pathToFile){
        Path path = Paths.get(pathToFile);
        return path.getFileName().toString();
    }

    public static String getFileExt(String pathToFile){
        String fileName = getFileName(pathToFile);
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".")+1);
        } else {
            return null; // An empty string as there is no extension
        }
    }

    public static byte[] getFileFromClassPath(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        InputStream inputStream = resource.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }

}
