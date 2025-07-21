package com.byteforge.lecture.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchVideosResponse {
    private List<SearchItem> items;

    @Data
    public static class SearchItem {
        private Id id;
        private Snippet snippet;
    }

    @Data
    public static class Id {
        private String videoId;
    }

    @Data
    public static class Snippet {
        private String publishedAt;
        private String title;
        private String description;
        private Thumbnails thumbnails;
    }

    @Data
    public static class Thumbnails {
        @com.fasterxml.jackson.annotation.JsonProperty("default")
        private Thumbnail defaultThumbnail;
    }

    @Data
    public static class Thumbnail {
        private String url;
    }
}
