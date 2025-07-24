package com.byteforge.lecture.controller;

import com.byteforge.lecture.dto.LectureDto;
import com.byteforge.lecture.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class YoutubeController {

    private final YoutubeService youtubeService;

    @GetMapping()
    public ResponseEntity<?> searchLecture(@RequestParam("query") String query) {
        try {
            List<LectureDto> result = youtubeService.getLectureDto(query);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
