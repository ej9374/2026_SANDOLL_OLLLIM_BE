package haennihaesseo.sandoll.domain.font.controller;

import haennihaesseo.sandoll.domain.font.dto.request.FontSettingRequest;
import haennihaesseo.sandoll.domain.font.dto.response.RecommendFontResponse;
import haennihaesseo.sandoll.domain.font.dto.response.RefreshFontResponse;
import haennihaesseo.sandoll.domain.font.entity.enums.FontType;
import haennihaesseo.sandoll.domain.font.service.FontService;
import haennihaesseo.sandoll.domain.font.status.FontSuccessStatus;
import haennihaesseo.sandoll.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Font Setting", description = "폰트 설정 및 추천 API")
@RestController
@RequestMapping("/api/letter")
@RequiredArgsConstructor
@Slf4j
public class FontController {

  private final FontService fontService;

  @Operation(
      summary = "[3.4] 목소리 분석 폰트 적용 / [3.7] 폰트 적용"
  )
  @PostMapping("/font")
  public ResponseEntity<ApiResponse<Void>> applyFont(
      @RequestHeader("letterId") String letterId,
      @RequestBody @Valid FontSettingRequest request
  ) {
    fontService.applyFont(letterId, request.getFontId());
    return ApiResponse.success(FontSuccessStatus.SUCCESS_304);
  }

  @Operation(
      summary = "[3.6] 추천 폰트 리스트 조회"
  )
  @GetMapping("/font")
  public ResponseEntity<ApiResponse<RecommendFontResponse>> getRecommendFont(
      @RequestHeader("letterId") String letterId,
      @RequestParam(name = "type", defaultValue = "VOICE") FontType fontType
  ) {
    RecommendFontResponse response = fontService.getRecommendFonts(letterId, fontType);
    return ApiResponse.success(FontSuccessStatus.SUCCESS_306, response);
  }

  @Operation(
      summary = "[3.9] 보이스 폰트 리스트 새로고침"
  )
  @PostMapping("/font/refresh")
  public ResponseEntity<ApiResponse<RefreshFontResponse>> refreshRecommendFont(
      @RequestHeader("letterId") String letterId
  ) {
    RefreshFontResponse response = fontService.refreshRecommendFonts(letterId);
    return ApiResponse.success(FontSuccessStatus.SUCCESS_309, response);
  }

}
