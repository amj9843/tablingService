package com.zerobase.tabling.data.comparator;

import com.zerobase.tabling.data.dto.StoreDto;

import java.util.Comparator;

public class StoreInfoByNameComparator implements Comparator<StoreDto.StoreInfo> {
    @Override
    public int compare(StoreDto.StoreInfo o1, StoreDto.StoreInfo o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
