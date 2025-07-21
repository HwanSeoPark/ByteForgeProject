package com.byteforge.lecture.service;

import com.byteforge.lecture.dto.api.SearchVideosResponse;
import com.byteforge.lecture.dto.api.VideoDetailsResponse;
import com.byteforge.lecture.dto.view.CustomLectureVideoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String apiKey;

    // --- Search API ---
    public SearchVideosResponse callSearchVideos(String keyword) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://www.googleapis.com/youtube/v3/search")
                .queryParam("part", "snippet")
                .queryParam("fields", "items(id.videoId,snippet(publishedAt,title,description,thumbnails/default/url))")
                .queryParam("q", keyword)
                .queryParam("type", "video")
                .queryParam("maxResults", 10)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();
        try {
            return restTemplate.getForObject(uri, SearchVideosResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Youtube Search API 호출 실패: " + e.getMessage(), e);
        }
    }

    // --- Video Detail API ---
    public VideoDetailsResponse callVideoDetails(String videoId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://www.googleapis.com/youtube/v3/videos")
                .queryParam("part", "snippet")
                .queryParam("fields", "items(id,snippet(publishedAt,title,description,thumbnails.default.url))")
                .queryParam("id", videoId)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();
        try {
            return restTemplate.getForObject(uri, VideoDetailsResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Youtube VideoDetails API 호출 실패: " + e.getMessage(), e);
        }
    }

    // --- 데이터 가공: 검색 ---
    public List<CustomLectureVideoDto> getLectureDto(String keyword) {
        SearchVideosResponse response = callSearchVideos(keyword);
        if (response.getItems() == null || response.getItems().isEmpty()) {
            throw new RuntimeException(keyword + "에 대한 데이터가 없습니다.");
        }

        return response.getItems().stream()
                .map(item -> CustomLectureVideoDto.builder()
                        .videoId(item.getId().getVideoId())
                        .videoPublishedAt(item.getSnippet().getPublishedAt())
                        .videoTitle(item.getSnippet().getTitle())
                        .videoDescriptionShort(item.getSnippet().getDescription())
                        .videoThumbnailUrl(item.getSnippet().getThumbnails().getDefaultThumbnail().getUrl())
                        .build())
                .toList();
    }

    // --- 데이터 가공: video 상세 ---
    public List<CustomLectureVideoDto> getVideoDto(String videoId) {
        VideoDetailsResponse response = callVideoDetails(videoId);
        if (response.getItems() == null || response.getItems().isEmpty()) {
            throw new RuntimeException("해당 videoId에 대한 데이터가 없습니다.");
        }

        return response.getItems().stream()
                .map(item -> CustomLectureVideoDto.builder()
                        .videoId(item.getId())
                        .videoPublishedAt(item.getSnippet().getPublishedAt())
                        .videoTitle(item.getSnippet().getTitle())
                        .videoDescription(item.getSnippet().getDescription())
                        .videoThumbnailUrl(item.getSnippet().getThumbnails().getDefaultThumbnail().getUrl())
                        .build())
                .toList();
    }

}
