package com.gongspot.project.domain.newplace.service;

public interface GoogleMapsService {
    String resolveFinalUrl(String shortUrl);
    String extractPlaceKeyword(String url);
    String resolveAndExtractKeyword(String shortUrl);
}
