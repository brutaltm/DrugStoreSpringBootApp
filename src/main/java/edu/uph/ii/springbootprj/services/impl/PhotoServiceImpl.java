package edu.uph.ii.springbootprj.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.uph.ii.springbootprj.services.PhotoService;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Value("${files.location}")
    private String photoDir;
    @Value("${photos.minWidth}")
    private int minWidth;
    @Value("${photos.mediumWidth}")
    private int mediumWidth;
    @Value("${photos.largeWidth}")
    private int largeWidth;

    @Override
    public void saveFile(String subdir,MultipartFile multipartFile) throws IOException {

        var path = Path.of(photoDir, subdir);
        new File(path.toString()).mkdirs();
        path = Path.of(photoDir, subdir, multipartFile.getOriginalFilename());
        var fos = new FileOutputStream(path.toFile());
        fos.write(multipartFile.getBytes());
        fos.close();
    }

    @Override
    public void saveFileWithResizing(String subdir, MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String mainDir = Path.of(photoDir, subdir).toString();

        var min = Scalr.resize(ImageIO.read(multipartFile.getInputStream()), Scalr.Mode.FIT_TO_WIDTH , minWidth);
        var medium = Scalr.resize(ImageIO.read(multipartFile.getInputStream()), Scalr.Mode.FIT_TO_WIDTH , mediumWidth);
        var large = Scalr.resize(ImageIO.read(multipartFile.getInputStream()), Scalr.Mode.FIT_TO_WIDTH, largeWidth);

        File minfile = new File(Path.of(mainDir,"min",fileName).toString());
        File mediumfile = new File(Path.of(mainDir,"medium",fileName).toString());;
        File largefile = new File(Path.of(mainDir,"large",fileName).toString());;
        minfile.mkdirs(); mediumfile.mkdirs(); largefile.mkdirs();

        var ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        ImageIO.write(min, ext, minfile);
        ImageIO.write(medium, ext, mediumfile);
        ImageIO.write(large, ext, largefile);

    }
}
