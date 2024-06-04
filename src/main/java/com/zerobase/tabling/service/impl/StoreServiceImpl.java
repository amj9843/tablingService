package com.zerobase.tabling.service.impl;

import com.zerobase.tabling.component.TimeConverter;
import com.zerobase.tabling.data.domain.Store;
import com.zerobase.tabling.data.domain.StoreDetail;
import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.dto.StoreDto;
import com.zerobase.tabling.data.repository.StoreDetailRepository;
import com.zerobase.tabling.data.repository.StoreRepository;
import com.zerobase.tabling.exception.impl.AlreadyExistsStoreDetailException;
import com.zerobase.tabling.exception.impl.AlreadyExistsStoreException;
import com.zerobase.tabling.exception.impl.NoAuthException;
import com.zerobase.tabling.exception.impl.NoStoreException;
import com.zerobase.tabling.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreDetailRepository storeDetailRepository;
    private final TimeConverter timeConverter;

    @Override
    @Transactional
    public StoreDto.RegistedStoreInfo registStore(Long userId, StoreDto.RegistRequest request) {
        //사용자가 등록한 매장들 중 매장명, 위치가 모두 동일한 매장이 있는지 확인
        boolean exists = this.storeRepository.existsByUserIdAndNameAndLocation(userId, request.getName(), request.getLocation());
        if (exists) {
            throw new AlreadyExistsStoreException();
        }

        //DB에 저장
        Store store = this.storeRepository.save(request.toEntity(userId));

        //보여줄 값만 빼서 return
        return new StoreDto.RegistedStoreInfo(userId,
                store.getStoreId(), store.getName(), store.getLocation(), store.getDescription());
    }

    @Override
    @Transactional
    public StoreDetailDto.RegistedStoreDetails registStoreDetail(Long userId, Long storeId, List<StoreDetailDto.RegistRequest> requests) {
        //등록하려는 매장이 있는지 확인
        Store store = this.storeRepository.findByStoreId(storeId).orElseThrow(NoStoreException::new);

        //기능을 시행하는 자가 등록하려는 매장의 점장인지 확인
        if (!Objects.equals(store.getUserId(), userId)) {
            throw new NoAuthException();
        }
        
        //엔티티 모아둘 리스트
        List<StoreDetail> storeDetails = new ArrayList<>();
        //입력 시간 중복 확인 리스트
        Set<String> times = new HashSet<>();
        int setSize = 0;

        //등록하려는 매장에 등록하려는 예약 가능 시간이 있는지 확인하며 등록할 객체 생성
        for (StoreDetailDto.RegistRequest request: requests) {
            times.add(request.getReservationTime());
            //이미 이전에 넣은 항목 중 시간이 일치하는 게 있다면 이번 값은 넘어감
            if (setSize == times.size()) continue;
            else setSize++;

            //패턴 분석을 위해 String 으로 받은 예약 시간 LocalDateTime 형식으로 전환
            LocalDateTime requestTime = this.timeConverter.stringToLocalDateTime(request.getReservationTime());

            boolean exists = this.storeDetailRepository.existsByStoreIdAndReservationTime(storeId, requestTime);
            if (exists) {
                throw new AlreadyExistsStoreDetailException();
            }

            StoreDetail storeDetail = request.toEntity(storeId, requestTime);
            storeDetails.add(storeDetail);
        }

        List<StoreDetail> savedStoreDetails = this.storeDetailRepository.saveAll(storeDetails);

        return new StoreDetailDto.RegistedStoreDetails(
                storeId,
                savedStoreDetails.stream()
                        .map(StoreDetailDto.RegistedStoreDetail::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}
