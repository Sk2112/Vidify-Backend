package com.stream.app.spring_stream_backend.Controllers;

import com.stream.app.spring_stream_backend.AppConstants;
import com.stream.app.spring_stream_backend.payload.CustomMessage;
import com.stream.app.spring_stream_backend.entites.Video;
import com.stream.app.spring_stream_backend.services.VideoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@CrossOrigin("*")
public class VideoController
{
    @Autowired
    private VideoServices videoServices;


    // video saved or upload
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam("file")MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description
            ){
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());

       Video savedVideo = videoServices.save(video,file);
       if(savedVideo!=null){
           return ResponseEntity.status(HttpStatus.OK).body(video);
       }else {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomMessage.builder().message("Video Not Uploaded").success(false).build());
       }
    }


    // video Streaming
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(@PathVariable String videoId){

        Video video = videoServices.get(videoId);

        String contentType = video.getContentType();
        String filePath=video.getFilepath();
        Resource resource=new FileSystemResource(filePath);

        if(contentType==null){
            contentType="application/octet-stream";
        }

return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

    }

    // get all videos
     @GetMapping()
    public List<Video> getAll(){
         return videoServices.getAll();
     }

// stream video in chunks
    @GetMapping("/stream/range/{videoId}")
    public  ResponseEntity<Resource> streamVideoRange(@PathVariable String videoId
    ,@RequestHeader(value = "Range" , required = false) String range
    )
    {
        System.out.println(range);

        Video video=videoServices.get(videoId);
        Path path= Paths.get(video.getFilepath());
      Resource resource= new FileSystemResource(path);

      String contentType = video.getContentType();
      if(contentType==null){
          contentType="application/octet-stream";
      }


      // file length
      long fileLength   = path.toFile().length();
      if(range==null){
          return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
      }


      // calculating start and end
      long rangeStart;
      long rangeEnd;


  String[] ranges = range.replace("bytes=" , "").split("-");
    rangeStart=Long.parseLong(ranges[0]);


    rangeEnd = rangeStart+ AppConstants.CHUNK_SIZE-1;

if(rangeEnd>= fileLength){
    rangeEnd=fileLength-1;
}



//    if(ranges.length>1){
//        rangeEnd=Long.parseLong(ranges[1]);
//    }else {
//     rangeEnd=fileLength-1;
//    }
//
//    if(rangeEnd>fileLength-1){
//        rangeEnd=fileLength-1;
//    }
        System.out.println("Range Start :"+ rangeStart);
        System.out.println("Range End "+ rangeEnd);
        InputStream inputStream;

    try{
        inputStream= Files.newInputStream(path);
         inputStream.skip(rangeStart);
         long contentLength = rangeEnd-rangeStart+1;

         byte[] data = new byte[(int) contentLength];
         int read = inputStream.read(data,0,data.length);
        System.out.println("Reading number of bytes"+read);


        HttpHeaders Headers= new HttpHeaders();
        Headers.add("Content-Range","bytes " +rangeStart + "-" + rangeEnd + "/" + fileLength);
        Headers.add("Cache-Control","no-cache ,no-store ,must-revalidate");
        Headers.add("Pragma","no-cache");
        Headers.add("Expires","0");
        Headers.add("X-Content-Type-Options","nosniff");
        Headers.setContentLength(contentLength);

        System.out.println("Video Player is working Fine");
        System.out.println("---------------------------");
        return  ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(Headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new ByteArrayResource(data));

    } catch (IOException ex) {
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



    }


// delete the video
@DeleteMapping("/{id}")
public ResponseEntity<Video> deleteVideo(@PathVariable("id") String videoId) {
    Video deletedVideo = videoServices.deleteVideo(videoId);
    System.out.println("Deletion is working fine");
    return ResponseEntity.ok(deletedVideo);
}




}
