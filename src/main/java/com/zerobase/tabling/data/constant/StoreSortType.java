package com.zerobase.tabling.data.constant;

import com.zerobase.tabling.data.comparator.*;
import com.zerobase.tabling.data.dto.StoreDto;

import java.util.Comparator;

public enum StoreSortType {
    NAMEASC("이름순", new StoreInfoByNameComparator()),

    RATEHIGHEST("평점 높은순", new StoreInfoByRateHighestComparator()),

    RATELOWEST("평점 낮은순", new StoreInfoByRateLowestComparator()),

    REVIEWCOUNTHIGHEST("리뷰 많은순", new StoreInfoByReviewCountHighestComparator()),

    REVIEWCOUNTLOWEST("리뷰 적은순", new StoreInfoByReviewCountLowestComparator());

    private final String description;
    private final Comparator<StoreDto.StoreInfo> comparator;

    StoreSortType(String description, Comparator<StoreDto.StoreInfo> comparator) {
        this.description = description;
        this.comparator = comparator;
    }

    public String description() {
        return description;
    }
    public Comparator<StoreDto.StoreInfo> comparator() {return comparator;}
}
