package com.gongspot.project.domain.place.controller;

import com.gongspot.project.common.enums.*;
import com.gongspot.project.common.response.ApiResponse;
import com.gongspot.project.domain.like.dto.LikeResponseDTO;
import com.gongspot.project.domain.like.service.LikeQueryService;
import com.gongspot.project.domain.place.converter.PlaceConverter;
import com.gongspot.project.domain.place.dto.PlaceResponseDTO;
import com.gongspot.project.domain.place.service.PlaceCommandService;
import com.gongspot.project.domain.place.service.PlaceQueryService;
import com.gongspot.project.domain.review.dto.ReviewResponseDTO;
import com.gongspot.project.domain.review.service.ReviewQueryService;
import com.gongspot.project.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
@Tag(name = "공간", description = "공간 관련 API")
@Validated
public class PlaceController {

    private final PlaceQueryService placeQueryService;
    private final PlaceCommandService placeCommandService;
    private final ReviewQueryService reviewQueryService;
    private final LikeQueryService likeQueryService;

    @Operation(summary = "공간 상세조회")
    @GetMapping("/{placeId}")
    public ApiResponse<PlaceResponseDTO.GetPlaceDTO> getPlace(@PathVariable("placeId") Long placeId){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PlaceResponseDTO.GetPlaceDTO result = placeQueryService.getPlace(user.getId(), placeId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공간 리뷰목록 조회", description = "특정 공간의 리뷰 목록을 20개씩 페이징하여 조회합니다.")
    @GetMapping("/{placeId}/reviews")
    public ApiResponse<ReviewResponseDTO.GetReviewListDTO> getReviewList(
            @PathVariable("placeId") Long placeId,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        ReviewResponseDTO.GetReviewListDTO result = reviewQueryService.getReviewList(placeId, page);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공간 찜하기")
    @PostMapping("/{placeId}/isLiked")
    public ApiResponse<String> likedPlace(@PathVariable("placeId") Long placeId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        placeCommandService.isLikedPlace(user.getId(), placeId);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "공간 찜 취소")
    @DeleteMapping("/{placeId}/isLiked")
    public ApiResponse<String> unLikedPlace(@PathVariable("placeId") Long placeId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        placeCommandService.unLikedPlace(user.getId(), placeId);
        return ApiResponse.onSuccess();
    }

    @Operation(summary = "내가 찜한 공간 목록 조회", description = "전체(ALL)/무료(FREE)/유료(PAID) 필터링")
    @GetMapping("/liked")
    public ApiResponse<LikeResponseDTO.LikedPlaceListDTO> getLikedPlaces(
            @RequestParam(name = "isFree", defaultValue = "ALL") String isFree) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LikeResponseDTO.LikedPlaceListDTO result = likeQueryService.getLikedPlaces(user.getId(), isFree);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "공간 혼잡도 목록조회")
    @GetMapping("/{placeId}/congestions")
    public ApiResponse<ReviewResponseDTO.CongestionListDTO> getCongestionList(
            @PathVariable("placeId") Long placeId,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        ReviewResponseDTO.CongestionListDTO congestionList = reviewQueryService.getCongestionList(userId, placeId, page);
        return ApiResponse.onSuccess(congestionList);
    }

    @GetMapping("/")
    @Operation(summary = "공간 검색", description = "쿼리 파라미터에 따라 공간을 검색합니다.")
    public ApiResponse<PlaceResponseDTO.SearchPlaceListDTO> searchPlace(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "purpose", required = false) List<PurposeEnum> purpose,
            @RequestParam(name = "type", required = false) PlaceEnum type,
            @RequestParam(name = "mood", required = false) List<MoodEnum> mood,
            @RequestParam(name = "facilities", required = false) List<FacilitiesEnum> facilities,
            @RequestParam(name = "location", required = false) List<LocationEnum> location,
            @RequestParam(name = "page", defaultValue = "0") Long page) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        List<PlaceResponseDTO.SearchPlaceDTO> placeList = placeQueryService.getFilteredPlaces(userId, keyword, purpose, type, mood, facilities, location, page);
        return ApiResponse.onSuccess(PlaceConverter.toSearchPlaceListDTO(placeList));
    }

    @Operation(summary = "방문한 공간 목록 조회")
    @GetMapping("/visited")
    public ApiResponse<PlaceResponseDTO.VisitedPlaceListDTO> getVisitedPlaces() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        PlaceResponseDTO.VisitedPlaceListDTO result = placeQueryService.getVisitedPlaces(userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "장소별 혼잡도 조회(포인트 사용)")
    @PostMapping("/{placeId}/congestion")
    public ApiResponse<String> viewCongestion(@PathVariable("placeId") Long placeId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        placeCommandService.viewCongestion(user.getId(), placeId);
        return ApiResponse.onSuccess("포인트 사용 완료");
    }
}