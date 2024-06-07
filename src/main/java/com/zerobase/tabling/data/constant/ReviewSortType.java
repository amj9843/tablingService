package com.zerobase.tabling.data.constant;

import org.springframework.data.domain.Sort;

public enum ReviewSortType {
    CREATEDATDESC("최근 등록순", Sort.by("createdAt").descending()),
    CREATEDATASC("등록순", Sort.by("createdAt").ascending()),

    RATEHIGHEST("평점 높은순", Sort.by("rate").descending()
            .and(Sort.by("createdAt").descending())),

    RATELOWEST("평점 낮은순", Sort.by("rate").ascending()
            .and(Sort.by("createdAt").ascending()));

    private final String description;
    private final Sort sort;

    ReviewSortType(String description, Sort sort) {
        this.description = description;
        this.sort = sort;
    }

    public String description() {
        return description;
    }
    public Sort sort() {return sort;}
}
