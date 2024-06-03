package com.zerobase.tabling.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisComponent {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 레디스에 정보 저장
     * @param key 키
     * @param data 값
     */
    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    /**
     * 레디스의 배열로 저장
     * @param key 키
     * @param data 값
     */
    public void setValuesList(String key, String data) {
        redisTemplate.opsForList().rightPushAll(key,data);
    }

    /**
     * 레디스의 배열값 키로 호출
     * @param key 키
     * @return List
     */
    public List<String> getValuesList(String key) {
        Long len = redisTemplate.opsForList().size(key);
        return len == 0 ? new ArrayList<>() : redisTemplate.opsForList().range(key, 0, len-1);
    }

    /**
     * 레디스 생명주기가 있는 값 생성
     * @param key 키
     * @param data 값
     * @param duration 시간
     */
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    /**
     * 레디스에서 키로 값 호출
     * @param key 키
     * @return 값
     */
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    /**
     * 레디스에서 키에 해당하는 값 삭제
     * @param key 키
     */
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 레디스에 키가 있는지 확인
     * @param key 키
     * @return boolean
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
