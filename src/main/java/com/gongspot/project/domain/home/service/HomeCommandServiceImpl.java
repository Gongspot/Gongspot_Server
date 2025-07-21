package com.gongspot.project.domain.home.service;

import com.gongspot.project.domain.home.entity.HotCheck;
import com.gongspot.project.domain.home.repository.HotCheckRepository;
import com.gongspot.project.domain.place.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeCommandServiceImpl implements HomeCommandService {

    private final HotCheckRepository hotCheckRepository;
    @Override
    public void updateHotCheck(Place place, int week) {
        HotCheck hotCheck = hotCheckRepository.findByPlaceAndWeek(place, week).orElse(null);
        if (hotCheck != null) {
            LocalDateTime updatedAt = hotCheck.getUpdatedAt();
            if (updatedAt != null && updatedAt.isAfter(LocalDateTime.now().minusDays(7))) {
                hotCheck.increaseCount(); // cnt += 1
            } else {
                hotCheck.resetCount(); // cnt = 1
            }
        } else {
            // 새로 등록
            HotCheck newHotCheck = HotCheck.builder()
                    .place(place)
                    .week(week)
                    .cnt(1L)
                    .build();
            hotCheckRepository.save(newHotCheck);
        }
    }
}
