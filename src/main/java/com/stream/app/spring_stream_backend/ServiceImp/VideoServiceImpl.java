package com.stream.app.spring_stream_backend.ServiceImp;

import com.stream.app.spring_stream_backend.entites.Video;
import com.stream.app.spring_stream_backend.repository.VideoRepository;
import com.stream.app.spring_stream_backend.services.VideoServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.List;


@Service

public class VideoServiceImpl implements VideoServices {
    @Value("${files.video}")
    String DIR;

    @Autowired
    private VideoRepository videoRepository;

@PostConstruct
    public void init(){
    File file = new File(DIR);
    if(!file.exists()){
        file.mkdir();
        System.out.println("Folder Created:");
        System.out.println("Folder name "+file);
    }
    else{
        System.out.println("Folder already Created ");
        System.out.println("Folder name "+file);
    }
    }

    @Override
    public Video save(Video video, MultipartFile file) {

       try{
           // give us original file name
           String filename= file.getOriginalFilename();
           String contentType = file.getContentType();
           InputStream inputStream = file.getInputStream();



           //  file path
String cleanFileName= StringUtils.cleanPath(filename);
           // folder path : create
String cleanFolder= StringUtils.cleanPath(DIR);

//Combining both of them
           // folder path with filename
           Path path =Paths.get(cleanFolder,cleanFileName);
           System.out.println(contentType);
           System.out.println(path);

//        copy file to folder

           Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);

           // video meta data
           video.setContentType(contentType);
           video.setFilepath(path.toString());
           // meta data save in database

          return videoRepository.save(video);
       }
       catch (IOException e)
       {
           e.printStackTrace();
//           System.out.println(e);
           return null;
       }
    }

    @Override
    public Video get(String videoId) {
   Video video= videoRepository.findById(videoId).orElseThrow(()->new RuntimeException("video not found"));
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }



    // delete the video
    public Video deleteVideo(String videoId) {
        // Check if video exists
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));

        // Delete the video
        videoRepository.delete(video);

        // Return deleted video (optional, just to confirm what was deleted)
        return video;
    }
}
