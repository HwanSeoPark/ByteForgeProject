package com.byteforge.admin.report.content.repository;

import com.byteforge.admin.report.content.dto.ContentReportResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomContentReportRepository {

    List<ContentReportResponse> findAllContentReport(Pageable pageable);

}
