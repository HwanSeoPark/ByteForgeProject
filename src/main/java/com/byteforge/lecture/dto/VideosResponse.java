package com.byteforge.lecture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideosResponse {
    private List<VideoItem> items;

    @Data
    public static class VideoItem {
        private String id;
        private Snippet snippet;
    }

    @Data
    public static class Snippet {
        private String publishedAt;
        private String title;
        private Thumbnails thumbnails;
        private List<String> tags;
    }

    @Data
    public static class Thumbnails {
        @JsonProperty("standard")
        private Thumbnail standardThumbnail;

        @JsonProperty("high")
        private Thumbnail highThumbnail;
    }

    @Data
    public static class Thumbnail {
        private String url;
    }
}
