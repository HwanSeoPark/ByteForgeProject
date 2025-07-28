package com.byteforge.lecture.service;

import com.byteforge.lecture.dto.SearchResponse;
import com.byteforge.lecture.dto.VideosResponse;
import com.byteforge.lecture.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String apiKey;

    // --- Search API 호출 ---
    public SearchResponse callSearchAPI(String keyword) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://www.googleapis.com/youtube/v3/search")
                .queryParam("part", "id")
                .queryParam("fields", "items(id/videoId)")
                .queryParam("q", keyword)
                .queryParam("type", "video")
                .queryParam("maxResults", 20)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();

        try {
            return restTemplate.getForObject(uri, SearchResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Youtube Search API 호출 실패: " + e.getMessage(), e);
        }
    }

    // --- Video API 호출 ---
    public VideosResponse callVideosAPI(List<String> videoIds) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://www.googleapis.com/youtube/v3/videos")
                .queryParam("part", "snippet")
                .queryParam("fields", "items(id,snippet(publishedAt,title,thumbnails/standard/url,thumbnails/high/url,tags))")
                .queryParam("id", String.join(",", videoIds))
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();

        try {
            return restTemplate.getForObject(uri, VideosResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Youtube Video API 호출 실패: " + e.getMessage(), e);
        }
    }

    // 통합 DTO 반환: search는 videoId 수집용으로만 쓰고, videos에서 DTO 생성
    public List<LectureDto> getLectureDto(String keyword) {
        // 1. Search API 호출
        SearchResponse searchResponse = callSearchAPI(keyword);
        if (searchResponse.getItems() == null || searchResponse.getItems().isEmpty()) {
            throw new RuntimeException(keyword + "에 대한 검색 결과가 없습니다.");
        }

        // 2. videoId 리스트만 추출
        List<String> videoIds = searchResponse.getItems().stream()
                .map(item -> item.getId().getVideoId())
                .collect(Collectors.toList());

        // 3. Videos API 호출
        VideosResponse videosResponse = callVideosAPI(videoIds);
        if (videosResponse.getItems() == null || videosResponse.getItems().isEmpty()) {
            throw new RuntimeException("해당 영상에 대한 정보가 없습니다.");
        }

        // 4. Videos에서 DTO 생성 후 반환
        return LectureDto.fromVideoDetailsResponse(videosResponse);
    }
}
