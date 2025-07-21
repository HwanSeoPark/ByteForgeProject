package com.byteforge.admin.report.bug.repository;

import com.byteforge.admin.report.bug.dto.BugReportResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.byteforge.account.user.domain.QUser.user;
import static com.byteforge.admin.report.bug.entity.QBugReport.bugReport;

@RequiredArgsConstructor
public class BugReportRepositoryImpl implements CustomBugReportRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BugReportResponse> findAllBugReport(Pageable pageable) {
        return jpaQueryFactory.select(
                        Projections.constructor(BugReportResponse.class,
                                bugReport.bugReportId,
                                user.id,
                                bugReport.reportTime,
                                bugReport.content,
                                bugReport.isSolved
                        )
                ).from(bugReport)
                .innerJoin(bugReport.reporter , user)
                .orderBy(bugReport.bugReportId.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
