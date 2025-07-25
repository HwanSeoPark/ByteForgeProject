package com.byteforge.admin.report.content.entity;

import com.byteforge.account.user.domain.User;
import com.byteforge.admin.report.content.dto.ContentReportRequest;
import com.byteforge.common.config.BooleanConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contentReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime reportTime;

    @Column(nullable = false)
    private String content;

    @Convert(converter = BooleanConverter.class)
    @Column(nullable = false)
    private boolean isAction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @OneToOne(cascade = { CascadeType.PERSIST , CascadeType.REMOVE }, fetch = FetchType.LAZY , orphanRemoval = true)
    private ReportData reportData;

    public static ContentReport createContentReport(User user, ContentReportRequest request, ReportData data) {
        return ContentReport.builder()
                .reporter(user)
                .content(request.getContent())
                .isAction(false)
                .reportType(request.getReportType())
                .reportData(data)
                .build();
    }

    public void toggleIsAction() {
        isAction = isAction ? false : true;
    }

}
