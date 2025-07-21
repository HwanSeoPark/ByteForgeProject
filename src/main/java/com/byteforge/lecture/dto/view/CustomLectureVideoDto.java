package com.byteforge.lecture.dto.view;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomLectureVideoDto {
    private String videoId;
    private String videoPublishedAt;
    private String videoTitle;
    private String videoDescriptionShort;
    private String videoDescription;
    private String videoThumbnailUrl;
}
