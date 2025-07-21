package com.byteforge.lecture.controller;

import com.byteforge.lecture.dto.api.VideoDetailsResponse;
import com.byteforge.lecture.dto.view.CustomLectureVideoDto;
import com.byteforge.lecture.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class YoutubeVideoController {

    private final YoutubeService youtubeService;

    // Youtube API 원본
    @GetMapping
    public VideoDetailsResponse searchVideos(@RequestParam("videoId") String videoId) {
        return youtubeService.callVideoDetails(videoId);
    }

    // Custom DTO
    @GetMapping("/custom")
    public ResponseEntity<?> videoDetail(@RequestParam("videoId") String videoId) {
        try{
            List<CustomLectureVideoDto> result = youtubeService.getVideoDto(videoId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

