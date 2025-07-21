package com.byteforge.lecture.controller;

import com.byteforge.lecture.dto.api.SearchVideosResponse;
import com.byteforge.lecture.dto.view.CustomLectureVideoDto;
import com.byteforge.lecture.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class YoutubeSearchController {
    private final YoutubeService youtubeService;

    // Youtube API 원본
    @GetMapping
    public SearchVideosResponse searchVideos(@RequestParam("query") String query) {
        return youtubeService.callSearchVideos(query);
    }

    // Custom DTO
    @GetMapping("/custom")
    public ResponseEntity<?> searchLecture(@RequestParam("query") String query) {
        try{
            List<CustomLectureVideoDto> result = youtubeService.getLectureDto(query);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
