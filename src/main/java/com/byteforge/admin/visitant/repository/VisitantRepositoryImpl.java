package com.byteforge.admin.visitant.repository;

import com.byteforge.admin.visitant.domain.QVisitant;
import com.byteforge.admin.visitant.dto.VisitantResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VisitantRepositoryImpl implements CustomVisitantRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<VisitantResponse> findVisitantCountByVisitDate() {
        return queryFactory.select(Projections.constructor(VisitantResponse.class
                        , QVisitant.visitant.count(), QVisitant.visitant.visitDate ))
                .from(QVisitant.visitant)
                .groupBy(QVisitant.visitant.visitDate)
                .orderBy(QVisitant.visitant.visitDate.asc())
                .limit(20L)
                .fetch();
    }
}
