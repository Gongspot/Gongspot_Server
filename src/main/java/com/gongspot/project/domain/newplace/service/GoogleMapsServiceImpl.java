package com.gongspot.project.domain.newplace.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleMapsServiceImpl implements GoogleMapsService {

    // 최종 리디렉션 URL 추적
    public String resolveFinalUrl(String shortUrl) {
        String url = shortUrl;
        int maxRedirects = 10;

        try {
            for (int i = 0; i < maxRedirects; i++) {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                int status = conn.getResponseCode();

                if (status == HttpURLConnection.HTTP_MOVED_PERM ||
                        status == HttpURLConnection.HTTP_MOVED_TEMP ||
                        status == HttpURLConnection.HTTP_SEE_OTHER ||
                        status == 307 || status == 308) {

                    String newUrl = conn.getHeaderField("Location");
                    if (newUrl == null) throw new IOException("리디렉션 위치 없음");
                    url = newUrl;
                } else {
                    return url;
                }
            }
            throw new IOException("리디렉션 너무 많음");
        } catch (IOException e) {
            throw new RuntimeException("리디렉션 추적 중 오류 발생", e);
        }
    }

    // URL에서 장소 키워드 추출
    public String extractPlaceKeyword(String url) {
        try {
            String[] segments = new URL(url).getPath().split("/");
            for (int i = 0; i < segments.length; i++) {
                if (segments[i].equals("place") && i + 1 < segments.length) {
                    String encodedKeyword = segments[i + 1];
                    return URLDecoder.decode(encodedKeyword.replace("+", " "), StandardCharsets.UTF_8.name());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("키워드 추출 실패", e);
        }
        return null;
    }

    public String resolveAndExtractKeyword(String shortUrl) {
        String fullUrl = resolveFinalUrl(shortUrl);
        return extractPlaceKeyword(fullUrl);
    }
}
