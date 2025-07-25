package com.byteforge.admin.report.bug.repository;

import com.byteforge.admin.report.bug.dto.BugReportResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBugReportRepository {

    List<BugReportResponse> findAllBugReport(Pageable pageable);

}
