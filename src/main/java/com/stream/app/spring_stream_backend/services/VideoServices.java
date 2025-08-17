package com.stream.app.spring_stream_backend.services;

import com.stream.app.spring_stream_backend.entites.Video;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoServices {

    // save video

    Video save(Video video , MultipartFile file);

    // get video
   Video get(String videoId);

    // get video by title

    Video getByTitle(String title);

    List<Video> getAll();


    // delete video
    Video deleteVideo(String videoId);
}
