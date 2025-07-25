package com.byteforge.admin.report.content.dto;

import com.byteforge.admin.report.content.entity.ReportType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ContentReportResponse {

    private long contentReportId;

    private String reporter;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:MM:SS" , timezone = "Asia/Seoul")
    private LocalDateTime reportTime;

    private String content;

    private boolean isAction;

    private ReportType contentType;

    private ReportDataResponse reportData;

}
