package com.gongspot.project.domain.home.service;

import com.gongspot.project.domain.place.entity.Place;

public interface HomeCommandService {
    void updateHotCheck(Place place, int week);
}
