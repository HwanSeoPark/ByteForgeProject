package com.byteforge.lecture.domain;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    private String lectureTitle;

    @Column(nullable = false)
    private String lectureBio;

    @Column(nullable = false)
    private String lectureDescription;

    @Column(name = "lecture_img", nullable = false)
    private String lectureThumbnailUrl;

    @Column(name = "lecture_publishedAt")
    private DateTime lecturePublishedAt;

}