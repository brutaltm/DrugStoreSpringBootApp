package edu.uph.ii.springbootprj.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface PhotoService {
    void saveFile(String subdir, MultipartFile multipartFile) throws IOException;
    void saveFileWithResizing(String subdir, MultipartFile multipartFile) throws IOException;
}
