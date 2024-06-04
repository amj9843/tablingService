package com.zerobase.tabling.data.dto;

import com.zerobase.tabling.data.domain.Store;
import com.zerobase.tabling.data.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StoreDto {
    //매장등록 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistRequest {
        @NotBlank(message = "매장명 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String name;
        @NotBlank(message = "주소 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String location;
        @NotNull
        private String description;

        public Store toEntity(User user){
            return Store.builder()
                    .user(user)
                    .name(this.name)
                    .location(this.location)
                    .description(this.description)
                    .build();
        }
    }

    //매장 등록 정보 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistedStoreInfo {
        private Long ownerId;
        private Long storeId;
        private String name;
        private String location;
        private String description;
    }

    /**
     * 매장 정보 수정 요청 DTO
     * 수정 가능 정보 : 매장명, 장소, 설명
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifiedInfoRequest {
        @Null(message = "매장명이 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String name;
        @Null(message = "주소가 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String location;
        private String description;
    }
}
