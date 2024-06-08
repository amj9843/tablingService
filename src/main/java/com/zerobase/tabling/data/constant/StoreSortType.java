package com.zerobase.tabling.data.constant;

import org.springframework.data.domain.Sort;

public enum StoreSortType {
    NAMEASC("이름순", Sort.by("name").ascending()),

    RATEHIGHEST("평점 높은순", Sort.by("rate").descending()
            .and(Sort.by("name").ascending())),

    RATELOWEST("평점 낮은순", Sort.by("rate").ascending()
            .and(Sort.by("name").ascending())),

    REVIEWCOUNTHIGHEST("리뷰 많은순", Sort.by("reviewCount").descending()
            .and(Sort.by("name").ascending())),

    REVIEWCOUNTLOWEST("리뷰 적은순", Sort.by("reviewCount").ascending()
            .and(Sort.by("name").ascending()));

    private final String description;
    private final Sort sort;

    StoreSortType(String description, Sort sort) {
        this.description = description;
        this.sort = sort;
    }

    public String description() {
        return description;
    }
    public Sort sort() {return sort;}
}
