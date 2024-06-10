package com.zerobase.tabling.data.comparator;

import com.zerobase.tabling.data.dto.StoreDto;

import java.util.Comparator;

public class StoreInfoByRateHighestComparator implements Comparator<StoreDto.StoreInfo> {
    @Override
    public int compare(StoreDto.StoreInfo o1, StoreDto.StoreInfo o2) {
        if (o1.getRate() < o2.getRate()) {
            return 1;
        } else if (o1.getRate().equals(o2.getRate())) {
            return o1.getName().compareTo(o2.getName());
        } else return -1;
    }
}
