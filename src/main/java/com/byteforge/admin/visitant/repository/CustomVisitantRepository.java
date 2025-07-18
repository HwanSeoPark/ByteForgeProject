package com.byteforge.admin.visitant.repository;

import com.byteforge.admin.visitant.dto.VisitantResponse;

import java.util.List;

public interface CustomVisitantRepository {

    List<VisitantResponse> findVisitantCountByVisitDate();

}
