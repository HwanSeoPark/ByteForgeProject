package com.byteforge.lecture.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {
    private String videoId;
    private String videoPublishedAt;
    private String videoTitle;
    private String videoThumbnailUrl;
    private List<String> videoTags;

    // VideoResponse로부터 DTO 리스트 생성
    public static List<LectureDto> fromVideoDetailsResponse(VideosResponse response) {
        List<LectureDto> list = new ArrayList<>();
        for (VideosResponse.VideoItem item : response.getItems()) {
            String standardUrl = item.getSnippet().getThumbnails().getStandardThumbnail() != null ?
                    item.getSnippet().getThumbnails().getStandardThumbnail().getUrl() : null;
            String highUrl = item.getSnippet().getThumbnails().getHighThumbnail() != null ?
                    item.getSnippet().getThumbnails().getHighThumbnail().getUrl() : null;

            String thumbnailUrl = (standardUrl != null && !standardUrl.isEmpty()) ? standardUrl : highUrl;

            list.add(LectureDto.builder()
                    .videoId(item.getId())
                    .videoPublishedAt(item.getSnippet().getPublishedAt())
                    .videoTitle(item.getSnippet().getTitle())
                    .videoThumbnailUrl(thumbnailUrl)
                    .videoTags(item.getSnippet().getTags())
                    .build());
        }
        return list;
    }
}
