package com.byteforge.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private List<SearchItem> items;

    @Data
    public static class SearchItem {
        private Id id;
    }

    @Data
    public static class Id {
        private String videoId;
    }
}
