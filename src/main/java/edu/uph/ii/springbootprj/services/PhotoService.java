package edu.uph.ii.springbootprj.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    void saveFile(String subdir, MultipartFile multipartFile) throws IOException;
    void saveFileWithResizing(String subdir, MultipartFile multipartFile) throws IOException;
}
