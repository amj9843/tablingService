package com.zerobase.tabling.service.impl;

import com.zerobase.tabling.component.TimeConverter;
import com.zerobase.tabling.data.domain.Store;
import com.zerobase.tabling.data.domain.StoreDetail;
import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.dto.StoreDto;
import com.zerobase.tabling.data.repository.ReservationRepository;
import com.zerobase.tabling.data.repository.StoreDetailRepository;
import com.zerobase.tabling.data.repository.StoreRepository;
import com.zerobase.tabling.exception.impl.*;
import com.zerobase.tabling.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public Page<StoreDto.StoreInfo> getStoreList(PageRequest pageRequest,
                                                 Comparator<StoreDto.StoreInfo> comparator) {
        //리스트로 전체 목록 받아오기
        List<StoreDto.StoreInfo> storeList = this.storeRepository.getStoreList();
        //리스트 정렬하기
        storeList.sort(comparator);

        //페이지화하기
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), storeList.size());
        return new PageImpl<>(storeList.subList(start, end), pageRequest, storeList.size());
    }

    @Override
    @Transactional
    public Page<StoreDto.StoreInfo> getStoreList(PageRequest pageRequest, String word,
                                                 Comparator<StoreDto.StoreInfo> comparator) {
        //리스트로 검색하기
        List<StoreDto.StoreInfo> storeList = this.storeRepository.getStoreList(word);
        //리스트 정렬하기
        storeList.sort(comparator);

        //페이지화하기
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), storeList.size());
        return new PageImpl<>(storeList.subList(start, end), pageRequest, storeList.size());
    }

    @Override
    @Transactional
    public Page<StoreDto.StoreInfo> getAllStoreListByPartner(Long userId, PageRequest pageRequest,
                                                             Comparator<StoreDto.StoreInfo> comparator) {
        //리스트로 자신이 관리하는 매장만 가져오기
        List<StoreDto.StoreInfo> storeList = this.storeRepository.getStoreListByPartner(userId);
        //리스트 정렬하기
        storeList.sort(comparator);

        //페이지화하기
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), storeList.size());
        return new PageImpl<>(storeList.subList(start, end), pageRequest, storeList.size());
    }

    @Override
    @Transactional
    public StoreDto.Detail getStoreDetail(Long storeId, Pageable pageable) {
        //storeId에 해당하는 매장 정보 StoreInfo 형으로 담아 가져오기(없다면 에러 반환)
        StoreDto.StoreInfo storeInfo = this.storeRepository.getStoreInfoByStoreId(storeId)
                .orElseThrow(NoStoreException::new);

        //리스트로 storeId에 해당하는 매장 상세정보 전부 가져오기(예약 시간순 정렬 고정, 현재 시간 이후의 예약 시간들 정보로만)
        Page<StoreDetailDto.Detail> details = this.storeDetailRepository.getDetailsByStoreId(
                storeId, pageable, LocalDateTime.now());

        return StoreDto.Detail.toDetail(storeInfo, details);
    }

    @Override
    @Transactional
    public StoreDto.RegistedStoreInfo registStore(Long userId, StoreDto.RegistRequest request) {
        //사용자가 등록한 매장들 중 매장명, 위치가 모두 동일한 매장이 있는지 확인
        boolean exists = this.storeRepository.existsByUserIdAndNameAndLocation(
                userId, request.getName(), request.getLocation());
        if (exists) {
            throw new AlreadyExistStoreException();
        }

        //DB에 저장
        Store store = this.storeRepository.save(request.toEntity(userId));

        //보여줄 값만 빼서 return
        return new StoreDto.RegistedStoreInfo(userId,
                store.getStoreId(), store.getName(), store.getLocation(), store.getDescription());
    }

    @Override
    @Transactional
    public void updateStore(Long userId, Long storeId, StoreDto.ModifiedRequest modifiedRequest) {
        //업데이트하려는 매장 정보 가져오기
        Store store = this.storeRepository.findByStoreId(storeId).orElseThrow(NoStoreException::new);

        //자신이 관리하는 매장인지 확인, 아니면 권한없음 반환
        if (!store.getUserId().equals(userId)) {
            throw new NoAuthException();
        }
        
        //입력받은 값이 있는지 없는지 확인
        String name = (modifiedRequest.getName() == null) ? store.getName() : modifiedRequest.getName();
        String location = (modifiedRequest.getLocation() == null) ? store.getLocation() : modifiedRequest.getLocation();
        String description =
                (modifiedRequest.getDescription() == null) ? store.getDescription() : modifiedRequest.getDescription();
        
        //업데이트
        store.update(name, location, description);
    }

    @Override
    @Transactional
    public void deleteStore(Long userId, Long storeId) {
        //자신이 관리하는 매장의 예약 정보인지 권한 확인
        boolean storeOwner
                = this.storeRepository.existsByUserIdAndStoreId(userId, storeId);
        if (!storeOwner) {
            throw new NoAuthException();
        }

        //해당 매장에 승인 요청중이거나 진행중인 예약 내역이 있는지 확인
        boolean exists = this.reservationRepository.existsProgressReservationByStoreId(storeId);
        if (exists) {
            throw new CannotDeleteCauseReservationException();
        }

        this.storeRepository.deleteByStoreId(storeId);
    }

    @Override
    @Transactional
    public StoreDetailDto.RegistedStoreDetails registStoreDetail(
            Long userId, Long storeId, List<StoreDetailDto.RegistRequest> requests) {
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
                throw new AlreadyExistStoreDetailException();
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

    @Override
    @Transactional
    public void updateStoreDetail(Long userId, Long storeDetailId, StoreDetailDto.ModifiedRequest request) {
        //업데이트하려는 매장 상세정보 가져오기
        StoreDetail storeDetail = this.storeDetailRepository.findByStoreDetailId(storeDetailId)
                .orElseThrow(NoStoreDetailException::new);

        //매장 상세정보와 관련된 예약자(신청, 승인된 사람들)가 총 몇명인지 확인
        int headCount = this.reservationRepository.countHeadCountByStoreDetailId(storeDetailId);

        //이미 예약한 사람 수보다 바꾸려는 수가 더 적으면 에러
        if (request.getHeadCount() < headCount) {
            throw new CannotChangeHeadCountException();
        }

        storeDetail.update(request.getHeadCount());
    }

    @Override
    @Transactional
    public void deleteStoreDetail(Long userId, Long storeDetailId) {
        //자신이 관리하는 매장의 예약 정보인지 권한 확인
        boolean storeOwner
                = this.storeDetailRepository.existsFindByUserIdAndStoreDetailId(userId, storeDetailId);
        if (!storeOwner) {
            throw new NoAuthException();
        }

        //상세 정보와 관련하여 승인 요청중이거나 진행중인 예약 내역이 있는지 확인
        boolean exists = this.reservationRepository.existsProgressReservationByStoreDetailId(storeDetailId);
        if (exists) {
            throw new CannotDeleteCauseReservationException();
        }

        this.storeDetailRepository.deleteByStoreDetailId(storeDetailId);
    }
}
